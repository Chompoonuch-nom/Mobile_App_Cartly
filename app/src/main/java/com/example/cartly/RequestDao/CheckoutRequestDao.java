package com.example.cartly.RequestDao;

/** ตรงกับ CheckoutRequest (schemas.py) — ใช้กับ POST /api/orders/checkout */
public class CheckoutRequestDao {
    private long address_id;
    private String payment_method; // "COD", "CREDIT_CARD", "PROMPTPAY", "TRUEMONEY"

    public CheckoutRequestDao(long address_id, String payment_method) {
        this.address_id = address_id;
        this.payment_method = payment_method;
    }

    public long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(long address_id) {
        this.address_id = address_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
