package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.AvatarRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.UserDao;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserApiServiceInterface {

    @GET("api/users/me")
    Call<ApiResponseDao<UserDao>> getMyProfile();

    @PUT("api/users/me/avatar")
    Call<ApiResponseDao<UserDao>> updateAvatar(@Body AvatarRequestDao request);
}
