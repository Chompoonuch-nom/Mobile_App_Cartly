package com.example.cartly.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cartly.AddressListActivity;
import com.example.cartly.ApiService.AuthApiService;
import com.example.cartly.ApiService.UserApiService;
import com.example.cartly.CategoryManageActivity;
import com.example.cartly.LoginActivity;
import com.example.cartly.MainActivity;
import com.example.cartly.R;
import com.example.cartly.ResponseDao.UserDao;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.FragmentProfileBinding;

import java.util.Objects;

import javax.security.auth.login.LoginException;
/**
 * โปรไฟล์ผู้ใช้ - GET /api/users/me + ทางลัดไปหน้าที่อยู่/คำสั่งซื้อ + ออกจากระบบ
 */
public class ProfileFragment extends Fragment {
    private final UserApiService userApiService = new UserApiService();
    private final AuthApiService authApiService = new AuthApiService();
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        userApiService.getMyProfile(new ApiCallback<UserDao>() {
            @Override
            public void onSuccess(UserDao user, String message) {
                if (binding == null) return;
                render(user);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (binding == null) return;
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
        if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .placeholder(R.drawable.account_circle_24)
                    .error(R.drawable.account_circle_24)
                    .into(binding.mvAvatar);
        } else {
            binding.mvAvatar.setImageResource(R.drawable.account_circle_24);
        }
        binding.tvProfileFullName.setText(user.getFull_name());
        binding.tvProfileUsername.setText("@" + user.getUsername());
        binding.tvProfileRoleBadge.setText(user.isAdmin() ? "ผู้ดูแลระบบ" : "ลูกค้า");
        binding.tvProfileEmail.setText(user.getEmail());
        binding.tvProfilePhone.setText(user.getPhone_number());
        binding.tvProfileMemberSince.setText(user.getCreated_at());
    }
    private void logout() {
        authApiService.logout(); // TokenManager.clear()
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}