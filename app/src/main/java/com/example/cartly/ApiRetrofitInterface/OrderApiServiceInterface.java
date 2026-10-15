package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.CheckoutRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.OrderDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApiServiceInterface {

    @POST("api/orders/checkout")
    Call<ApiResponseDao<OrderDao>> checkout(@Body CheckoutRequestDao request);

    @GET("api/orders")
    Call<ApiResponseDao<List<OrderDao>>> getMyOrders();

    @GET("api/orders/{id}")
    Call<ApiResponseDao<OrderDao>> getById(@Path("id") long orderId);

    @PUT("api/orders/{id}/cancel")
    Call<ApiResponseDao<OrderDao>> cancel(@Path("id") long orderId);

    // ------ADMIN
    @PUT("api/orders/{id}/status")
    Call<ApiResponseDao<OrderDao>> updateStatus(@Path("id") long orderId, @Query("status") String status);
}
