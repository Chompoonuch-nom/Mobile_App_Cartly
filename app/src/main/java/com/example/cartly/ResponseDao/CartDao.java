package com.example.cartly.ResponseDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * ตรงกับ CartOut (schema.py)
 * ใช้กับ Get /api/cart, POST /api/cart/items, PUT/DELETE /api/cart/items/{id}
 */
public class CartDao {
    private long cart_id;
    private List<CartItemDao> items;

    public CartDao(long cart_id, List<CartItemDao> items) {
        this.cart_id = cart_id;
        this.items = items;
    }

    public long getCart_id() {
        return cart_id;
    }

    public void setCart_id(long cart_id) {
        this.cart_id = cart_id;
    }

    public List<CartItemDao> getItems() {
        return items;
    }

    public void setItems(List<CartItemDao> items) {
        this.items = items;
    }

    /** ผลรวมราคาสินค้าทั้งตระกร้า สำหรับแสดงยอดรวมก่อน checkout */
    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        if (items == null) return total;
        for (CartItemDao item : items) {
            total = total.add(item.getSubTotal());
        }
        return total;
    }

    public int getTotalItemCount() {
        if (items == null) return 0;
        return items.size();
    }
}
