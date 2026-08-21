package com.example.cartly.Model;

import com.example.cartly.Dao.ProductDao;
import com.example.cartly.R;

import java.util.ArrayList;

public class ProductModel {
    public static ArrayList<ProductDao> getProductData() {
        ArrayList<ProductDao> productList = new ArrayList<>();
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        productList.add(new ProductDao(R.drawable.qr_code_add_24,"Product Name","Product Detail","$1.99",true));
        return productList;
    }
}
