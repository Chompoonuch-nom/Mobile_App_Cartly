package com.example.cartly.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.ResponseDao.CartItemDao;
import com.example.cartly.databinding.ItemCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    public interface Listener {
        void onIncrease(CartItemDao item);
        void onDecrease(CartItemDao item);
        void onRemove(CartItemDao item);
    }

    private final List<CartItemDao> items = new ArrayList<>();
    private final Listener listener;

    public CartItemAdapter(Listener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<CartItemDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        CartItemDao item = items.get(position);
        holder.binding.tvCartProductName.setText(item.getProduct().getProduct_name());
        holder.binding.tvCartUnitPrice.setText("฿" + item.getProduct().getPrice().toPlainString() + " / ชิ้น");
        holder.binding.tvCartSubtotal.setText("฿" + item.getSubTotal().toPlainString());
        holder.binding.tvCartQuantity.setText(String.valueOf(item.getQuantity()));

        holder.binding.btnCartPlus.setOnClickListener(view -> listener.onIncrease(item));
        holder.binding.btnCartMinus.setOnClickListener(view -> listener.onDecrease(item));
        holder.binding.btnCartRemove.setOnClickListener(view -> listener.onRemove(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemCartBinding binding;
        public ViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
