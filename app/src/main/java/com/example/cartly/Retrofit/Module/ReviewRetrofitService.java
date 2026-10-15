package com.example.cartly.Retrofit.Module;

import com.example.cartly.ApiRetrofitInterface.ReviewApiServiceInterface;
import com.example.cartly.Retrofit.RetrofitService;

/**
 * สร้าง ReviewApiServiceInterface จาก RetrofitService.getRetrofitInstance()
 * (สร้างครั้งเดียวแล้วใช้ซ้ำ)
 */
public final class ReviewRetrofitService {
    private static ReviewApiServiceInterface instance;
    private ReviewRetrofitService() {}

    public static synchronized ReviewApiServiceInterface getInstance() {
        if (instance == null) {
            instance = RetrofitService.getRetrofitInstance().create(ReviewApiServiceInterface.class);
        }
        return instance;
    }
}
