package com.example.cartly.Util;

import androidx.annotation.Nullable;

import com.example.cartly.Retrofit.ApiConfig;

/**
 * แปลง URL รูปจาก backend ให้โหลดได้
 * - รูปที่อัปโหลดเอง backend เก็บเป็น Path สัมพันธ์ เช่น "/uploads/ab12.jpg" -> ต่อกับ BASE_URL
 *   (เปลี่ยนเครื่อง/IP server แล้วรูปเก่ายังโหลดได้ เพราะไม่ได้ฝัง host ไว้ในฐานข้อมูล)
 * - URL เต็ม (http/https) ที่กรอกเอง -> ใช้ตามเดิม
 * ทุกที่ที่ใช้ Glide โหลดรุปจาก API ให้ผ่าน Method นี้
 */
public class ImageUrlResolver {

    private ImageUrlResolver() {}

    /** @return URL เต็มสำหรับ Glide หรือ null ถ้าไม่มีรุป */
    @Nullable
    public static String resolve(@Nullable String url) {
        if (url == null) return null;
        String u = url.trim();
        if (u.isEmpty()) return null;
        if (u.startsWith("http://") || u.startsWith("https://")) return u;
        return ApiConfig.BASE_URL + (u.startsWith("/") ? u.substring(1) : u); // BASE_URL ลงท้ายด้วย "/" เสมอ
    }
}
