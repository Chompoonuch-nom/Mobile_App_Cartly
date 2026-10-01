package com.example.cartly.ResponseDao;

import java.math.BigDecimal;

/**
 * ตรงกับ CartItemOut (schemas.py) - 1 แถวในตระกร้าสินค้า
 * สั่งเกตว่ามี field "product" เป็น object เต็ม ไม่ใช่แค่ product_id เฉยๆ
 * (backend ส่งรายละเอียดสินค้าแนบมาด้วย ไม่ต้องเรียก API แยกไปดึงสินค้าอีกครั้ง)
 */
public class CartItemDao {
    private long cart_item_id;
    private ProductDao product;
    private int quantity;

    public CartItemDao(long cart_item_id, ProductDao product, int quantity) {
        this.cart_item_id = cart_item_id;
        this.product = product;
        this.quantity = quantity;
    }

    public long getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(long cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public ProductDao getProduct() {
        return product;
    }

    public void setProduct(ProductDao product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //    คำนวณราคารวมของรายการนี้ (ราคาสินค้า x จำนวน) สำหรับแสดงผลใน UI
    public BigDecimal getSubTotal() {
        if (product == null || product.getPrice() == null) return BigDecimal.ZERO;
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
