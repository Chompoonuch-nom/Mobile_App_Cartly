package com.example.cartly.RequestDao;

/** ตรงกับ ProductImageCreate (schemas.py) — ใช้เป็นส่วนหนึ่งของ ProductRequest */
public class ProductImageRequestDao {
    private String image_url;
    private boolean is_primary;

    public ProductImageRequestDao(String imageUrl, boolean isPrimary) {
        this.image_url = image_url;
        this.is_primary = isPrimary;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
    }

    public boolean isPrimary() {
        return is_primary;
    }

    public void setPrimary(boolean isPrimary) {
        this.is_primary = isPrimary;
    }
}
