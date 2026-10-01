package com.example.cartly.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * จุดเดียวที่สร้างและถือ Retrofit instance ของทั้งแอป (Singleton)
 * ใช้งาน:
 *   Retrofit retrofit = RetrofitService.getRetrofitInstance();
 *   MyApi api = retrofit.create(MyApi.class);
 * (โดยปกติไม่ต้องเรียกตรง ๆ ใน Activity — ให้เรียกผ่าน XxxRetrofitService / XxxApiService)
 */

public class RetrofitService {
    private static Retrofit retrofit;
    private RetrofitService() {}
    public static synchronized Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConfig.BASE_URL)
                    .client(buildOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor());

        if (ApiConfig.ENABLE_HTTP_LOGGING) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }
}
