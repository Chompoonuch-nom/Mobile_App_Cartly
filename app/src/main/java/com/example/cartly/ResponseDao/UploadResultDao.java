package com.example.cartly.ResponseDao;
/** ผลลัพธ์ของ POST /api/uploads/images - url เป็น path สัมพันธ์ เช่น "/uploads/ab12.jpg" */
public class UploadResultDao {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
