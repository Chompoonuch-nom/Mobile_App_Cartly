package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.AddressApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง AddressApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class AddressRetrofitService {
    private static AddressApiServiceInterface instance;
    private AddressRetrofitService() {}

    public static synchronized AddressApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(AddressApiServiceInterface.class);
        }
        return instance;
    }
}
