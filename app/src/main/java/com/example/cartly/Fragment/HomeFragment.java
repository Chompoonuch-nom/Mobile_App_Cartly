package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.cartly.Util.ProductListState;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.FragmentHomeBinding;

import java.util.List;

/**
 * หน้าแรก — หมวดหมู่ (GET /categories), สินค้า + ค้นหา + แบ่งหน้า (GET /products),
 * และ admin ลบสินค้า (DELETE /products/{id})
 * - เลื่อนลงใกล้สุดรายการ -> โหลดหน้าถัดไปอัตโนมัติ (infinite scroll)
 * - ช่องค้นหาและแถบหมวดหมู่อยู่นอก RecyclerView สินค้า จึงอยู่กับที่ ไม่หุบเวลาเงื่อน
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private static final int PAGE_SIZE = 20;
    /** เหลืออีกกี่ item ก่อนสุดรายการแล้วเริ่มโหลดหน้าถัดไป (Grid 2 คอลัมน์ = ประมาณ 2 แถว) */
    private static final int LOAD_MORE_THRESHOLD = 4;
    private final CategoryApiService categoryApiService = new CategoryApiService();
    private final ProductApiService productApiService = new ProductApiService();
    private int currentPage = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    /** เลขของ request ล่าสุด - callback ของ request เก่า (เช่น สลับหมวดหมู่เร็วๆ) จะถูกทิ้ง */
    private int requestSeq = 0;
    private Long selectedCategoryId = null;  // null = ทั้งหมด
    private String currentKeyword = null;
    private ProductAdapter productAdapter;
    private CategoryChipAdapter categoryChipAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        isLoading = false;

        // หมวดหมู่ : RecyclerView แนวนอน (icon + ชื่อ)
        binding.recyclerCategories.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryChipAdapter = new CategoryChipAdapter(categoryId -> {
            selectedCategoryId = categoryId;
            // backend ให้ keyword มาก่อน category_id จึงต้องล้างคำค้นหา ไม่งั้นกดหมวดหมู่แล้วไม่มีผล
            currentKeyword = null;
            binding.edtSearch.setText("");
            loadFirstPage();
        });
        binding.recyclerCategories.setAdapter(categoryChipAdapter);

        // สินค้า : Grid 2 คอลัมน์ + infinite scroll
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),2);
        binding.recyclerProducts.setLayoutManager(gridLayoutManager);
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
        binding.recyclerProducts.setAdapter(productAdapter);

        binding.recyclerProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) return; // สนใจเฉพาะตอนเลื่อนลง
                int lastVisible = gridLayoutManager.findLastVisibleItemPosition();
                if (lastVisible >= gridLayoutManager.getItemCount() - LOAD_MORE_THRESHOLD) {
                    loadNextPage();
                }
            }
        });

        binding.swipeRefresh.setOnRefreshListener(this::loadFirstPage);
        binding.btnSearch.setOnClickListener(view1 -> {
            currentKeyword = binding.edtSearch.getText().toString().trim();
            if (currentKeyword.isEmpty()) currentKeyword = null;
            loadFirstPage();
        });

        ProductListState.consumeStale(); // กำลังจะโหลดใหม่อยู่แล้ว
        loadCategories();
        loadFirstPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        // กลับมาจากหน้าอื่น: โหลดใหม่เฉพาะเมื่อข้อมูลสินค้าเปลี่ยน (ไม่งั้นรักษาตำแหน่งที่เลื่อนไว้)
        if (!isHidden() && ProductListState.consumeStale()) loadFirstPage();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && ProductListState.consumeStale()) loadFirstPage();
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
        if (isLoading || isLastPage) return; // กันยิงซ้ำระหว่างกำลังโหลด / ครบทุกหน้าแล้ว
        currentPage++;
        fetchProducts(true);
    }

    private void fetchProducts(boolean append) {
        if (binding == null) return;
        final int seq = ++requestSeq;
        isLoading = true;
        if (append) binding.progressMore.setVisibility(View.VISIBLE);
        else binding.swipeRefresh.setRefreshing(true);

        ApiCallback<PageResponseDao<ProductDao>> callback = new ApiCallback<PageResponseDao<ProductDao>>() {
            @Override
            public void onSuccess(PageResponseDao<ProductDao> data, String message) {
                if (binding == null || seq != requestSeq) return;
                finishLoading();
                isLastPage = currentPage + 1 >= data.getTotal_pages();
                if (append) productAdapter.appendItems(data.getContent());
                else productAdapter.setItems(data.getContent());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (binding == null || seq != requestSeq) return;
                finishLoading();
                if (append) currentPage--; // ให้เลื่อนลงแล้วลองโหลดหน้าเดิมซ้ำได้
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

    private void finishLoading() {
        isLoading = false;
        binding.swipeRefresh.setRefreshing(false);
        binding.progressMore.setVisibility(View.GONE);
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