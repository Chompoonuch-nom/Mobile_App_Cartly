package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.RequestDao.CartItemRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.CartDao;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartApiServiceInterface {
    @GET("api/cart")
    Call<ApiResponseDao<CartDao>> getMyCart();

    @POST("api/cart/items")
    Call<ApiResponseDao<CartDao>> addItem(@Body CartItemRequestDao request);

    @POST("api/cart/items")
    Call<ApiResponseDao<CartDao>> updateItemQuantity(
            @Path("cartItemId") long cartItemId,
            @Query("quantity") int quantity);

    @DELETE("api/cart/items/{cartItemId}")
    Call<ApiResponseDao<CartDao>> removeItem(@Path("cartItemId") long cartItemId);

    @DELETE("api/cart")
    Call<ApiResponseDao<Object>> clearCart();
}
