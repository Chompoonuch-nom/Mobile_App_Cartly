package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.AddressRequestDao;
import com.example.cartly.ResponseDao.AddressDao;
import com.example.cartly.ResponseDao.ApiResponseDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApiServiceInterface {
    @GET("api/address")
    Call<ApiResponseDao<List<AddressDao>>> getMyAddresses();

    @POST("api/address")
    Call<ApiResponseDao<AddressDao>> create(@Body AddressRequestDao request);

    @PUT("api/address/{id}")
    Call<ApiResponseDao<AddressDao>> update(@Path("id") long addressId, @Body AddressRequestDao request);

    @DELETE("api/address/{id}")
    Call<ApiResponseDao<Object>> delete(@Path("id") long addressId);
}
