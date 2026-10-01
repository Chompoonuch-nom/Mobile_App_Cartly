package com.example.cartly.Retrofit;

import androidx.annotation.NonNull;
//import com.cartly.app.util.TokenManager;


import com.example.cartly.Util.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * แนบ header "Authorization: Bearer <token>" ให้ทุก request อัตโนมัติ
 * อ่าน token จาก TokenManager ทุกครั้งที่ยิง request จึงไม่ต้องสร้าง Retrofit ใหม่
 * หลัง login / logout
 */

public class AuthInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        String token = TokenManager.getToken();

        if (token == null || token.isEmpty()) {
            return chain.proceed(original);
        }

        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(request);
    }
}
