package com.example.cartly.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.ResponseDao.OrderDao;
import com.example.cartly.databinding.ItemOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    public interface Listener {
        void onOrderClick(OrderDao order);
    }

    private final List<OrderDao> items = new ArrayList<>();
    private final Listener listener;

    public OrderAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<OrderDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderDao order = items.get(position);
        holder.binding.tvOrderId.setText("คำสั่งซื้อ #" + order.getOrder_id());
        holder.binding.tvOrderStatus.setText(order.getOrder_status());
        holder.binding.tvOrderDate.setText(order.getCreated_at() != null && order.getCreated_at().length() >= 10 ? order.getCreated_at().substring(0, 10) : "");
        holder.binding.tvOrderTotal.setText("฿" + order.getTotal_amount().toPlainString());
        holder.itemView.setOnClickListener(view -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemOrderBinding binding;
        public ViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
