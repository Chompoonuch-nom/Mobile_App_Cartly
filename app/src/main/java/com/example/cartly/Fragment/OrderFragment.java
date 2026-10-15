package com.example.cartly.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cartly.Adapter.OrderAdapter;
import com.example.cartly.ApiService.OrderApiService;
import com.example.cartly.MainActivity;
import com.example.cartly.OrderDetailActivity;
import com.example.cartly.ResponseDao.OrderDao;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.FragmentOrderBinding;

import java.util.List;

/** ประวัติคำสั่งซื้อของฉัน — GET /api/orders (กดรายการเพื่อเปิดรายละเอียด) */
public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private final OrderApiService orderApiService = new OrderApiService();
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderAdapter = new OrderAdapter(order -> {
            Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
            intent.putExtra("order_id", order.getOrder_id());
            startActivity(intent);
        });
        binding.recyclerOrders.setAdapter(orderAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) loadOrders(); //กลับมาจากหน้ารายละเอียด (อาจยกเลิกออเดอร์ไปแล้ว)
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) loadOrders();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadOrders() {
        orderApiService.getMyOrders(new ApiCallback<List<OrderDao>>() {
            @Override
            public void onSuccess(List<OrderDao> data, String message) {
                if (binding == null) return;
                orderAdapter.setItems(data);
                binding.tvEmptyOrders.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
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
}