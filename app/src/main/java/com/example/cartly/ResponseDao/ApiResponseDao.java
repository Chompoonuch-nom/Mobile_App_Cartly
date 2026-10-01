package com.example.cartly.ResponseDao;

/**
 * Wrapper มาตรฐานที่ทุก endpoint ของ Cartly API ส่งกลับมา
 * รูปแบบ: { "success": true, "message": "...", "data": {...} }
 * ใช้กับ Retrofit เช่น:
 *   Call<ApiResponseDao<ProductDao>> getProduct(...);
 *   Call<ApiResponseDao<List<CategoryDao>>> getCategories();
 */

public class ApiResponseDao<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponseDao(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
