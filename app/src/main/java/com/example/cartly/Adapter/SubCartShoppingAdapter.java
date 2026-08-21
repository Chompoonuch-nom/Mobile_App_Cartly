package com.example.cartly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.Dao.ItemProductDao;
import com.example.cartly.Interface.OnClickProductSelectInterface;
import com.example.cartly.databinding.ItemSubCartShoppingBinding;

import java.util.ArrayList;

public class SubCartShoppingAdapter extends RecyclerView.Adapter<SubCartShoppingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemProductDao> products;
    private OnClickProductSelectInterface onClickProductSelectInterface;

    public SubCartShoppingAdapter (Context context,ArrayList<ItemProductDao> products) {
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public SubCartShoppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubCartShoppingBinding binding = ItemSubCartShoppingBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCartShoppingAdapter.ViewHolder holder, int position) {
        ItemProductDao product = products.get(position);
        holder.binding.mvImageProduct.setImageResource(product.getImage());
        holder.binding.tvNameProduct.setText(product.getProduct_name());

        String total = String.valueOf(product.total);
        holder.binding.tvTotal.setText(total);
        holder.binding.tvCount.setText(String.valueOf(product.getCount()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSubCartShoppingBinding binding;
        public ViewHolder(@NonNull ItemSubCartShoppingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
