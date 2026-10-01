package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.AuthApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง AuthApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * สร้างครั้งเดียวแล้วใช้ซ้ำ
 */
public class AuthRetrofitService {
    private static AuthApiServiceInterface instance;
    private AuthRetrofitService() {}
    public static synchronized AuthApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(AuthApiServiceInterface.class);
        }
        return instance;
    }
}
