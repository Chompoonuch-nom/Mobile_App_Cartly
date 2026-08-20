package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cartly.databinding.FragmentProfileBinding;
import javax.security.auth.login.LoginException;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding fragmentProfileBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

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

        initComponent();
        return view;
    }

    private void initComponent() {
        signOut();
    }

    private void signOut() {
        fragmentProfileBinding.btnSightOutLayout.setOnClickListener(view -> {
            Intent signOutIntent = new Intent(getContext(), LoginException.class);
            startActivity(signOutIntent);
        });
    }
}