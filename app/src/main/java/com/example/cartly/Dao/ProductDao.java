package com.example.cartly.Dao;

public class ProductDao {
    public int image;
    public String product_name;
    public String product_detail;
    public String product_price;
    public Boolean product_like;

    public ProductDao(int image, String product_name, String product_detail, String product_price, Boolean product_like) {
        this.image = image;
        this.product_name = product_name;
        this.product_detail = product_detail;
        this.product_price = product_price;
        this.product_like = product_like;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public Boolean getProduct_like() {
        return product_like;
    }

    public void setProduct_like(Boolean product_like) {
        this.product_like = product_like;
    }
}
