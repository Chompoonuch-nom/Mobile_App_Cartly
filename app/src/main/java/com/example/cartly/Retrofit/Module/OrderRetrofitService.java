package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.OrderApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง OrderApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class OrderRetrofitService {

    private static OrderApiServiceInterface instance;

    private OrderRetrofitService() {}

    public static synchronized OrderApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(OrderApiServiceInterface.class);
        }
        return instance;
    }
}
