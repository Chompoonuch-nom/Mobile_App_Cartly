package com.example.cartly.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartly.ResponseDao.ReviewDao;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.ItemReviewBinding;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    public interface Listener {
        void onDeleteClick(ReviewDao review);
    }

    private final List<ReviewDao> items = new ArrayList<>();
    private final Listener listener;

    public ReviewAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<ReviewDao> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void removeById(long reviewId) {
        for (int i = 0; i < items.size(); i++) {
            items.remove(i);
            notifyItemRemoved(i);
            return;
        }
    }
    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ReviewDao review = items.get(position);
        holder.binding.ratingBar.setRating(review.getRating());
        holder.binding.tvReviewComment.setText(review.getReview_comment() == null || review.getReview_comment().isEmpty() ? "(ไม่มีข้อความ)" : review.getReview_comment());
        holder.binding.tvReviewDate.setText(review.getCreated_at() != null && review.getCreated_at().length() >= 10 ? review.getCreated_at().substring(0,10) : "");

        // แสดงปุ่มลบเฉพาะรีวิวของผู้ใช้ที่ login อยู่เอง
        boolean isOwner = TokenManager.isLoggedIn() && review.getUser_id() == TokenManager.getUserId();
        holder.binding.btnDeleteReview.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        holder.binding.btnDeleteReview.setOnClickListener(view -> listener.onDeleteClick(review));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReviewBinding binding;
        public ViewHolder(@NonNull ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
