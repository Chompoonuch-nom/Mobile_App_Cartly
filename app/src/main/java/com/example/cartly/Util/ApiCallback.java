package com.example.cartly.Util;

/**
 * Callback มาตรฐานที่ Activity/Fragment ได้รับผลลัพธ์จาก ApiService ทุกตัว
 * @param <T> ชนิดของ "data" ที่อยู่ใน ApiResponse
 */
public interface ApiCallback<T> {
//    เรียกเมื่อ Http สำเร็จ และ success = true
    void onSuccess(T data, String message);

    /**
     * เรียกเมื่อเกืดข้อผิดพลาดทุกกรณี
     * @param message ข้อความพร้อมแสดงผู้ใช้
     * @param httpCode HTTP status code (401, 404, 422, ...) หรือ ApiCallHandler.NETWORK_ERROR (-1)
     *                 ถ้าเชื่อม server ไม่ได้
     */

    void onError(String message, int httpCode);
}
