package com.example.cartly.Fragment;

import android.annotation.SuppressLint;
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

import com.example.cartly.Adapter.CartItemAdapter;
import com.example.cartly.ApiService.CartApiService;
import com.example.cartly.CheckoutActivity;
import com.example.cartly.MainActivity;
import com.example.cartly.ResponseDao.CartDao;
import com.example.cartly.ResponseDao.CartItemDao;
import com.example.cartly.Util.ApiCallHandler;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.FragmentCartBinding;

/**
 * ตะกร้าสินค้า — GET/PUT/DELETE /api/cart, /api/cart/items/{id}
 */
public class CartFragment extends Fragment {
    private final CartApiService cartApiService = new CartApiService();
    private FragmentCartBinding binding;
    private CartItemAdapter cartItemAdapter;

    /** callback ร่วมของ โหลดตะกร้า / เปลี่ยนจำนวน / ลบรายการ — ผลลัพธ์เป็น Cart ล่าสุดเหมือนกันหมด */
    private final ApiCallback<CartDao> renderCallback = new ApiCallback<CartDao>() {
        @Override
        public void onSuccess(CartDao data, String message) {
            if (binding == null) return;
            renderCart(data);
        }

        @Override
        public void onError(String message, int httpCode) {
            handleError(message, httpCode);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartItemAdapter = new CartItemAdapter(new CartItemAdapter.Listener() {
            @Override
            public void onIncrease(CartItemDao item) {
                cartApiService.updateItemQuantity(item.getCart_item_id(), item.getQuantity() + 1, renderCallback);
            }

            @Override
            public void onDecrease(CartItemDao item) {
                // <= 0 -> backend จะลบรายการให้เอง
                cartApiService.updateItemQuantity(item.getCart_item_id(), item.getQuantity() - 1, renderCallback);
            }

            @Override
            public void onRemove(CartItemDao item) {
                cartApiService.removeItem(item.getCart_item_id(), renderCallback);
            }
        });
        binding.recyclerCart.setAdapter(cartItemAdapter);

        binding.btnClearCart.setOnClickListener(v-> clearCart());
        binding.btnCheckoutCart.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CheckoutActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) loadCart(); // กลับมาจากหน้า checkout / หน้าสินค้า
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) loadCart(); // สลับ tab มาที่ตะกร้า -> โหลดข้อมูลล่าสุด
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadCart() {
        cartApiService.getMyCart(renderCallback);
    }

    @SuppressLint("SetTextI18n")
    private void renderCart(CartDao cart) {
        cartItemAdapter.setItems(cart.getItems());
        boolean empty = cart.getItems() == null || cart.getItems().isEmpty();
        binding.tvEmptyCart.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.recyclerCart.setVisibility(empty ? View.GONE : View.VISIBLE);
        binding.tvCartTotal.setText("ยอดรวม: ฿" + cart.getTotalAmount().toPlainString());
    }

    private void clearCart() {
        cartApiService.clearCart(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                if (binding == null) return;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                loadCart();
            }

            @Override
            public void onError(String message, int httpCode) {
                handleError(message, httpCode);
            }
        });
    }

    private void handleError(String message, int httpCode) {
        if (binding == null) return;
        if (httpCode == ApiCallHandler.UNAUTHORIZE) {
            ((MainActivity) requireActivity()).onSessionExpire();
            return;
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}