package com.example.cartly.ResponseDao;

/**
 * ตรงกับ CategoryOut (schemas.py)
 * ใช้กับ GET /api/categories, GET /api/categories/{id}
 */

public class CategoryDao {
    private long category_id;
    private String category_name;
    private String category_descp;   // อาจเป็น null
    private String icon_url;   // อาจเป็น null

    public CategoryDao(long category_id, String category_name, String category_descp, String icon_url) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_descp = category_descp;
        this.icon_url = icon_url;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_descp() {
        return category_descp;
    }

    public void setCategory_descp(String category_descp) {
        this.category_descp = category_descp;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
