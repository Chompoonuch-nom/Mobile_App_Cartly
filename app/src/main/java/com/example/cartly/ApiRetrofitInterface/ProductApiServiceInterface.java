package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.ProductRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.PageResponseDao;
import com.example.cartly.ResponseDao.ProductDao;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApiServiceInterface {
    /** category_id / keyword ส่ง null ได้ (Retrofit จะไม่แนบ query นั้นไปให้)*/
    @GET("api/products")
    Call<ApiResponseDao<PageResponseDao<ProductDao>>> getProducts(
            @Query("page") int page,
            @Query("size") int size,
            @Query("category_id") Long categoryId,
            @Query("keyword") String keyword);

    @GET("api/products,{id}")
    Call<ApiResponseDao<ProductDao>> getById(@Path("id") long productId);

    // ---ADMIN
    @POST("api/products")
    Call<ApiResponseDao<ProductDao>> create(@Body ProductRequestDao request);

    @PUT("api/products/{id}")
    Call<ApiResponseDao<ProductDao>> update(@Path("id") long productId, @Body ProductRequestDao request);

    @DELETE("api/products/{id}")
    Call<ApiResponseDao<Object>> delete(@Path("id") long productId);

    @POST("api/products/{id}/images")
    Call<ApiResponseDao<ProductDao>> addImage(
            @Path("id") long productId,
            @Query("image_url") String imageUrl,
            @Query("is_primary") boolean isPrimary);

    @DELETE("api/products/{id}/images/{imageId}")
    Call<ApiResponseDao<Object>> deleteImage(
            @Path("id") long productId,
            @Path("imageId") long productImgId);
}
