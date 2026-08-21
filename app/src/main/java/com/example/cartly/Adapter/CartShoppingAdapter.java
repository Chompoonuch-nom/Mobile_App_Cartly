package com.example.cartly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.Dao.CartDao;
import com.example.cartly.Dao.ItemProductDao;
import com.example.cartly.Dao.StoreDao;
import com.example.cartly.Interface.OnClickProductSelectInterface;
import com.example.cartly.databinding.ItemCartShoppingBinding;

import java.util.ArrayList;

public class CartShoppingAdapter extends RecyclerView.Adapter<CartShoppingAdapter.ViewHolder> {
    private Context context;
    private CartDao carts;
    private ArrayList<StoreDao> stores;
    private OnClickProductSelectInterface onClickProductSelectInterface;

    public CartShoppingAdapter (Context context, ArrayList<StoreDao> stores) {
        this.context = context;
        this.stores = stores;
    }

    public void setOnClickProductSelect (OnClickProductSelectInterface onClickProductSelect) {
        this.onClickProductSelectInterface = onClickProductSelect;
    }
    @NonNull
    @Override
    public CartShoppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartShoppingBinding binding = ItemCartShoppingBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartShoppingAdapter.ViewHolder holder, int position) {
        StoreDao store = stores.get(position);
        ArrayList<ItemProductDao> item_product = store.getProducts();
        holder.binding.tvNameStore.setText(store.getStore_name());

        holder.binding.recyclerViewSubCard.setLayoutManager(new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        SubCartShoppingAdapter subCartShoppingAdapter = new SubCartShoppingAdapter(context.getApplicationContext(), item_product);
        holder.binding.recyclerViewSubCard.setAdapter(subCartShoppingAdapter);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartShoppingBinding binding;
        public ViewHolder(@NonNull ItemCartShoppingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
