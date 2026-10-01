package com.example.cartly.RequestDao;

/** ตรงกับ CartItemCreate (schemas.py) — ใช้กับ POST /api/cart/items */
public class CartItemRequestDao {
    private long product_id;
    private int quantity;

    public CartItemRequestDao(long product_id, int quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
