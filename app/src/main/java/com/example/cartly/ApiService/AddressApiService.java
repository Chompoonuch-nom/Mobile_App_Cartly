package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.AddressApiServiceInterface;
import com.example.cartly.RequestDao.AddressRequestDao;
import com.example.cartly.ResponseDao.AddressDao;
import com.example.cartly.Retrofit.Module.AddressRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

import java.util.List;

/** Module: Address - ที่อยู่จัดส่งของผู้ใช้ (ต้อง login) */
public class AddressApiService {
    private final AddressApiServiceInterface api = AddressRetrofitService.getInstance();

    public void getMyAddresses(ApiCallback<List<AddressDao>> callback) {
        ApiCallHandler.enqueue(api.getMyAddresses(), callback);
    }

    public void create(AddressRequestDao request, ApiCallback<AddressDao> callback) {
        ApiCallHandler.enqueue(api.create(request),callback);
    }

    public void update(long addressId, AddressRequestDao request, ApiCallback<AddressDao> callback) {
        ApiCallHandler.enqueue(api.update(addressId,request), callback);
    }

    public void delete(long addressId, ApiCallback<Object> callback) {
        ApiCallHandler.enqueue(api.delete(addressId), callback);
    }
}
