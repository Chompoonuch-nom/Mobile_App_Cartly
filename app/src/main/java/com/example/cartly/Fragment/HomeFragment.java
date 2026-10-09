package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cartly.Adapter.CategoryChipAdapter;
import com.example.cartly.Adapter.ProductAdapter;
import com.example.cartly.ApiService.CategoryApiService;
import com.example.cartly.ApiService.ProductApiService;
import com.example.cartly.ProductDetailActivity;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.ResponseDao.PageResponseDao;
import com.example.cartly.ResponseDao.ProductDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * หน้าแรก - แสดงสินค้า, filter ตามหมวดหมู่ (Category.getAll), ค้นหา (Product.getProducts?keyword=)
 * และแบ่งหน้าด้วยปุ่ม "โหลดเพิ่มเติม"
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final int PAGE_SIZE = 20;
    private final CategoryApiService categoryApiService = new CategoryApiService();
    private final ProductApiService productApiService = new ProductApiService();
    private final List<CategoryDao> categories = new ArrayList<>();
    private int currentPage = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private Long selectedCategoryId = null;  // null = ทั้งหมด
    private String currentKeyword = null;
    private ProductAdapter productAdapter;
    private CategoryChipAdapter categoryChipAdapter;

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
        setHasOptionsMenu(true);
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
        setCategories();
        setProducts();
        component();
        loadCategories();
        loadFirstPage();
    }

    private void component() {
        fragmentHomeBinding.mvBtnCart.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CartFragment.class);
            startActivity(intent);
        });
    }

    // หมวดหมู่ : RecyclerView แนวนอน (icon + ชื่อ)
    private void setCategories() {
        fragmentHomeBinding.recyclerViewCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
         categoryChipAdapter = new CategoryChipAdapter(categoryId -> {
            selectedCategoryId = categoryId;
            // backend ให้ keyword มาก่อน category_id จึงต้องล้างคำค้นหา ไม่งั้นกดหมวดหมู่แล้วไม่มีผล
            currentKeyword = null;
            fragmentHomeBinding.edtSearch.setText("");
            loadFirstPage();
        });
        fragmentHomeBinding.recyclerViewCategories.setAdapter(categoryChipAdapter);
    }

    private void setProducts() {
        fragmentHomeBinding.productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(new ProductAdapter.Listener() {
            @Override
            public void onProductClick(ProductDao product) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product_id", product.getProduct_id());
                startActivity(intent);
            }

            @Override
            public void onEditClick(ProductDao product) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product_id", product.getProduct_id());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(ProductDao product) {
                deleteProduct(product);
            }
        }, TokenManager.isAdmin());
        fragmentHomeBinding.productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadNextPage();
            }
        });
        fragmentHomeBinding.productRecyclerView.setAdapter(productAdapter);
        fragmentHomeBinding.swipeRefresh.setOnRefreshListener(this::loadFirstPage);

//        fragmentHomeBinding.btnLoadMore.setOnClickListener(view -> loadNextPage());
        fragmentHomeBinding.mvSearch.setOnClickListener(view -> {
            currentKeyword = fragmentHomeBinding.edtSearch.getText().toString().trim();
            if (currentKeyword.isEmpty()) currentKeyword = null;
            loadFirstPage();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFirstPage(); // เมื่อกลับจากหน้าแก้ไข / เพิ่มสินค้า
    }

//    Load Categories
    private void loadCategories() {
        // GET API categoryApiService.getAll
        categoryApiService.getAll(new ApiCallback<List<CategoryDao>>() {
            @Override
            public void onSuccess(List<CategoryDao> data, String message) {
                if (getActivity().isFinishing() || getActivity().isDestroyed()) return;
                categoryChipAdapter.setItems(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                Toast.makeText(getContext(), "โหลดหมวดหมู่ไม่สำเร็จ: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Error: ", message);
            }
        });
    }

//    Load Products
    private void loadFirstPage() {
        currentPage = 0;
        isLastPage = false;
        fetchProduct(false);
    }

    private void loadNextPage() {
        if (isLastPage) {
//            Toast.makeText(getContext(), "โหลดสินค้าครบทุกรายการแล้ว", Toast.LENGTH_SHORT).show();
            return;
        }
        currentPage++;
        fetchProduct(true);
    }

    private void fetchProduct(boolean append) {
        fragmentHomeBinding.swipeRefresh.setRefreshing(true);
        //GET API Product
        ApiCallback<PageResponseDao<ProductDao>> callback = new ApiCallback<PageResponseDao<ProductDao>>() {
            @Override
            public void onSuccess(PageResponseDao<ProductDao> data, String message) {
                if (getActivity().isFinishing() || getActivity().isDestroyed()) return;
                fragmentHomeBinding.swipeRefresh.setRefreshing(false);
                isLastPage = currentPage + 1 >= data.getTotal_pages();
                if (append) productAdapter.appendItems(data.getContent());
                else productAdapter.setItems(data.getContent());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (getActivity().isFinishing() || requireActivity().isDestroyed()) return;
                fragmentHomeBinding.swipeRefresh.setRefreshing(false);
                currentPage--;
                Log.e("PRODUCT_API", message);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };

        if (currentKeyword != null) {
            productApiService.search(currentKeyword, currentPage, PAGE_SIZE, callback);
        } else if (selectedCategoryId != null) {
            productApiService.getByCategory(selectedCategoryId, currentPage, PAGE_SIZE, callback);
        } else {
            productApiService.getProducts(currentPage, PAGE_SIZE, callback);
        }
    }

    private void deleteProduct(ProductDao product) {
        // DELETE API ProductApiService
        productApiService.delete(product.getProduct_id(), new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                loadFirstPage();
            }

            @Override
            public void onError(String message, int httpCode) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}