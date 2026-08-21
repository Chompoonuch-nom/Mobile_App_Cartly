package com.example.cartly.Dao;

import java.util.ArrayList;

public class StoreDao {
    private String store_id;
    private String store_name;
    private int product_total;
    private ArrayList<ItemProductDao> products;

    public StoreDao(String store_id, String store_name, int product_total, ArrayList<ItemProductDao> products) {
        this.store_id = store_id;
        this.store_name = store_name;
        this.product_total = product_total;
        this.products = products;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getProduct_total() {
        return product_total;
    }

    public void setProduct_total(int product_total) {
        this.product_total = product_total;
    }

    public ArrayList<ItemProductDao> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ItemProductDao> products) {
        this.products = products;
    }
}
