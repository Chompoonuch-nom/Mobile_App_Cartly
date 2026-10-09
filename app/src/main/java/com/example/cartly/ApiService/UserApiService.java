package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.UserApiServiceInterface;
import com.example.cartly.ResponseDao.UserDao;
import com.example.cartly.Retrofit.Module.UserRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

/**
 * Module User - ข้อมูลโปรไฟล์ของผู้ใช้ที่ login อยู่
 */
public class UserApiService {
    private final UserApiServiceInterface api = UserRetrofitService.getInstance();
    public void getMyProfile(ApiCallback<UserDao> callback) {
        ApiCallHandler.enqueue(api.getMyProfile(), callback);
    }
}
