package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.LoginRequestDao;
import com.example.cartly.RequestDao.RegisterRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.AuthResponseDao;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiServiceInterface {
    @POST("api/auth/register")
    Call<ApiResponseDao<AuthResponseDao>> register(@Body RegisterRequestDao request);

    @POST("api/auth/login")
    Call<ApiResponseDao<AuthResponseDao>> login(@Body LoginRequestDao request);
}
