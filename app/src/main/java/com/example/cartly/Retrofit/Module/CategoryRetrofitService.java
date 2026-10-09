package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.CategoryApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง CategoryApiServiceInterface จาก RetrofitSErvice.getREtrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class CategoryRetrofitService {
    private static CategoryApiServiceInterface instance;
    private CategoryRetrofitService() {}
    public static synchronized CategoryApiServiceInterface getInstance() {
        if ((instance == null)) {
            instance = RetrofitService.getRetrofitInstance().create(CategoryApiServiceInterface.class);
        }
        return instance;
    }
}
