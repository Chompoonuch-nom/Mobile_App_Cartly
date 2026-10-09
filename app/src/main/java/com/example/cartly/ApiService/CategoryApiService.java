package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.CategoryApiServiceInterface;
import com.example.cartly.RequestDao.CategoryRequestDao;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.Retrofit.Module.CategoryRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

import java.util.List;

/** Module: Category - หมวดหมู่สินค้า (create/update/delete ใช้ได้เฉพาะ ADMIN) */
public class CategoryApiService {
    private final CategoryApiServiceInterface api = CategoryRetrofitService.getInstance();
    public void getAll(ApiCallback<List<CategoryDao>> callback) {
        ApiCallHandler.enqueue(api.getAll(), callback);
    }
    public void getById(long categoryId, ApiCallback<CategoryDao> callback) {
        ApiCallHandler.enqueue(api.getById(categoryId), callback);
    }
    public void create(CategoryRequestDao request, ApiCallback<CategoryDao> callback) {
        ApiCallHandler.enqueue(api.create(request), callback);
    }
    public void update(long categoryId, CategoryRequestDao request, ApiCallback<CategoryDao> callback) {
        ApiCallHandler.enqueue(api.update(categoryId, request), callback);
    }
    public void delete(long categoryId, ApiCallback<Object> callback) {
        ApiCallHandler.enqueue(api.delete(categoryId), callback);
    }
}
