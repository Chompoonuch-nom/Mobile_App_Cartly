package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.CartApiServiceInterface;
import com.example.cartly.RequestDao.CartItemRequestDao;
import com.example.cartly.ResponseDao.CartDao;
import com.example.cartly.Retrofit.Module.CartRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

/** Module: Cart - ตระกร้าสินค้า (ต้อง login)*/
public class CartApiService {
    private final CartApiServiceInterface api = CartRetrofitService.getInstance();

    public void getMyCart(ApiCallback<CartDao> callback) {
        ApiCallHandler.enqueue(api.getMyCart(), callback);
    }

    public void addItem(long productId, int quantity, ApiCallback<CartDao> callback) {
        ApiCallHandler.enqueue(api.addItem(new CartItemRequestDao(productId, quantity)), callback);
    }

    /** quantity <= 0 = ลบรายการนั้นออกจากตระกร้า (ตาม logic ฝั่ง backend) */
    public void updateItemQuantity(long cartItemId, int quantity, ApiCallback<CartDao> callback) {
        ApiCallHandler.enqueue(api.updateItemQuantity(cartItemId, quantity),callback);
    }

    public void removeItem(long cartItemId, ApiCallback<CartDao> callback) {
        ApiCallHandler.enqueue(api.removeItem(cartItemId), callback);
    }

    public void clearCart(ApiCallback<Object> callback) {
        ApiCallHandler.enqueue(api.clearCart(), callback);
    }
}
