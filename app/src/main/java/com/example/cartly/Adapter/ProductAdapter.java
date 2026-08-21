package com.example.cartly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cartly.Dao.ProductDao;
import com.example.cartly.Interface.OnClickItemListenerInterface;
import com.example.cartly.Interface.OnClickProductSelectInterface;
import com.example.cartly.databinding.ItemProductBinding;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private OnClickProductSelectInterface onClick;
    private ArrayList<ProductDao> itemProduct;

    public ProductAdapter(Context context,ArrayList<ProductDao> product, OnClickProductSelectInterface onClick) {
        this.context = context;
        this.itemProduct = product;
        this.onClick = onClick;
    }

    public void setOnClickProductSelectInterface (OnClickProductSelectInterface onClick) {
        this.onClick = onClick;
    }
    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductDao item_product = itemProduct.get(position);
        holder.binding.mvProduct.setImageResource(item_product.image);
        holder.binding.tvNameProduct.setText(item_product.product_name);
        holder.binding.tvDetailProduct.setText(item_product.product_detail);
        holder.binding.tvPriceProduct.setText(item_product.product_price);

        holder.binding.mvAddToCart.setOnClickListener(view -> {
            onClick.onCreateClick(position);
        });

        holder.itemView.setOnClickListener(view -> {
            if (onClick != null) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
