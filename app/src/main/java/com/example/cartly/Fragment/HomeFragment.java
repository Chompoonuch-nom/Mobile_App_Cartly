package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.cartly.ProductFormActivity;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.ResponseDao.PageResponseDao;
import com.example.cartly.ResponseDao.ProductDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * หน้าแรก — หมวดหมู่ (GET /categories), สินค้า + ค้นหา + แบ่งหน้า (GET /products),
 * และ admin ลบสินค้า (DELETE /products/{id})
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private static final int PAGE_SIZE = 20;
    private final CategoryApiService categoryApiService = new CategoryApiService();
    private final ProductApiService productApiService = new ProductApiService();
    private int currentPage = 0;
    private boolean isLastPage = false;
    private Long selectedCategoryId = null;  // null = ทั้งหมด
    private String currentKeyword = null;
    private ProductAdapter productAdapter;
    private CategoryChipAdapter categoryChipAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedCategoryId = null; // chip adapter เริ่มที่ "ทั้งหมด" ทุกครั้งที่สร้าง view ใหม่
        currentKeyword = null;

        // หมวดหมู่ : RecyclerView แนวนอน (icon + ชื่อ)
        binding.recyclerViewCategories.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryChipAdapter = new CategoryChipAdapter(categoryId -> {
            selectedCategoryId = categoryId;
            // backend ให้ keyword มาก่อน category_id จึงต้องล้างคำค้นหา ไม่งั้นกดหมวดหมู่แล้วไม่มีผล
            currentKeyword = null;
            binding.edtSearch.setText("");
            loadFirstPage();
        });
        binding.recyclerViewCategories.setAdapter(categoryChipAdapter);

        // สินค้า : Grid 2 คอลัมน์
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));
        productAdapter = new ProductAdapter(new ProductAdapter.Listener() {
            @Override
            public void onProductClick(ProductDao product) {
                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                intent.putExtra("product_id", product.getProduct_id());
                startActivity(intent);
            }

            @Override
            public void onEditClick(ProductDao product) {
                Intent intent = new Intent(requireContext(), ProductFormActivity.class);
                intent.putExtra("product_id", product.getProduct_id());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(ProductDao product) {
                deleteProduct(product);
            }
        }, TokenManager.isAdmin());
        binding.productRecyclerView.setAdapter(productAdapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadFirstPage);
        binding.btnLoadMore.setOnClickListener(view1 -> loadNextPage());
        binding.mvSearch.setOnClickListener(view1 -> {
            currentKeyword = binding.edtSearch.getText().toString().trim();
            if (currentKeyword.isEmpty()) currentKeyword = null;
            loadFirstPage();
        });

        loadCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        // เผื่อกลับมาจากหน้าแก้ไข/เพิ่มสินค้า (ไม่โหลดตอนถูกซ่อนอยู่หลัง tab อื่น)
        if (!isHidden()) loadFirstPage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // กัน memory leak และให้ callback ที่มาช้าเช็ค binding == null ได้
    }

    // --------- Categories
    private void loadCategories() {
        categoryApiService.getAll(new ApiCallback<List<CategoryDao>>() {
            @Override
            public void onSuccess(List<CategoryDao> data, String message) {
                if (binding == null) return;
                categoryChipAdapter.setItems(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (binding == null) return;
                toast("โหลดหมวดหมู่ไม่สำเร็จ:" + message);
            }
        });
    }

    private void loadFirstPage() {
        currentPage = 0;
        isLastPage = false;
        fetchProducts(false);
    }

    private void loadNextPage() {
        if (isLastPage) {
            toast("โหลดสินค้าครบทุกรายการแล้ว");
            return;
        }
        currentPage++;
        fetchProducts(true);
    }

    private void fetchProducts(boolean append) {
        if (binding == null) return;
        binding.swipeRefresh.setRefreshing(true);

        ApiCallback<PageResponseDao<ProductDao>> callback = new ApiCallback<PageResponseDao<ProductDao>>() {
            @Override
            public void onSuccess(PageResponseDao<ProductDao> data, String message) {
                if (binding == null) return;
                binding.swipeRefresh.setRefreshing(false);
                isLastPage = currentPage + 1 >= data.getTotal_pages();
                if (append) productAdapter.appendItems(data.getContent());
                else productAdapter.setItems(data.getContent());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (binding == null) return;
                binding.swipeRefresh.setRefreshing(false);
                toast(message);
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
        productApiService.delete(product.getProduct_id(), new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                if (binding == null) return;
                toast(message);
                loadFirstPage();
            }

            @Override
            public void onError(String message, int httpCode) {
                if (binding == null) return;
                toast(message);
            }
        });
    }

    private void toast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}