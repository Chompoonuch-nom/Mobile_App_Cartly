package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.CategoryRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.CategoryDao;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CategoryApiServiceInterface {
    @GET("api/categories")
    Call<ApiResponseDao<List<CategoryDao>>> getAll();

    @GET("api/categories/{id}")
    Call<ApiResponseDao<CategoryDao>> getById(@Part("id") long categoryId);

    //----ADMIN
    @POST("api/categories")
    Call<ApiResponseDao<CategoryDao>> create(@Body CategoryRequestDao request);

    @PUT("api/categories/{id}")
    Call<ApiResponseDao<CategoryDao>> update(@Part("id") long categoryId, @Body CategoryRequestDao request);

    @DELETE("api/categories/{id}")
    Call<ApiResponseDao<Object>> delete(@Path("id") long categoryId);
}
