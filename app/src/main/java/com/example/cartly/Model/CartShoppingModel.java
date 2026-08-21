package com.example.cartly.Model;

import com.example.cartly.Dao.CartDao;
import com.example.cartly.Dao.ItemProductDao;
import com.example.cartly.Dao.ProductDao;
import com.example.cartly.Dao.StoreDao;
import com.example.cartly.R;

import java.util.ArrayList;

public class CartShoppingModel {
    public static CartDao getCartShopping() {
        ArrayList<ItemProductDao> productList = new ArrayList<>();
        productList.add(new ItemProductDao("0001","Product Name 1","Product Detail 1", R.drawable.qr_code_add_24,1, 250));
        productList.add(new ItemProductDao("0002","Product Name 2", "Product Detail 2", R.drawable.qr_code_add_24, 1,250));

        ArrayList<StoreDao> storeList = new ArrayList<>();
        storeList.add(new StoreDao("Store 01", "Store Name 1", 2, productList));
        storeList.add(new StoreDao("Store 02", "Store Name 2", 2, productList));

        return new CartDao(1, storeList.size(), 500,storeList);
    }
}
