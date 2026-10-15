package com.example.cartly.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartly.R;
import com.example.cartly.ResponseDao.ProductDao;
import com.example.cartly.databinding.ItemProductBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * แสดงรายการสินค้าแบบ grid/list - ใช้ร่วมกันทั้งหน้า Home และหน้า "จัดการสินค้า" (admin)
 * ส่ง showAdminAction = true เพื่อโชว์ปุ่มแก้ไข/ลบใต้การ์ดสินค้าแต่ละใบ
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public interface Listener {
        void onProductClick(ProductDao product);
        void onEditClick(ProductDao product);
        void onDeleteClick(ProductDao product);
    }
    private final List<ProductDao> items = new ArrayList<>();
    private final Listener listener;
    private final boolean showAdminAction;
    public ProductAdapter(Listener listener, boolean showAdminAction) {
        this.listener = listener;
        this.showAdminAction = showAdminAction;
    }
    public void setItems(List<ProductDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void appendItems(List<ProductDao> more) {
        if (more == null || more.isEmpty()) return;
        int start = items.size();
        items.addAll(more);
        notifyItemRangeChanged(start, more.size());
    }
    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductDao product = items.get(position);
        holder.binding.tvNameProduct.setText(product.getProduct_name());
        holder.binding.tvPriceProduct.setText("฿" + product.getPrice().toPlainString());
        holder.binding.tvStockProduct.setText("คงเหลือ: " + product.getStock_qty());

        String imageUrl = product.getPrimaryImageURL();
        if (imageUrl == null || imageUrl.isEmpty()) {
            // กันรูปเก่าค้างตอน view ถูก recycle
            Glide.with(holder.binding.mvProduct).clear(holder.binding.mvProduct);
            holder.binding.mvProduct.setImageResource(R.drawable.icon_imagesmode_24);
        } else {
            Glide.with(holder.binding.mvProduct)
                    .load(imageUrl)
                    .placeholder(R.drawable.icon_imagesmode_24)
                    .error(R.drawable.icon_imagesmode_24)
                    .centerCrop()
                    .into(holder.binding.mvProduct);
        }

        holder.itemView.setOnClickListener(view -> listener.onProductClick(product));
        if (showAdminAction) {
            holder.binding.adminActionLayout.setVisibility(View.VISIBLE);
            holder.binding.mvBtnEditProduct.setOnClickListener(view -> listener.onEditClick(product));
            holder.binding.mvBtnDeleteProduct.setOnClickListener(view -> listener.onDeleteClick(product));
        } else {
            holder.binding.adminActionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemProductBinding binding;
        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
