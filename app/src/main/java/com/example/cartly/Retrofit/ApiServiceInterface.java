package com.example.cartly.Retrofit;

import com.example.cartly.RequestDao.LoginRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.LoginResponseDao;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServiceInterface {
//    ---Auth
    @POST("api/auth/register")
    Observable<ApiResponseDao<LoginResponseDao>> authLogin(@Body LoginRequestDao request);
}
