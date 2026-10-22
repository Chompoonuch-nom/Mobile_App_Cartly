package com.example.cartly;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.cartly.ApiService.AddressApiService;
import com.example.cartly.RequestDao.AddressRequestDao;
import com.example.cartly.ResponseDao.AddressDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivityAddressFormBinding;

import java.util.List;

/**
 * สร้าง/แก้ไขที่อยู่ (POST, PUT /api/addresses) — ไม่มี GET by id ฝั่ง backend จึงโหลดจาก list มากรองเอา
 */
public class AddressFormActivity extends AppCompatActivity {
    private ActivityAddressFormBinding binding;
    private final AddressApiService addressApiService = new AddressApiService();

    private long addressId = -1;
    private boolean isEditMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call SplashScreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddressFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addressId = getIntent().getLongExtra("address_id", -1);
        isEditMode = addressId != -1;

        binding.toolbar.setTitle(isEditMode ? "แก้ไขที่อยู่" : "เพิ่มที่อยู่ใหม่");
        binding.toolbar.setTitleTextAppearance(this,R.style.ToolbarTitleStyle);
        binding.toolbar.setOnBackClickListener(this::finish);

        if (isEditMode) loadExistingAddress();

        binding.btnSave.setOnClickListener(view -> save());
    }

    private void loadExistingAddress() {
        // backend ไม่มี GET /api/addresses/{id} เดี่ยว ๆ ใช้ list แล้วกรองเอาแทน
        addressApiService.getMyAddresses(new ApiCallback<List<AddressDao>>() {
            @Override
            public void onSuccess(List<AddressDao> data, String message) {
                if (isFinishing() || isDestroyed()) return;
                for (AddressDao address : data) {
                    if (address.getAddress_id() == addressId) {
                        fillForm(address);
                        return;
                    }
                }
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(AddressFormActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void fillForm(AddressDao address) {
        binding.edtLabelType.setText(address.getLabel_type());
        binding.edtRecipientName.setText(address.getRecipient_name());
        binding.edtPhoneNumber.setText(address.getPhone_number());
        binding.edtAddressLine.setText(address.getAddress_line());
        binding.edtSubDistrict.setText(address.getSub_district());
        binding.edtDistrict.setText(address.getDistrict());
        binding.edtProvince.setText(address.getProvince());
        binding.edtPostalCode.setText(address.getPostal_code());
        binding.cbIsDefault.setChecked(address.is_default());
    }
    
    private void save() {
        String recipient = binding.edtRecipientName.getText().toString().trim();
        String phone = binding.edtPhoneNumber.getText().toString().trim();
        String line = binding.edtLabelType.getText().toString().trim();
        String subDistrict = binding.edtSubDistrict.getText().toString().trim();
        String district = binding.edtDistrict.getText().toString().trim();
        String province = binding.edtProvince.getText().toString().trim();
        String postal = binding.cbIsDefault.getText().toString().trim();
        
        if (TextUtils.isEmpty(recipient) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(line)
                || TextUtils.isEmpty(subDistrict) || TextUtils.isEmpty(district)
                || TextUtils.isEmpty(province) || TextUtils.isEmpty(postal)) {
            Toast.makeText(this, "กรอกข้อมูลที่อยู่ให้ครบทุกช่อง", Toast.LENGTH_SHORT).show();
            return;
        }

        AddressRequestDao request = new AddressRequestDao(
                binding.edtLabelType.getText().toString().trim(), recipient, phone, line,
                subDistrict, district, province, postal, binding.cbIsDefault.isChecked());

        setLoading(true);

        ApiCallback<AddressDao> callback = new ApiCallback<AddressDao>() {
            @Override
            public void onSuccess(AddressDao data, String message) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(AddressFormActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message, int httpCode) {
                if (isFinishing() || isDestroyed()) return;
                setLoading(false);
                Toast.makeText(AddressFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };

        if (isEditMode) addressApiService.update(addressId, request, callback);
        else addressApiService.create(request, callback);
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSave.setEnabled(!loading);
    }
}