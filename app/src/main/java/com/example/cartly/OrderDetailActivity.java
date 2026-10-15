package com.example.cartly;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.cartly.ApiService.OrderApiService;
import com.example.cartly.ResponseDao.OrderDao;
import com.example.cartly.ResponseDao.OrderItemDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.ActivityOrderDetailBinding;
import com.example.cartly.databinding.ItemOrderLineBinding;

import java.util.Arrays;
import java.util.List;

/**
 * รายละเอียดคำสั่งซื้อ - GET, PUT .../cancel, PUT .../status (admin)
 */
public class OrderDetailActivity extends AppCompatActivity {
    private ActivityOrderDetailBinding binding;
    private static final List<String> STATUS_OPTIONS =
            Arrays.asList("PENDING", "PAID", "SHIPPED", "DELIVERED", "CANCELLED");
    private final OrderApiService orderApiService = new OrderApiService();
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call Splashscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId = getIntent().getLongExtra("order_id", -1);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> finish());
        binding.btnCancelOrder.setOnClickListener(view -> cancelOrder());

        if (TokenManager.isAdmin()) {
            binding.layoutAdminStatus.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, STATUS_OPTIONS);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerStatus.setAdapter(adapter);
            binding.btnUpdateStatus.setOnClickListener(view -> updateStatus());
        }

        loadOrder();
    }

    private void loadOrder() {
        orderApiService.getById(orderId, new ApiCallback<OrderDao>() {
            @Override
            public void onSuccess(OrderDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                render(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(OrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void render(OrderDao order) {
        binding.tvOrderIdDetail.setText("คำสั่งซื้อ #" + order.getOrder_id());
        binding.tvOrderStatusDetail.setText("สถานะ: " + order.getOrder_status());
        binding.tvOrderDateDetail.setText(order.getCreated_at());
        binding.tvOrderTotalDetail.setText("ยอดรวม: ฿" + order.getTotal_amount().toPlainString());

        binding.layoutOrderItems.removeAllViews();
        if (order.getItems() != null) {
            for (OrderItemDao item : order.getItems()) {
                ItemOrderLineBinding row = ItemOrderLineBinding.inflate(getLayoutInflater(), binding.layoutOrderItems, false);
                row.tvLineName.setText(item.getProduct_name());
                row.tvLineQty.setText("x" + item.getQuantity());
                row.tvLineSubTotal.setText("฿" + item.getSubtotal().toPlainString());
                binding.layoutOrderItems.addView(row.getRoot());
            }
        }

        binding.btnCancelOrder.setVisibility("PENDING".equals(order.getOrder_status()) ? View.VISIBLE : View.GONE);

        int statusPos = STATUS_OPTIONS.indexOf(order.getOrder_status());
        if (statusPos >= 0 && TokenManager.isAdmin()) binding.spinnerStatus.setSelection(statusPos);
    }

    private void cancelOrder() {
        orderApiService.cancel(orderId, new ApiCallback<OrderDao>() {
            @Override
            public void onSuccess(OrderDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(OrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                render(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(OrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus() {
        String newStatus = STATUS_OPTIONS.get(binding.spinnerStatus.getSelectedItemPosition());
        orderApiService.updateStatus(orderId, newStatus, new ApiCallback<OrderDao>() {
            @Override
            public void onSuccess(OrderDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(OrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                render(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(OrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}