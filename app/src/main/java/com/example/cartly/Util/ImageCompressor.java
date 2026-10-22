package com.example.cartly.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
//noinspection ExifInterface
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * เตรียมรูปก่อนอัปโหลด: ย่อให้ด้านยาวสุดไม่เกิน 1280px, หมุนให้ตรงตาม EXIF (รูปหลังจากกล้องมักเอียง)ม
 * แปลงเป็น JPEG คุณภาพ 85 - รูปจากกล้อง 12MP ที่ ~5MB จะเหลือราว 200-400KB (และไม่ชนขีดจำกัด 5MB ของ backend)
 * ควรเรียกจาด background thread
 */
public class ImageCompressor {
    private static final int MAX_SIDE_PX = 1280;
    private static final int JPEG_QUANTITY = 85;

    public static File compressToJpeg(Context context, Uri uri) throws IOException {
        ContentResolver resolver = context.getContentResolver();

        try {
            // 1. อ่านขนาดจริงก่อน (ไม่โหลดพิกเซล)
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            try(InputStream in = resolver.openInputStream(uri)) {
                if (in == null) throw new IOException("เปิดไฟล์รูปไม่ได้");
                BitmapFactory.decodeStream(in, null, bounds);
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) throw new IOException("ไฟล์นี้ไม่มีรูปภาพที่อ่านได้");

            // 2. ลดขนาดตอน decode (ยกกำลัง 2) เพื่อไม่ให้หน่วยความจำเต็ม
            int longest = Math.max(bounds.outWidth, bounds.outHeight);
            int sample = 1;
            while (longest / (sample * 2) >= MAX_SIDE_PX) sample *= 2;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = sample;
            Bitmap bitmap;
            try(InputStream in = resolver.openInputStream(uri)) {
                bitmap = BitmapFactory.decodeStream(in, null, opts);
            }
            if (bitmap == null) throw new IOException("ถอดรหัสรูปภาพไม่สำเร็จ");

            // 3. ย่อให้พอดี MAX_SIDE_PX + หมุนตาม EXIF
            Matrix matrix = new Matrix();
            float scale = (float) MAX_SIDE_PX / Math.max(bitmap.getWidth(), bitmap.getHeight());
            if (scale < 1f) matrix.postScale(scale, scale);
            int rotation = readExifRotation(resolver, uri);
            if (rotation != 0) matrix.postRotate(rotation);
            if (!matrix.isIdentity()) {
                Bitmap transformed = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (transformed != bitmap) {
                    bitmap.recycle();
                    bitmap = transformed;
                }
            }

            // 4. JPEG ไม่มี alpha: รูป PNG โปร่งใสจะกลายเป็นพื้นดำ -> วางบนพื้นขาวก่อน
            if (bitmap.hasAlpha()) {
                Bitmap flat = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(flat);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                bitmap.recycle();
                bitmap = flat;
            }

            // 5. เขียนเป้นไฟล์ชั่วคราวใน cache
            File dir = new File(context.getCacheDir(), "images");
            if (!dir.exists() && !dir.mkdirs()) throw new IOException("สร้างโฟล์เดอร์ชั่วคราวไม่ได้");
            File out = File.createTempFile("upload_", ".jpg", dir);
            try(FileOutputStream fos = new FileOutputStream(out)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUANTITY, fos);
            }
            bitmap.recycle();
            return out;
        } finally {
            deleteIfOwnCameraFile(context, uri);
        }
    }

    private static int readExifRotation(ContentResolver resolver, Uri uri) {
        try(InputStream in = resolver.openInputStream(uri)) {
            if (in == null) return  0;
            int orientation = new ExifInterface(in)
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90: return 90;
                case ExifInterface.ORIENTATION_ROTATE_180: return 180;
                case ExifInterface.ORIENTATION_ROTATE_270: return 270;
                default: return 0;
            }
        } catch (IOException | RuntimeException e) {
            return 0; // อ่าน EXIF ไม่ได้ก็ใช้รูปตามเดิม
        }
    }

    /** ไฟล์ที่แอปกล้องเขียนลง cache ของเรา (FileProvider) ไม่ใช้แล้วหลังย่อรูป -> ลบทิ้งสะสม */
    private static void deleteIfOwnCameraFile(Context context, Uri uri) {
        try {
            if ((context.getPackageName() + ".fileprovider").equals(uri.getAuthority())) {
                context.getContentResolver().delete(uri, null, null);
            }
        } catch (RuntimeException ignored) {
            // ลบได้ไม่เป็นไร (cache ถูกล้างโดยระบบได้เอง)
        }
    }
}
