package com.example.cartly;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartly.ApiService.CategoryApiService;
import com.example.cartly.ApiService.ProductApiService;
import com.example.cartly.RequestDao.ProductRequestDao;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.ResponseDao.ProductDao;
import com.example.cartly.ResponseDao.ProductImageDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivityProductFormBinding;
import com.example.cartly.databinding.RowProductImageBinding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ADMIN: สร้าง/แก้ไขสินค้า (POST, PUT /api/products) + จัดการรูปภาพ (POST/DELETE .../images)
 * ไม่ส่ง product_id ทาง Intent = โหมดสร้างใหม่ | ส่งมา = โหมดแก้ไข
 */
public class ProductFormActivity extends AppCompatActivity {

    private final ProductApiService productApiService = new ProductApiService();
    private final CategoryApiService categoryApiService = new CategoryApiService();

    private long productId = -1;
    private boolean isEditMode;
    private final List<CategoryDao> categories = new ArrayList<>();
    private ActivityProductFormBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call SplashScreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityProductFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        productId = intent.getLongExtra("product_id", -1);
        isEditMode = productId != -1;

        initComponent();
    }

    private void initComponent() {
        binding.tvFormTitle.setText(isEditMode ? "แก้ไขสินค้า" : "เพิ่มสินค้าใหม่");
        binding.btnSave.setOnClickListener(view -> save());
        binding.btnAddImage.setOnClickListener(view -> addImage());

        if (isEditMode) {
            binding.layoutImagesSection.setVisibility(View.VISIBLE);
        }

        loadCategories(); // โหลด spinner ก่อน แล้วค่อย loadProduct (ถ้ามี) เพื่อเลือกหมวดหมู่ที่ถูกต้อง
    }

    // -------- Category Spinner
    private void loadCategories() {
        categoryApiService.getAll(new ApiCallback<List<CategoryDao>>() {
            @Override
            public void onSuccess(List<CategoryDao> data, String message) {
                if (isFinishing() || isDestroyed()) return;
                categories.clear();
                categories.addAll(data);

                List<String> names = new ArrayList<>();
                names.add("ไม่ระบุหมวดหมู่");
                for (CategoryDao categories: categories) names.add(categories.getCategory_name());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ProductFormActivity.this, android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerCategory.setAdapter(adapter);
                
                if (isEditMode) loadProduct();
            }

            @Override
            public void onError(String message, int httpCode) {
                Toast.makeText(ProductFormActivity.this, "โหลดหมวดหมู่ไม่สำเร็จ: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --------- Load existing product (edit mode)
    private void loadProduct() {
        productApiService.getById(productId, new ApiCallback<ProductDao>() {
            @Override
            public void onSuccess(ProductDao product, String message) {
                if (isFinishing() || isDestroyed()) return;
                binding.edtProductName.setText(product.getProduct_name());
                binding.edtProductDescp.setText(product.getProduct_descp());
                binding.edtProductPrice.setText(product.getPrice().toPlainString());
                binding.edtStockQty.setText(String.valueOf(product.getStock_qty()));

                if (product.getCategory_id() != null) {
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).getCategory_id() == product.getCategory_id()) {
                            binding.spinnerCategory.setSelection(i + 1); // +1 เพราะ index 0 คือ "ไม่ระบุหมวดหมู่"
                            break;
                        }
                    }
                }
                renderImages(product.getImages());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --------- Image (edit mode only)
    private void renderImages(List<ProductImageDao> images) {
        binding.layoutImageList.removeAllViews();
        if (images == null) return;
        for (ProductImageDao img : images) {
            RowProductImageBinding rowProductImageBinding = RowProductImageBinding.inflate(LayoutInflater.from(this),binding.layoutImageList,false);
            rowProductImageBinding.tvImageUrl.setText(img.getImage_url());
            rowProductImageBinding.tvPrimaryBadge.setVisibility(img.isPrimary() ? View.VISIBLE : View.GONE);
            rowProductImageBinding.btnDeleteImage.setOnClickListener(view -> deleteImage(img.getProduct_img_id()));
            binding.layoutImageList.addView(rowProductImageBinding.getRoot());
        }
    }

    private void addImage() {
        String url = binding.edtImageUrl.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "กรอก URL รูปภาพก่อน", Toast.LENGTH_LONG).show();
            return;
        }
        productApiService.addImage(productId, url, binding.checkboxIsPrimary.isChecked(), new ApiCallback<ProductDao>() {
            @Override
            public void onSuccess(ProductDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                binding.edtImageUrl.setText("");
                binding.checkboxIsPrimary.setChecked(false);
                renderImages(data.getImages());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteImage(long productImgId) {
        productApiService.deleteImage(productId, productImgId, new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                if (isFinishing() || isDestroyed()) return;
                loadProduct(); // โหลดใหม่เพื่ออัปเดตรายการรูป
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------- Save (Create/Update)
    private void save() {
        String name = binding.edtProductName.getText().toString().trim();
        String des = binding.edtProductDescp.getText().toString().trim();
        String priceStr = binding.edtProductPrice.getText().toString().trim();
        String stockStr = binding.edtStockQty.getText().toString().trim();
        
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(stockStr)) {
            Toast.makeText(this, "กรอกชื่อ ราคา และจำนวนคงเหลือให้ครบ", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal price;
        int stockQty;
        try {
            price = new BigDecimal(priceStr);
            stockQty = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ราคาหรือจำนวนคงเหลือไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
            return;
        }
        int spinnerPos = binding.spinnerCategory.getSelectedItemPosition();
        Long categoryId = spinnerPos <= 0 ? null : categories.get(spinnerPos - 1).getCategory_id();

        // จัดการรูปภาพผ่าน addImage/deleteImage แยกต่างหาก (ดูข้างบน) จึงส่ง images ว่างที่นี่เสมอ
        ProductRequestDao request = new ProductRequestDao(name, des,price,stockQty, categoryId, Collections.emptyList());

        setLoading(true);
        ApiCallback<ProductDao> callback = new ApiCallback<ProductDao>() {
            @Override
            public void onSuccess(ProductDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(ProductFormActivity.this, message, Toast.LENGTH_SHORT).show();
                if (!isEditMode) {
                    // สร้างใหม่เสร็จแล้ว -> เปิดหน้านี้อีกครั้งในโหมดแก้ไขเพื่อเพิ่มรูปภาพต่อได้ทันที
                    productId = data.getProduct_id();
                    isEditMode = true;
                    binding.tvFormTitle.setText("แก้ไขสินค้า");
                    binding.layoutImagesSection.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(ProductFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };

        if (isEditMode) {
            productApiService.update(productId, request, callback);
        } else {
            productApiService.create(request, callback);
        }
    }
    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSave.setEnabled(!loading);
    }
}