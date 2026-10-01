package com.example.cartly.ResponseDao;

/**
 * ตรงกับ ReviewOut (schemas.py)
 * ใช้กับ GET/POST /api/products/{id}/reviews, DELETE /api/reviews/{id}
 */

public class ReviewDao {
    private long review_id;
    private long user_id;
    private int rating;        // 1-5
    private String review_comment;    // อาจเป็น null
    private String created_at; // ISO-8601 string

    public ReviewDao(long review_id, long user_id, int rating, String review_comment, String created_at) {
        this.review_id = review_id;
        this.user_id = user_id;
        this.rating = rating;
        this.review_comment = review_comment;
        this.created_at = created_at;
    }

    public long getReview_id() {
        return review_id;
    }

    public void setReview_id(long review_id) {
        this.review_id = review_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_comment() {
        return review_comment;
    }

    public void setReview_comment(String review_comment) {
        this.review_comment = review_comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
