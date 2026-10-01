package com.example.cartly.ResponseDao;

/**
 * ตรงกับ ProductImageOut (schemas.py) - รูปสินค้า 1 รูป (สินค้า 1 ชิ้นมีได้หลายรูป)
 * อยู่ใน field "images" ของ Product
 */

public class ProductImageDao {
    private long product_img_id;
    private String image_url;
    private boolean is_primary;

    public ProductImageDao(long product_img_id, String image_url, boolean is_primary) {
        this.product_img_id = product_img_id;
        this.image_url = image_url;
        this.is_primary = is_primary;
    }

    public long getProduct_img_id() {
        return product_img_id;
    }

    public void setProduct_img_id(long product_img_id) {
        this.product_img_id = product_img_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isPrimary() {
        return is_primary;
    }

    public void setPrimary(boolean is_primary) {
        this.is_primary = is_primary;
    }
}
