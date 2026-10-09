package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.UserDao;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApiServiceInterface {

    @GET("api/users/me")
    Call<ApiResponseDao<UserDao>> getMyProfile();
}
