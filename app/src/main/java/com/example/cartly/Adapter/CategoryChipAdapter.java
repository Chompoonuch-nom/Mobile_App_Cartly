package com.example.cartly.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartly.Interface.OnClickItemListenerInterface;
import com.example.cartly.R;
import com.example.cartly.ResponseDao.CategoryDao;
import com.example.cartly.databinding.ItemCategoryChipBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * รายการหมวดหมู่แนวนอน (icon + ชื่อ) สำหรับหน้า Home - ใช้คู่กับ LinearLayoutManager.HORIZONTAL
 * ตำแหน่งแรกเป็นปุ่ม "ทั้งหมด" เสมอ (categoryId = null)
 * แยกจาก CategoryAdapter เดิม เพราะอันนั้นเป็นแนวตั้งพร้อมปุ่มแก้ไข/ลบสำหรับหน้า ADMIN
 */
public class CategoryChipAdapter extends RecyclerView.Adapter<CategoryChipAdapter.ViewHolder> {
    public interface Listener {
        /** @param categoryId null = เลือก "ทั้งหมด" */
        void onCategorySelected(@Nullable Long categoryId);
    }
    private final List<CategoryDao> items = new ArrayList<>();
    private final Listener listener;
    private int selectedPosition = 0; // 0 = "ทั้งหมด"
    public CategoryChipAdapter(Listener listener) {
        this.listener = listener;
    }
    public void setItems(List<CategoryDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        selectedPosition = 0;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryChipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryChipBinding binding = ItemCategoryChipBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryChipAdapter.ViewHolder holder, int position) {
        boolean isAllItem = position == 0;
        CategoryDao category = isAllItem ? null : items.get(position - 1);

        holder.binding.tvTitleCategory.setText(isAllItem ? "ทั้งหมด" : category.getCategory_name());

        if (isAllItem || category.getIcon_url() == null || category.getIcon_url().isEmpty()) {
            Glide.with(holder.binding.mvIconCategory).clear(holder.binding.mvIconCategory); //กันรูปเก่าค้างเมื่อ view ถูก recycle
            holder.binding.mvIconCategory.setImageResource(isAllItem ? R.drawable.icon_shopping_bag_speed_24 : R.drawable.add_photo_24);
        } else {
            Glide.with(holder.binding.mvIconCategory)
                    .load(category.getIcon_url())
                    .placeholder(R.drawable.add_photo_24)
                    .error(R.drawable.add_photo_24)
                    .circleCrop()
                    .into(holder.binding.mvIconCategory);
        }

        holder.itemView.setSelected(position == selectedPosition);
        holder.itemView.setOnClickListener( view -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION || pos == selectedPosition) return;

            int previous = selectedPosition;
            selectedPosition = pos;
            notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);

            listener.onCategorySelected(pos == 0 ? null : items.get(pos - 1).getCategory_id());
        });
    }

    @Override
    public int getItemCount() {
        return items.size() + 1; // +1 = ปุ่ม "ทั้งหมด"
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryChipBinding binding;

        public ViewHolder(@NonNull ItemCategoryChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
