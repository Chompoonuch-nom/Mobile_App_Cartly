package com.example.cartly.ResponseDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * ตรงกับ OrderOut (schemas.py)
 * ใช้กับ POST /api/orders/checkout, GET /api/orders, GET /api/orders/{id}
 */

public class OrderDao {
    private long order_id;
    private String order_status;  // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
    private BigDecimal total_amount;
    private String created_at;   // ISO-8601 string
    private List<OrderItemDao> items;

    public OrderDao(long order_id, String order_status, BigDecimal total_amount, String created_at, List<OrderItemDao> items) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.total_amount = total_amount;
        this.created_at = created_at;
        this.items = items;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<OrderItemDao> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDao> items) {
        this.items = items;
    }
}
