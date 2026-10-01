package com.example.cartly.ResponseDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * ตรงกับ ProductOut (schemas.py)
 * ใช้กับ GET /api/products, GET /api/products/{id}
 * หมายเหตุ: category_id ใช้ Long (ตัวใหญ่ ไม่ใช่ long ตัวเล็ก) เพราะฝั่ง backend
 * เป็น Optional[int] -> อาจเป็น null ได้ (สินค้าที่ยังไม่ผูกหมวดหมู่)
 */

public class ProductDao {
    private long product_id;
    private String product_name;
    private String product_descp;    // อาจเป็น null
    private BigDecimal price;
    private int stock_qty;
    private boolean is_active;
    private Long category_id;      // nullable -> ต้องใช้ Long ไม่ใช่ long
    private List<ProductImageDao> images;

    public ProductDao(long product_id, String product_name, String product_descp, BigDecimal price, int stock_qty, boolean is_active, Long category_id, List<ProductImageDao> images) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_descp = product_descp;
        this.price = price;
        this.stock_qty = stock_qty;
        this.is_active = is_active;
        this.category_id = category_id;
        this.images = images;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
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

    public void setProduct_des(String product_descp) {
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

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public List<ProductImageDao> getImages() {
        return images;
    }

    public void setImages(List<ProductImageDao> images) {
        this.images = images;
    }

    public String getPrimaryImageURL() {
        if (images == null || images.isEmpty()) return null;
        for (ProductImageDao img : images) {
            if (img.isPrimary()) return img.getImage_url();
        }
        return images.get(0).getImage_url(); // ไม่มี primary ก็เอารูปแรกแทน
    }
}
