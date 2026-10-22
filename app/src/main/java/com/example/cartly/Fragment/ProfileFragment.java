package com.example.cartly.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cartly.AddressListActivity;
import com.example.cartly.ApiService.AuthApiService;
import com.example.cartly.ApiService.UploadApiService;
import com.example.cartly.ApiService.UserApiService;
import com.example.cartly.CategoryManageActivity;
import com.example.cartly.LoginActivity;
import com.example.cartly.MainActivity;
import com.example.cartly.R;
import com.example.cartly.ResponseDao.UploadResultDao;
import com.example.cartly.ResponseDao.UserDao;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.ImagePicker;
import com.example.cartly.Util.ImageUrlResolver;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.FragmentProfileBinding;

/**
 * โปรไฟล์ผู้ใช้ - GET /api/users/me + ทางลัดไปหน้าที่อยู่/คำสั่งซื้อ + ออกจากระบบ
 */
public class ProfileFragment extends Fragment {
    private final UserApiService userApiService = new UserApiService();
    private final AuthApiService authApiService = new AuthApiService();
    private final UploadApiService uploadApiService = new UploadApiService();

    // ต้องเป็น field เพื่อให้ลงทะเบียนตัวรับผลลัพธ์ (กล้อง/แกลเลอรี) ทันก่อน Fragment เริ่มทำงาน
    private final ImagePicker imagePicker = new ImagePicker(this, this::onAvatarPicked);
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // สลับไป tab คำสั่งซื้อของ BottomNavigation แทนการเปิดหน้าใหม่
        binding.btnMyOrderLayout.setOnClickListener(v ->
                ((MainActivity) requireActivity()).selectTab(R.id.nav_orders));
        binding.btnMyAddressesLayout.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddressListActivity.class)));
        if (TokenManager.isAdmin()) {
            binding.btnManageCategoriesLayout.setVisibility(View.VISIBLE);
            binding.btnManageCategoriesLayout.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), CategoryManageActivity.class)));
        }

        binding.btnSightOutLayout.setOnClickListener(v -> logout());

        // แตะรูปโปรไฟล์ หรือปุ่ม "เปลี่ยนรูปโปรไฟล์" -> ถ่ายรูป / เลือกจากแกลเลอรี
        binding.mvAvatar.setOnClickListener(v -> imagePicker.showChooser(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) loadProfile();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) loadProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadProfile() {
        if (binding == null) return;
//        binding.progressBar.setVisibility(View.VISIBLE);
        userApiService.getMyProfile(new ApiCallback<UserDao>() {
            @Override
            public void onSuccess(UserDao user, String message) {
                if (binding == null) return;
//                binding.progressBar.setVisibility(View.GONE);
                render(user);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (binding == null) return;
//                binding.progressBar.setVisibility(View.GONE);
                if (httpCode == ApiCallHandler.UNAUTHORIZE) {
                    ((MainActivity) requireActivity()).onSessionExpire();
                    return;
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void render(UserDao user) {
        binding.tvProfileFullName.setText(user.getFull_name());
        binding.tvProfileUsername.setText("@" + user.getUsername());
        binding.tvProfileRoleBadge.setText(user.isAdmin() ? "ผู้ดูแลระบบ" : "ลูกค้า");
        binding.tvProfileEmail.setText(user.getEmail());
        binding.tvProfilePhone.setText(user.getPhone_number());
        binding.tvProfileMemberSince.setText(user.getCreated_at());

        String avatarUrl = ImageUrlResolver.resolve(user.getAvatar_url());
        if (avatarUrl == null) {
            Glide.with(binding.mvAvatar).clear(binding.mvAvatar);
            binding.mvAvatar.setPadding(dp(16), dp(16), dp(16), dp(16));
            binding.mvAvatar.setImageResource(R.drawable.account_circle_24);
        } else {
            binding.mvAvatar.setPadding(0,0,0,0); // รูปจริงเต็มวงกลม
            Glide.with(binding.mvAvatar)
                    .load(avatarUrl)
                    .placeholder(R.drawable.account_circle_24)
                    .error(R.drawable.account_circle_24)
                    .centerCrop()
                    .into(binding.mvAvatar);
        }
    }

    private void onAvatarPicked(Uri uri) {
        if (binding == null) return;
//        binding.progressBar.setVisibility(View.VISIBLE);
        uploadApiService.uploadImage(requireContext(), uri, new ApiCallback<UploadResultDao>() {
            @Override
            public void onSuccess(UploadResultDao data, String message) {
                if (binding == null) return;
                userApiService.updateAvatar(data.getUrl(), new ApiCallback<UserDao>() {
                    @Override
                    public void onSuccess(UserDao user, String message) {
                        if (binding == null) return;
//                        binding.progressBar.setVisibility(View.GONE);
                        render(user);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message, int httpCode) {
                        handleAvatarError(message, httpCode);
                    }
                });
            }

            @Override
            public void onError(String message, int httpCode) {
                handleAvatarError(message, httpCode);
            }
        });
    }

    private void handleAvatarError(String message, int httpCode) {
        if (binding == null) return;
//        binding.progressBar.setVisibility(View.GONE);
        if (httpCode == ApiCallHandler.UNAUTHORIZE) {
            ((MainActivity) requireActivity()).onSessionExpire();
            return;
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
    private void logout() {
        authApiService.logout(); // TokenManager.clear()
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}