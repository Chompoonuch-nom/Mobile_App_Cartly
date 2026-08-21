package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cartly.Adapter.ProductAdapter;
import com.example.cartly.Dao.ProductDao;
import com.example.cartly.Interface.OnClickProductSelectInterface;
import com.example.cartly.Model.ProductModel;
import com.example.cartly.ProductDetailActivity;
import com.example.cartly.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = fragmentHomeBinding.getRoot();

        initComponent();
        return view;
    }

    private void initComponent() {
        setType();
        setProducts();
        component();
    }

    private void component() {
        fragmentHomeBinding.mvBtnCart.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CartFragment.class);
            startActivity(intent);
        });
    }

    private void setType() {
        ArrayList<ProductDao> hotProduct = ProductModel.getProductData();
        fragmentHomeBinding.recyclerViewHotProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        ProductAdapter productAdapter = new ProductAdapter(getContext(), hotProduct, new OnClickProductSelectInterface() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCreateClick(int position) {

            }

            @Override
            public void onEditClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
        fragmentHomeBinding.recyclerViewHotProduct.setAdapter(productAdapter);
    }
    private void setProducts() {
        ArrayList<ProductDao> products = ProductModel.getProductData();
        fragmentHomeBinding.productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductAdapter productAdapter = new ProductAdapter(getContext(),products, new OnClickProductSelectInterface() {

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCreateClick(int position) {

            }

            @Override
            public void onEditClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
        fragmentHomeBinding.productRecyclerView.setAdapter(productAdapter);
    }
}