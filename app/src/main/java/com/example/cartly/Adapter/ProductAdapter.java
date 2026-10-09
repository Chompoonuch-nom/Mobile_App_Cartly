package com.example.cartly.Adapter;

import android.content.Context;
import android.location.GnssAntennaInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cartly.Interface.OnClickItemListenerInterface;
import com.example.cartly.Interface.OnClickProductSelectInterface;
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
    private Context context;
//    private OnClickProductSelectInterface onClick;
//    private ArrayList<ProductDao> itemProduct;

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

//    public void setOnClickProductSelectInterface (OnClickProductSelectInterface onClick) {
//        this.onClick = onClick;
//    }
    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductDao item_product = items.get(position);
//        holder.binding.mvProduct.setImageResource(item_product.image);
//        holder.binding.tvNameProduct.setText(item_product.product_name);
//        holder.binding.tvDetailProduct.setText(item_product.product_detail);
//        holder.binding.tvPriceProduct.setText(item_product.product_price);
//
//        holder.binding.mvAddToCart.setOnClickListener(view -> {
//            onClick.onCreateClick(position);
//        });
//
//        holder.itemView.setOnClickListener(view -> {
//            if (onClick != null) {
//                onClick.onItemClick(position);
//            }
//        });

        holder.binding.tvNameProduct.setText(item_product.getProduct_name());
        holder.binding.tvDetailProduct.setText(item_product.getProduct_descp());
        holder.binding.tvPriceProduct.setText("฿" + item_product.getPrice().toPlainString());
        holder.binding.tvStockProduct.setText("คงเหลือ: " + item_product.getStock_qty());
//        holder.binding.mvProduct.setImageResource(R.drawable.add_photo_24); // ใส่ Glide/Picasso โหลด item_product.getPrimaryImageUrl() แทนได้

        holder.itemView.setOnClickListener(view -> listener.onProductClick(item_product));

        if (showAdminAction) {
            holder.binding.adminActionLayout.setVisibility(View.VISIBLE);
            holder.binding.mvBtnEditProduct.setOnClickListener(view -> listener.onEditClick(item_product));
            holder.binding.mvBtnDeleteProduct.setOnClickListener(view -> listener.onDeleteClick(item_product));
        } else {
            holder.binding.adminActionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
