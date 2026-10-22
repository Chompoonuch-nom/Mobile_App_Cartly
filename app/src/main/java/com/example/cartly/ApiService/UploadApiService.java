package com.example.cartly.ApiService;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.example.cartly.ApiRetrofitInterface.UploadApiServiceInterface;
import com.example.cartly.ResponseDao.UploadResultDao;
import com.example.cartly.Retrofit.Module.UploadRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.ImageCompressor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Module: Upload - อัปโหลดรูปจากเครื่อง (ถ่ายรูป/แกลเลอรี) ไปเก็บที่ backend
 * ย่อ/หมุนรูปภาพ background thread ก่อน แล้วค่อยส่ง; callback ทำงานบน main thread เหมือน ApiService อื่น
 * ผลลัพธ์ UploadResult.getUrl() เอาไปใช้กับ ProductApiService.addImage / CAtegoryRequest.iconUrl / UserApiService.updateAvatar
 */
public class UploadApiService {
    private static final ExecutorService IO = Executors.newSingleThreadExecutor();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    public final UploadApiServiceInterface api = UploadRetrofitService.getInstance();

    public void uploadImage(Context context, Uri imageUri, ApiCallback<UploadResultDao> callback) {
        final Context appContext = context.getApplicationContext();
        IO.execute(() -> {
            final File file;
            try {
                file = ImageCompressor.compressToJpeg(appContext, imageUri);
            } catch (IOException | RuntimeException e) {
                MAIN.post(() -> callback.onError("อ่านรูปภาพไม่สำเร็จ: " + e.getMessage(), ApiCallHandler.NETWORK_ERROR));
                return;
            }

            RequestBody body = RequestBody.create(file, MediaType.parse("image/jpeg"));
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", "image.jpg", body);

            ApiCallHandler.enqueue(api.uploadImage(part), new ApiCallback<UploadResultDao>() {
                @Override
                public void onSuccess(UploadResultDao data, String message) {
                    file.delete();
                    callback.onSuccess(data, message);
                }

                @Override
                public void onError(String message, int httpCode) {
                    file.delete();
                    callback.onError(message, httpCode);
                }
            });
        });
    }
}
