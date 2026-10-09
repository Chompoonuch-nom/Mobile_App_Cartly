package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cartly.ApiService.AuthApiService;
import com.example.cartly.ApiService.UserApiService;
import com.example.cartly.LoginActivity;
import com.example.cartly.R;
import com.example.cartly.ResponseDao.UserDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.FragmentProfileBinding;

import java.util.Objects;

import javax.security.auth.login.LoginException;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding fragmentProfileBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final UserApiService userApiService = new UserApiService();
    private final AuthApiService authApiService = new AuthApiService();

    /**
     * โปรไฟล์ผู้ใช้ - GET /api/users/me + ทางลัดไปหน้าที่อยู่/คำสั่งซื้อ + ออกจากระบบ
     */
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(getLayoutInflater());
        View view = fragmentProfileBinding.getRoot();

        if (!TokenManager.isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        initComponent();
        return view;
    }

    private void initComponent() {
        loadProfile();
        signOut();
    }

    private void loadProfile() {
        userApiService.getMyProfile(new ApiCallback<UserDao>() {
            @Override
            public void onSuccess(UserDao user, String message) {
                if (requireActivity().isFinishing() || requireActivity().isDestroyed()) return;
                render(user);
            }

            @Override
            public void onError(String message, int httpCode) {
                if (getActivity().isFinishing() || getActivity().isDestroyed()) return;
                Log.e("Error: " , message);
            }
        });
    }

    private void render(UserDao user) {
//        if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
//            Glide.with(this)
//                    .load(user.getAvatar_url())
//                    .placeholder(R.drawable.account_circle_24)
//                    .error(R.drawable.account_circle_24)
//                    .into(fragmentProfileBinding.mvAvatar);
//        } else {
//            fragmentProfileBinding.mvAvatar.setImageResource(R.drawable.account_circle_24);
//        }

        fragmentProfileBinding.tvProfileFullName.setText(user.getFull_name());
        fragmentProfileBinding.tvProfileUsername.setText("@" + user.getUsername());
        fragmentProfileBinding.tvProfileRoleBadge.setText(user.isAdmin() ? "ผู้ดูแลระบบ" : "ลูกค้า");
        fragmentProfileBinding.tvProfileEmail.setText(user.getEmail());
        fragmentProfileBinding.tvProfilePhone.setText(user.getPhone_number());
        fragmentProfileBinding.tvProfileMemberSince.setText(user.getCreated_at());
    }

    private void signOut() {
        fragmentProfileBinding.btnSightOutLayout.setOnClickListener(view -> {
            authApiService.logout(); // TokenManager.clear()
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}