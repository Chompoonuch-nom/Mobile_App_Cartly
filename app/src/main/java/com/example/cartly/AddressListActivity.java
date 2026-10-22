package com.example.cartly;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cartly.Adapter.AddressAdapter;
import com.example.cartly.ApiService.AddressApiService;
import com.example.cartly.ResponseDao.AddressDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivityAddressListBinding;

import java.util.List;

/**
 * รายการที่อยู่จัดส่ง (GET/DELETE /api/addresses)
 * เปิดจาก CheckoutActivity ด้วย extra "select_mode"=true เพื่อให้เป็นตัวเลือกที่อยู่ (ส่งผลกลีบด้วย setResult)
 */
public class AddressListActivity extends AppCompatActivity {
    private ActivityAddressListBinding binding;
    private final AddressApiService addressApiService = new AddressApiService();
    private AddressAdapter addressAdapter;
    private boolean selectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call SplashScreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddressListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectMode = getIntent().getBooleanExtra("select_mode",false);

        binding.toolbar.setTitle(selectMode ? "แก้ไขที่อยู่" : "เพิ่มที่อยู่ใหม่");
        binding.toolbar.setTitleTextAppearance(this,R.style.ToolbarTitleStyle);
        binding.toolbar.setOnBackClickListener(this::finish);

        binding.tvSelectHint.setVisibility(selectMode ? View.VISIBLE : View.GONE);

        binding.recyclerAddress.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(new AddressAdapter.Listener() {
            @Override
            public void onRowClick(AddressDao address) {
                Intent result = new Intent();
                result.putExtra("address_id", address.getAddress_id());
                result.putExtra("address_text", address.getRecipient_name() + " - " + address.getFullAddressText());
                setResult(Activity.RESULT_OK, result);
                finish();
            }

            @Override
            public void onEditClick(AddressDao address) {
                Intent intent = new Intent(AddressListActivity.this, AddressFormActivity.class);
                intent.putExtra("address_id", address.getAddress_id());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(AddressDao address) {
                deleteAddress(address);
            }
        }, selectMode);
        binding.recyclerAddress.setAdapter(addressAdapter);

        binding.btnAddAddress.setOnClickListener(view ->
                startActivity(new Intent(this, AddressFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddresses();
    }

    private void loadAddresses() {
        addressApiService.getMyAddresses(new ApiCallback<List<AddressDao>>() {
            @Override
            public void onSuccess(List<AddressDao> data, String message) {
                if (isFinishing() || isDestroyed()) return;
                addressAdapter.setItems(data);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(AddressListActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.e("Error Load Address: ", message);
            }
        });
    }

    private void deleteAddress(AddressDao address) {
        addressApiService.delete(address.getAddress_id(), new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                Toast.makeText(AddressListActivity.this, message, Toast.LENGTH_SHORT).show();
                loadAddresses();
            }

            @Override
            public void onError(String message, int httpCode) {
                Toast.makeText(AddressListActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.e("Error Delete Address: ", message);
            }
        });
    }
}