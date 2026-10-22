package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.UploadApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง UploadApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class UploadRetrofitService {
    private static UploadApiServiceInterface instance;

    private UploadRetrofitService() {}

    public static synchronized UploadApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(UploadApiServiceInterface.class);
        }
        return instance;
    }
}
