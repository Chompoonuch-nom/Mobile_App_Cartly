package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cartly.Adapter.CartShoppingAdapter;
import com.example.cartly.Dao.CartDao;
import com.example.cartly.Dao.StoreDao;
import com.example.cartly.Model.CartShoppingModel;
import com.example.cartly.ProductDetailActivity;
import com.example.cartly.databinding.FragmentCartBinding;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private FragmentCartBinding fragmentCartBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCartBinding = FragmentCartBinding.inflate(getLayoutInflater());
        View view = fragmentCartBinding.getRoot();

        initComponent();
        return view;
    }
    private void initComponent() {
        setCartShopping();
        setComponent();
    }
    private void setCartShopping() {
        CartDao carts = CartShoppingModel.getCartShopping();
        ArrayList<StoreDao> stores = CartShoppingModel.getCartShopping().getStores();
        fragmentCartBinding.tvTotal.setText(String.valueOf(carts.getTotal()));

        fragmentCartBinding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        CartShoppingAdapter adapter = new CartShoppingAdapter(getContext(), stores);
        fragmentCartBinding.recyclerViewCart.setAdapter(adapter);

        fragmentCartBinding.btnCheckOutLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            startActivity(intent);
        });
    }

    private void setComponent() {

    }
}