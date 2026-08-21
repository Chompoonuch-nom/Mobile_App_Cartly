package com.example.cartly.Dao;

import java.util.ArrayList;

public class CartDao {
    private int id;
    private int store_count;
    private int total;
    private ArrayList<StoreDao> stores;

    public CartDao(int id, int store_count, int total, ArrayList<StoreDao> stores) {
        this.id = id;
        this.store_count = store_count;
        this.total = total;
        this.stores = stores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<StoreDao> getStores() {
        return stores;
    }

    public void setStores(ArrayList<StoreDao> stores) {
        this.stores = stores;
    }
}
