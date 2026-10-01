package com.example.cartly.RequestDao;

import java.math.BigDecimal;
import java.util.List;

/** ตรงกับ ProductCreate (schemas.py) — ใช้กับ POST/PUT /api/products (admin เท่านั้น) */
public class ProductRequestDao {
    private String product_name;
    private String product_descp;
    private BigDecimal price;
    private int stock_qty;
    private Long category_id;                  // nullable
    private List<ProductImageRequestDao> images;   // ส่ง list ว่างได้ถ้ายังไม่มีรูป

    public ProductRequestDao(String product_name, String product_descp, BigDecimal price, int stock_qty, Long category_id, List<ProductImageRequestDao> images) {
        this.product_name = product_name;
        this.product_descp = product_descp;
        this.price = price;
        this.stock_qty = stock_qty;
        this.category_id = category_id;
        this.images = images;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_descp() {
        return product_descp;
    }

    public void setProduct_descp(String product_descp) {
        this.product_descp = product_descp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock_qty() {
        return stock_qty;
    }

    public void setStock_qty(int stock_qty) {
        this.stock_qty = stock_qty;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public List<ProductImageRequestDao> getImages() {
        return images;
    }

    public void setImages(List<ProductImageRequestDao> images) {
        this.images = images;
    }
}
