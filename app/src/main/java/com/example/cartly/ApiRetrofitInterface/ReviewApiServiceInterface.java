package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.ReviewRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.ReviewDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewApiServiceInterface {
    @GET("api/products/{productId}/reviews")
    Call<ApiResponseDao<List<ReviewDao>>> getByProduct(@Path("productId") long productId);

    @POST("api/products/{productId}/reviews")
    Call<ApiResponseDao<ReviewDao>> create(@Path("productId") long productId, @Body ReviewRequestDao request);

    @DELETE("api/reviews/{reviewId}")
    Call<ApiResponseDao<Object>> delete(@Path("reviewId") long reviewId);
}
