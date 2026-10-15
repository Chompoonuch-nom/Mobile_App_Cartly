package com.example.cartly.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface Listener {
        void onEditClick(CategoryDao category);
        void onDeleteClick(CategoryDao category);
    }

    private final List<CategoryDao> items = new ArrayList<>();
    private final Listener listener;

    public CategoryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<CategoryDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryDao categoey = items.get(position);
        holder.binding.tvCategoryName.setText(categoey.getCategory_name());
        holder.binding.tvCategoryDes.setText(categoey.getCategory_descp());
        holder.binding.btnEditCategory.setOnClickListener(view -> listener.onEditClick(categoey));
        holder.binding.btnDeleteCategory.setOnClickListener(view -> listener.onDeleteClick(categoey));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemCategoryBinding binding;
        public ViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
