package com.example.cartly.Util;

import com.example.cartly.ResponseDao.ApiResponseDao;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ตัวกลางจัดการ Call.enqueue() แทนการเขียน onResponse/onFailure ซ้ำทุก API
 * - แกะ ApiResponse<T> ให้เหลือแค่ data
 * - แปลง error body ของ FastAPI ให้เป็นข้อความอ่านง่าย
 *      (รองรับทางรูปแบบ {"message": ...} และ {"detail": ...})
 */
public class ApiCallHandler {
//    ใช้เป็น httpCode เมื่อเชื่อมต่อ server ไม่ได้ (ไม่ที HTTP response)
    public static final int NETWORK_ERROR = -1;
    public static final int UNAUTHORIZE = 401;

    private ApiCallHandler() {}
    public static <T> void enqueue(Call<ApiResponseDao<T>>call, final ApiCallback<T> callback) {
        call.enqueue(new Callback<ApiResponseDao<T>>() {
            @Override
            public void onResponse(Call<ApiResponseDao<T>> call, Response<ApiResponseDao<T>> response) {
                if (response.isSuccessful()) {
                    ApiResponseDao<T> body = response.body();
                    if (body != null && body.isSuccess()) {
                        callback.onSuccess(body.getData(), body.getMessage());
                    } else {
                        String msg = body != null ? body.getMessage() : "ไม่พบข้อมูลตอบกลับจากเซิร์ฟเวอร์";
                        callback.onError(msg, response.code());
                    }
                } else {
                    callback.onError(parseErrorMessage(response), response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDao<T>> call, Throwable t) {
                callback.onError("เชื่อมต่อเซิร์เวอร์ไม่ได้: " + t.getMessage(), NETWORK_ERROR);
            }
        });
    }

    private static <T> String parseErrorMessage(Response<ApiResponseDao<T>> response) {
        String fallback = defaultMessage(response.code());
        try {
            if (response.errorBody() == null) return fallback;

            JsonElement root = JsonParser.parseString(response.errorBody().string());
            if (!root.isJsonObject()) return fallback;
            JsonObject obj = root.getAsJsonObject();

            // รูปแบบของ custom exception: {"success":false, "message":"...", "data":null}
            if (obj.has("message") && obj.get("message").isJsonPrimitive()) {
                return obj.get("message").getAsString();
            }

            // รูปแบบของ HTTPException (401/403): {"detail":"..."}
            if (obj.has("detail")) {
                JsonElement detail = obj.get("detail");
                if (detail.isJsonPrimitive()) {
                    return detail.getAsString();
                }
                if (detail.isJsonArray()) {
                    JsonArray arr = detail.getAsJsonArray();
                    if (arr.size() > 0 && arr.get(0).isJsonObject()
                            && arr.get(0).getAsJsonObject().has("msg")) {
                        return arr.get(0).getAsJsonObject().get("msg").getAsString();
                    }
                }
            }
        } catch (IOException | RuntimeException ignored) {
            // parse ไม่ได้ ใช้ข้อมความ default
        }
        return fallback;
    }

    private static String defaultMessage(int code) {
        switch (code) {
            case 400: return "ข้อมูลไม่ถูกต้อง";
            case 401: return "กรุณาเข้าสู่ระบบใหม่อีกครั้ง";
            case 403: return "คุณไม่มีสิทธิ์ใช้งานส่วนนี้";
            case 404: return "ไม่พบข้อมูลที่ต้องการ";
            case 422: return "ข้อมูลที่ส่งไปไม่ครบถ้วนหรือรูปแบบไม่ถูกต้อง";
            default: return code >= 500 ? "เซิร์ฟเวอร์ขัดข้อง กรุณาลองใหม่ภายหลัง"
                                        : "เกิดข้อผิดพลาด (" + code + ")";
        }
    }
}
