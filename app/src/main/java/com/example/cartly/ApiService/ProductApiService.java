package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.ProductApiServiceInterface;
import com.example.cartly.RequestDao.ProductRequestDao;
import com.example.cartly.ResponseDao.PageResponseDao;
import com.example.cartly.ResponseDao.ProductDao;
import com.example.cartly.Retrofit.Module.ProductRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

/**
 * Module: Product - สินค้า + รูปสินค้า (create/update/delete ใช้ได้เฉพาะ ADMIN)
 */
public class ProductApiService {
    private final ProductApiServiceInterface api = ProductRetrofitService.getInstance();

    /** สินค้าทั้งหมด (แบ่งหน้า) */
    public void getProducts(int page, int size, ApiCallback<PageResponseDao<ProductDao>> callback) {
        ApiCallHandler.enqueue(api.getProducts(page, size, null, null), callback);
    }

    /** สินค้าตามหมวดหมู่ */
    public void getByCategory(long categoryId, int page, int size,
                              ApiCallback<PageResponseDao<ProductDao>> callback) {
        ApiCallHandler.enqueue(api.getProducts(page, size, categoryId, null), callback);
    }

    /** ค้นหาสินค้าจากชือ */
    public void search(String keyword, int page, int size,
                       ApiCallback<PageResponseDao<ProductDao>> callback) {
        ApiCallHandler.enqueue(api.getProducts(page, size, null, keyword), callback);
    }

    public void getById(long productId, ApiCallback<ProductDao> callback) {
        ApiCallHandler.enqueue(api.getById(productId), callback);
    }

    public void create(ProductRequestDao request, ApiCallback<ProductDao> callback) {
        ApiCallHandler.enqueue(api.create(request), callback);
    }

    public void update(long productId, ProductRequestDao request, ApiCallback<ProductDao> callback) {
        ApiCallHandler.enqueue(api.update(productId, request), callback);
    }

    public void delete(long productId, ApiCallback<Object> callback) {
        ApiCallHandler.enqueue(api.delete(productId), callback);
    }

    public void addImage(long productId, String imageUrl, boolean isPrimary,
                         ApiCallback<ProductDao> callback) {
        ApiCallHandler.enqueue(api.addImage(productId, imageUrl, isPrimary), callback);
    }

    public void deleteImage(long productId, long productImageId, ApiCallback<Object> callback) {
        ApiCallHandler.enqueue(api.deleteImage(productId, productImageId), callback);
    }
}
