package com.example.cartly.ApiService;

import com.example.cartly.ApiRetrofitInterface.ReviewApiServiceInterface;
import com.example.cartly.RequestDao.ReviewRequestDao;
import com.example.cartly.ResponseDao.ReviewDao;
import com.example.cartly.Retrofit.Module.ReviewRetrofitService;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;

import java.util.List;

/** Module: Review - รีวิวสินค้า (ดูได้โดยไม่ต้อง login, เขียน/ลบต้อง login) */
public class ReviewApiService {

    private final ReviewApiServiceInterface api = ReviewRetrofitService.getInstance();

    public void getByProduct(long productId, ApiCallback<List<ReviewDao>> callback) {
        ApiCallHandler.enqueue(api.getByProduct(productId), callback);
    }

    public void create(long productId, int rating, String comment, ApiCallback<ReviewDao> callback) {
        ApiCallHandler.enqueue(api.create(productId, new ReviewRequestDao(rating, comment)), callback);
    }

    public void delete(long reviewId, ApiCallback<Object> callback) {
        ApiCallHandler.enqueue(api.delete(reviewId), callback);
    }
}
