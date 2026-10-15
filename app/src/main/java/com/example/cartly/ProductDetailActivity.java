package com.example.cartly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.cartly.Adapter.ReviewAdapter;
import com.example.cartly.ApiService.CartApiService;
import com.example.cartly.ApiService.ProductApiService;
import com.example.cartly.ApiService.ReviewApiService;
import com.example.cartly.ResponseDao.CartDao;
import com.example.cartly.ResponseDao.ProductDao;
import com.example.cartly.ResponseDao.ReviewDao;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.ActivityProductDetailBinding;

import java.util.List;

/**
 * รายละเอียดสินค้า - ใช้ 3 module: Product (ดู/แก้ไข/ลบ), Cart (เพิ่มลงในตระกร้า), Review (ดู/เพิ่ม/ลบ)
 * ส่ง id มาทาง internet: intent.putExtra("product_id", 1L)
 */
public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private final ProductApiService productApiService = new ProductApiService();
    private final CartApiService cartApiService = new CartApiService();
    private final ReviewApiService reviewApiService = new ReviewApiService();

    private long productId;
    private int quantity = 1;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call SplashScreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        productId = intent.getLongExtra("product_id", -1);
        initComponent();
    }

    private void initComponent() {
        binding.btnMinus.setOnClickListener(view -> changeQuantity(-1));
        binding.btnPlus.setOnClickListener(view -> changeQuantity(1));
        binding.btnAddToCart.setOnClickListener(view -> addToCart());
        binding.btnSubmitReview.setOnClickListener(view -> submitReview());

        setupAdminAction();
        setupReviewList();

        loadProduct();
        loadReviews();
    }

    private void changeQuantity(int delta) {
        quantity = Math.max(1, quantity + delta);
        binding.tvQuantity.setText(String.valueOf(quantity));
    }

    // ---------Products
    private void loadProduct() {
        productApiService.getById(productId, new ApiCallback<ProductDao>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ProductDao product, String message) {
                if (isFinishing() || isDestroyed()) return;
                binding.tvProductName.setText(product.getProduct_name());
                binding.tvProductPrice.setText("฿" + product.getPrice().toPlainString());
                binding.tvProductStock.setText("คงเหลือ: " + product.getStock_qty() + " ชิ้น");
                binding.tvProductDescp.setText(product.getProduct_descp());

                String imageUrl = product.getPrimaryImageURL();
                if (imageUrl == null || imageUrl.isEmpty()) {
                    binding.ivProduct.setImageResource(R.drawable.icon_imagesmode_24);
                } else {
                    Glide.with(ProductDetailActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.icon_imagesmode_24)
                            .error(R.drawable.icon_imagesmode_24)
                            .into(binding.ivProduct);
                }
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart() {
        if (!TokenManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        binding.btnAddToCart.setEnabled(false);
        cartApiService.addItem(productId, quantity, new ApiCallback<CartDao>() {
            @Override
            public void onSuccess(CartDao cart, String message) {
                if (isFinishing() || isDestroyed()) return;
                binding.btnAddToCart.setEnabled(true);
                Toast.makeText(ProductDetailActivity.this,
                        "เพิ่มลงในตระกร้าแล้ว (" + cart.getTotalItemCount() + "รายการ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                binding.btnAddToCart.setEnabled(true);
                if (httpCode == ApiCallHandler.UNAUTHORIZE) {
                    TokenManager.clear();
                    startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                    return;
                }
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // -------- ADMIN
    private void setupAdminAction() {
        if (!TokenManager.isAdmin()) return;
        binding.layoutAdminActions.setVisibility(View.VISIBLE);

        binding.ivBtnEditProduct.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProductFormActivity.class);
            intent.putExtra("product_id", productId);
            startActivity(intent);
        });

        binding.ivBtnDeleteProduct.setOnClickListener(view -> {
            productApiService.delete(productId, new ApiCallback<Object>() {
                @Override
                public void onSuccess(Object data, String message) {
                    Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message, int httpCode) {
                    Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // ----------Reviews
    private void setupReviewList() {
        binding.recyclerReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(this::deleteReview);
        binding.recyclerReviews.setAdapter(reviewAdapter);
    }

    private void loadReviews() {
        reviewApiService.getByProduct(productId, new ApiCallback<List<ReviewDao>>() {
            @Override
            public void onSuccess(List<ReviewDao> data, String message) {
                if (isFinishing() || isDestroyed()) return;
                reviewAdapter.setItems(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitReview() {
        if (!TokenManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        int rating = (int) binding.ratingBarInput.getRating();
        if (rating < 1) {
            Toast.makeText(this, "กรุณาให้คะแนนอย่างน้อย 1 ดาว", Toast.LENGTH_SHORT).show();
            return;
        }
        String comment = binding.edtComment.getText().toString().trim();

        reviewApiService.create(productId, rating, comment.isEmpty() ? null : comment, new ApiCallback<ReviewDao>() {
            @Override
            public void onSuccess(ReviewDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                binding.edtComment.setText("");
                loadReviews(); // โหลดใหม่แทนการแหรกเองใน adapter เพื่อให้ id/create_at ตรงกับ backend
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteReview(ReviewDao review) {
        reviewApiService.delete(review.getReview_id(), new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                if (isFinishing() || isDestroyed()) return;
                reviewAdapter.removeById(review.getReview_id());
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}