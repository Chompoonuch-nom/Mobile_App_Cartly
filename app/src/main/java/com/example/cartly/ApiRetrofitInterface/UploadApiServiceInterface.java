package com.example.cartly.ApiRetrofitInterface;

import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.UploadResultDao;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApiServiceInterface {
    /** multipart.form-data: field ชื่อ "file" (ต้อง login) */
    @Multipart
    @POST("api/uploads/image")
    Call<ApiResponseDao<UploadResultDao>> uploadImage(@Part MultipartBody.Part file);
}
