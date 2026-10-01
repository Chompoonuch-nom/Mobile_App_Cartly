package com.example.cartly.RequestDao;

/** ตรงกับ ReviewCreate (schemas.py) — ใช้กับ POST /api/products/{id}/reviews */
public class ReviewRequestDao {
    private int rating;      // 1-5
    private String review_comment;  // optional ส่ง null ได้

    public ReviewRequestDao(int rating, String review_comment) {
        this.rating = rating;
        this.review_comment = review_comment;
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
}
