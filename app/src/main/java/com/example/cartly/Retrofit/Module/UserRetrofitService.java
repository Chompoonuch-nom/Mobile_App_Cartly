package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.UserApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง UserApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class UserRetrofitService {
    private static UserApiServiceInterface instance;
    private UserRetrofitService(){}
    public static synchronized UserApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(UserApiServiceInterface.class);
        }
        return instance;
    }
}
