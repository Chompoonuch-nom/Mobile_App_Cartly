package com.example.cartly.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.ResponseDao.AddressDao;
import com.example.cartly.databinding.ItemAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    public interface Listener {
        void onRowClick(AddressDao address); //ใช้ตอนเลือกที่อยู่สำหรับ checkout
        void onEditClick(AddressDao address);
        void onDeleteClick(AddressDao address);
    }

    private final List<AddressDao> items = new ArrayList<>();
    private final Listener listener;
    private final boolean selectMode;

    public AddressAdapter(Listener listener, boolean selectMode) {
        this.listener = listener;
        this.selectMode = selectMode;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<AddressDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        AddressDao address = items.get(position);
        String label = address.getLabel_type() == null || address.getLabel_type().isEmpty() ? "ที่อยู่จัดส่ง" : address.getLabel_type();
        holder.binding.tvAddressLabel.setText(label + (address.is_default() ? "  (ค่าเริ่มต้น)" : ""));
        holder.binding.tvAddressRecipient.setText(address.getRecipient_name());
        holder.binding.tvAddressFull.setText(address.getFullAddressText());
        holder.binding.tvAddressPhone.setText(address.getPhone_number());

        if (selectMode) {
            holder.itemView.setOnClickListener(view -> listener.onRowClick(address));
        }
        holder.binding.btnEditAddress.setOnClickListener(view -> listener.onEditClick(address));
        holder.binding.btnDeleteAddress.setOnClickListener(view -> listener.onDeleteClick(address));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemAddressBinding binding;
        public ViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
