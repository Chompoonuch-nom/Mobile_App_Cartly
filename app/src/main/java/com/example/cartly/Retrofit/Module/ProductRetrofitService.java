package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.ProductApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง ProductApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class ProductRetrofitService {
    private static ProductApiServiceInterface instance;
    private ProductRetrofitService() {}

    public static synchronized ProductApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(ProductApiServiceInterface.class);
        }
        return instance;
    }
}
