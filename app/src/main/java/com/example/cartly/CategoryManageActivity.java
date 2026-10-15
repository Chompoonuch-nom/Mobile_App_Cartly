package com.example.cartly;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cartly.Adapter.CategoryAdapter;
import com.example.cartly.ApiService.CategoryApiService;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivityCategoryManageBinding;

import java.util.List;

/**
 * Admin: รายการหมวดหมู่ + ลบ (GET, DELETE /api/categories)
 */
public class CategoryManageActivity extends AppCompatActivity {
    private ActivityCategoryManageBinding binding;
    private final CategoryApiService categoryApiService = new CategoryApiService();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call Splashscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCategoryManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        initComponent();
    }

    private void initComponent() {
        binding.recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(new CategoryAdapter.Listener() {
            @Override
            public void onEditClick(CategoryDao category) {
                Intent intent = new Intent(CategoryManageActivity.this, CategoryFormActivity.class);
                intent.putExtra("category_id", category.getCategory_id());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(CategoryDao category) {
                deleteCategory(category);
            }
        });
        binding.recyclerCategories.setAdapter(categoryAdapter);
        binding.btnAddCategory.setOnClickListener(view ->
                startActivity(new Intent(this, CategoryFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    private void loadCategories() {
        categoryApiService.getAll(new ApiCallback<List<CategoryDao>>() {
            @Override
            public void onSuccess(List<CategoryDao> data, String message) {
                if (isFinishing() || isDestroyed()) return;
                categoryAdapter.setItems(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Log.e("Error Load Categories: ", message);
                Toast.makeText(CategoryManageActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategory(CategoryDao category) {
        categoryApiService.delete(category.getCategory_id(), new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                Toast.makeText(CategoryManageActivity.this, message, Toast.LENGTH_SHORT).show();
                loadCategories();
            }

            @Override
            public void onError(String message, int httpCode) {
                // ลบไม่ได้บ่อยครั้งเพราะยังมีสินค้าผูกอยู่ (FK constraint) -> แสดงข้อความจาก backend ตรง ๆ
                Toast.makeText(CategoryManageActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}