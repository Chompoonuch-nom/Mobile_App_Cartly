package com.example.cartly.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ค่าคงที่ของการเชื่อม API - แก้ที่ไฟล์นี้ไฟล์เดียวเมื่อเปลี่ยน Server
 */

public class ApiConfig {
    /**
     * Emulator                 -> http://10.0.2.2:8000/
     * เครื่องจริง (Wi-fi เดียวกัน)  -> http://<IP เครื่องที่รัน FastAPI>:8000/
     * Production               -> https://api.yourdomain.com
     * ต้องลงท้ายด้วย "/" เสมอ (เป็นข้อกำหนดของ Retrofit)
     */

    private ApiConfig() {}
//    Base url for Android Emulator
    public static final String BASE_URL = "http://10.0.2.2:8000/";
    public static final long CONNECT_TIMEOUT_SECONDS = 30;
    public static final long READ_TIMEOUT_SECONDS = 30;
    public static final long WRITE_TIMEOUT_SECONDS = 30;

//    เปิด log request/response ใน Logcat (ปิดเป็น false ก่อน release)
    public static final boolean ENABLE_HTTP_LOGGING = true;
}
