package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.OrderApiServiceInterface;
import com.example.cartly.RequestDao.CheckoutRequestDao;
import com.example.cartly.ResponseDao.OrderDao;
import com.example.cartly.Retrofit.Module.OrderRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

import java.util.List;

/** Module: Order - สั่งซื้อ / ประวัติคำสั่งซื้อ / ยกเลิก (updateStatus ใช้ได้เฉพาะ ADMIN) */
public class OrderApiService {
    private final OrderApiServiceInterface api = OrderRetrofitService.getInstance();

    /** paymentMethod: "COD", "CREDIT_CARD", "PROMPTPAY", "TRUEMONEY" */
    public void checkout(long addressId, String paymentMethod, ApiCallback<OrderDao> callback) {
        ApiCallHandler.enqueue(api.checkout(new CheckoutRequestDao(addressId, paymentMethod)), callback);
    }

    public void getMyOrders(ApiCallback<List<OrderDao>> callback) {
        ApiCallHandler.enqueue(api.getMyOrders(), callback);
    }

    public void getById(long orderId, ApiCallback<OrderDao> callback) {
        ApiCallHandler.enqueue(api.getById(orderId), callback);
    }

    public void cancel(long orderId, ApiCallback<OrderDao> callback) {
        ApiCallHandler.enqueue(api.cancel(orderId), callback);
    }

    /** status: PENDING, PAID, SHIPPED, DELIVERED, CANCELLED */
    public void updateStatus(long orderId, String status, ApiCallback<OrderDao> callback) {
        ApiCallHandler.enqueue(api.updateStatus(orderId, status), callback);
    }
}
