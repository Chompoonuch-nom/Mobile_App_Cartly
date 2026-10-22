package com.example.cartly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.cartly.ApiService.CartApiService;
import com.example.cartly.ApiService.OrderApiService;
import com.example.cartly.ResponseDao.CartDao;
import com.example.cartly.ResponseDao.OrderDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.ProductListState;
import com.example.cartly.databinding.ActivityCheckoutBinding;

/** ยืนยันคำสั่งซื้อ — เลือกที่อยู่ (AddressListActivity แบบ select mode) แล้วเรียก POST /api/orders/checkout */
public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private static final int REQ_SELECT_ADDRESS = 100;
    private final CartApiService cartApiService = new CartApiService();
    private final OrderApiService orderApiService = new OrderApiService();

    private Long selectedAddressId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call Splashscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("ยืนยันคำสั่งซื้อ");
        binding.toolbar.setTitleTextAppearance(this,R.style.ToolbarTitleStyle);
        binding.toolbar.setOnBackClickListener(this::finish);

        binding.tvSelectedAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            intent.putExtra("select_mode", true);
            startActivityForResult(intent, REQ_SELECT_ADDRESS);
        });

        binding.btnConfirmOrder.setOnClickListener(view -> confirmOrder());
        loadCartSummary();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SELECT_ADDRESS && resultCode == Activity.RESULT_OK && data != null) {
            selectedAddressId = data.getLongExtra("address_id", -1);
            binding.tvSelectedAddress.setText(data.getStringExtra("address_text"));
        }
    }

    private void loadCartSummary() {
        cartApiService.getMyCart(new ApiCallback<CartDao>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(CartDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                binding.tvCheckoutTotal.setText("ยอดรวม: ฿" + data.getTotalAmount().toPlainString());
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private String getSelectedPaymentMethod() {
        int id = binding.radioPaymentMethod.getCheckedRadioButtonId();
        if (id == binding.rbCreditCard.getId()) return "CREDIT_CARD";
        if (id == binding.rbPromptpay.getId()) return "PROMPTPAY";
        if (id == binding.rbTruemoney.getId()) return "TRUEMONEY";
        return "COD";
    }
    
    private void confirmOrder() {
        if (selectedAddressId == null) {
            Toast.makeText(this, "กรุณาเลือกที่อยู่จัดส่งก่อน", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        orderApiService.checkout(selectedAddressId, getSelectedPaymentMethod(), new ApiCallback<OrderDao>() {
            @Override
            public void onSuccess(OrderDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                ProductListState.markStale(); // สต็อกสินค้าลดลง
                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckoutActivity.this, OrderDetailActivity.class);
                intent.putExtra("order_id", data.getOrder_id());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.layoutLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnConfirmOrder.setEnabled(!loading);
    }
}