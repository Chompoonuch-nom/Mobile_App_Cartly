package com.example.cartly.Util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * เลือกรูปจาก "ถ่ายรูป" หรือ "แกลเลอรี" - ใช้ซ้ำได้ทั้งใน Activity และ Fragment
 * วิธีใช้ (ต้องสร้างเป็น Field ของ Activity/Fragment เพื่อให้ลงทะเบียนทันก่อนหน้าจอเริ่มทำงาน) :
 *    private final ImagePicker imagePicker = new ImagePicker(this, uri -> upload(uri));
 *    ...
 *    button.setOnClickListener(v -> imagePicker.showChooser(this)); // Fragment ใช้ requireContext()
 * - ถ่ายรูป: เปิดแอปกล้องของระบบ ไม่ต้องขอสิทธิ์ CAMERA
 * - แกลเลอรี: Photo Picker ของระบบ (Android เก่าจะ fallback เป็นตัวเลือกไฟล์) ไม่ต้องขอสิทธิ์อ่านรูป
 */
public class ImagePicker {
    public interface Callback {
        /** @param uri content:// URI ของรูปที่เลือก/ถ่าย (อ่านได้ด้วย ContentResolver) */
        void onImagePicked(Uri uri);
    }

    /** static เพื่อให้ผลลัพธ์จากกล้องไม่หายถ้า Activity ที่ถูกสร้างใหม่ระหว่างที่อยู่ในแอปกล้อง */
    private static Uri pendingCameraUri;

    private final ActivityResultLauncher<PickVisualMediaRequest> galleryLauncher;
    private final ActivityResultLauncher<Uri> cameraLauncher;

    public ImagePicker(ActivityResultCaller caller, Callback callback) {
        galleryLauncher = caller.registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) callback.onImagePicked(uri); // null = ผู้ใช้ยกเลิก
                });

        cameraLauncher = caller.registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    Uri uri = pendingCameraUri;
                    pendingCameraUri = null;
                    if (Boolean.TRUE.equals(success) && uri != null) callback.onImagePicked(uri);
                });
    }

    /** แสดงตัวเลือก "ถ่ายรูป / เลือกจากแกลเลอรี" */
    public void showChooser(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("เลือกรูปภาพ")
                .setItems(new String[]{"ถ่ายรูป", "เลือกจากแกลเลอรี"}, (dialog, which) -> {
                    if (which == 0) launchCamera(context);
                    else launchGallery();
                })
                .show();
    }

    private void launchGallery() {
        galleryLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void launchCamera(Context context) {
        try {
            File dir = new File(context.getCacheDir(), "image"); // ตรงกับ res/xml/file_paths.xml
            if (!dir.exists() && !dir.mkdirs()) throw new IOException("สร้างโฟล์เดอร์ไม่ได้");
            File file = File.createTempFile("camera_", ".jpg", dir);
            pendingCameraUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            cameraLauncher.launch(pendingCameraUri);
        } catch (IOException e) {
            Toast.makeText(context, "เตรียมไฟล์สำหรับถ่ายรูปไม่สำเร็จ", Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException e) {
            pendingCameraUri = null;
            Toast.makeText(context, "ไม่พบแอปกล้องในเครื่องนี้ ลองเลือกจากแกลเลอรีแทน", Toast.LENGTH_SHORT).show();
        }
    }
}
