package com.example.cartly.Dao;

public class ItemProductDao {
    public String product_id;
    public String product_name;
    public String product_detail;
    public int image;
    public int count;
    public double total;

    public ItemProductDao(String product_id, String product_name, String product_detail, int image, int count, double total) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_detail = product_detail;
        this.image = image;
        this.count = count;
        this.total = total;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
