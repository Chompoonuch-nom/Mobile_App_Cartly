package com.example.cartly.ResponseDao;

import java.math.BigDecimal;

/**
 * ตรงกับ OrderItemOut (schemas.py) - snapshot ของสินค้า ณ ตอนสั่งซื้อ
 * (product_name, unite_price ถูกก็อปปี้มาเก็บไว้ ไม่ใช่ราคาสินค้าปัจจุบัน)
 */

public class OrderItemDao {
    private long order_item_id;
    private long product_id;
    private String product_name;
    private BigDecimal unit_price;
    private int quantity;
    private BigDecimal subtotal;

    public OrderItemDao(long order_item_id, long product_id, String product_name, BigDecimal unit_price, int quantity, BigDecimal subtotal) {
        this.order_item_id = order_item_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public long getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(long order_item_id) {
        this.order_item_id = order_item_id;
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

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
