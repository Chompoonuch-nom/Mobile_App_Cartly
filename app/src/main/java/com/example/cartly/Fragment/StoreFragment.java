package com.example.cartly.Fragment;

import android.os.Bundle;

import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cartly.R;
import com.example.cartly.databinding.FragmentStoreBinding;

import java.util.Objects;

public class StoreFragment extends Fragment {

    private FragmentStoreBinding fragmentStoreBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StoreFragment() {
        // Required empty public constructor
    }
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentStoreBinding = FragmentStoreBinding.inflate(getLayoutInflater());
        View view = fragmentStoreBinding.getRoot();

        initComponent();
        return view;
    }

    private void initComponent() {

    }
}