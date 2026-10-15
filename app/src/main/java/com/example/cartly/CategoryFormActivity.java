package com.example.cartly;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartly.ApiService.CategoryApiService;
import com.example.cartly.RequestDao.CategoryRequestDao;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivityCategoryFormBinding;

/**
 * Admin: สร้าง/แก้ไขหมวดหมู่ (POST, GET by id, PUT api/categories)
 */
public class CategoryFormActivity extends AppCompatActivity {

    private ActivityCategoryFormBinding binding;
    private final CategoryApiService categoryApiService = new CategoryApiService();

    private long categoryId = -1;
    private boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call Splashscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCategoryFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoryId = getIntent().getLongExtra("category_id", -1);
        isEditMode = categoryId != -1;

        initComponent();
    }

    private void initComponent() {
        binding.tvFormTitle.setText(isEditMode ? "แก้ไขหมวดหมู่" : "เพิ่มหมวดหมู่ใหม่");
        binding.btnSave.setOnClickListener(view -> save());
        
        if (isEditMode) loadCategory();
    }

    private void loadCategory() {
        categoryApiService.getById(categoryId, new ApiCallback<CategoryDao>() {
            @Override
            public void onSuccess(CategoryDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                binding.edtCategoryName.setText(data.getCategory_name());
                binding.edtCategoryDes.setText(data.getCategory_descp());
                binding.edtIconUrl.setText(data.getIcon_url());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(CategoryFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void save() {
        String name = binding.edtCategoryName.getText().toString().trim();
        String des = binding.edtCategoryDes.getText().toString().trim();
        String icon = binding.edtIconUrl.getText().toString().trim();
        
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "กรอกชื่อหมวดหมู่", Toast.LENGTH_SHORT).show();
            return;
        }

        CategoryRequestDao request = new CategoryRequestDao(
                name, des.isEmpty() ? null : des, icon.isEmpty() ? null : icon);

        setLoading(true);
        ApiCallback<CategoryDao> callback = new ApiCallback<CategoryDao>() {
            @Override
            public void onSuccess(CategoryDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(CategoryFormActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(CategoryFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };

        if (isEditMode) categoryApiService.update(categoryId, request, callback);
        else categoryApiService.create(request, callback);
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSave.setEnabled(!loading);
    }
}