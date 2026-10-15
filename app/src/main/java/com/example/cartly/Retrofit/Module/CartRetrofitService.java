package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.CartApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง CartApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class CartRetrofitService {
    private static CartApiServiceInterface instance;
    private CartRetrofitService() {}

    public static synchronized CartApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(CartApiServiceInterface.class);
        }
        return instance;
    }
}
