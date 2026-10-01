package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.AuthApiServiceInterface;
import com.example.cartly.RequestDao.LoginRequestDao;
import com.example.cartly.RequestDao.RegisterRequestDao;
import com.example.cartly.ResponseDao.AuthResponseDao;
import com.example.cartly.Retrofit.Module.AuthRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;

/**
 * Module: Auth - สมัครสมาชิก / เข้าสู่ระบบ / ออกจากระบบ
 * บันทึก token ลง TokenManager ให้อัตโนมัติเมื่อ register/login สำเร็จ
 * (Activity ไม่ต้องจัดการ token เอง)
 */
public class AuthApiService {
    private final AuthApiServiceInterface api = AuthRetrofitService.getInstance();
    public void register(RegisterRequestDao request, ApiCallback<AuthResponseDao> callback) {
        ApiCallHandler.enqueue(api.register(request), withSessionSaving(callback));
    }
    public void login(LoginRequestDao request, ApiCallback<AuthResponseDao> callback) {
        ApiCallHandler.enqueue(api.login(request), withSessionSaving(callback));
    }
    public void logout() {
        TokenManager.clear();
    }
    public boolean isLoggedIn() {
        return TokenManager.isLoggedIn();
    }

    private ApiCallback<AuthResponseDao> withSessionSaving(final ApiCallback<AuthResponseDao> callback) {
        return new ApiCallback<AuthResponseDao>() {
            @Override
            public void onSuccess(AuthResponseDao data, String message) {
                TokenManager.saveSession(data.getToken(), data.getUser_id(), data.getUser_role());
                callback.onSuccess(data, message);
            }

            @Override
            public void onError(String message, int httpCode) {
                callback.onError(message, httpCode);
            }
        };
    }
}
