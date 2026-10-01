package com.example.cartly.RequestDao;

/** ตรงกับ CategoryCreate (schemas.py) — ใช้กับ POST/PUT /api/categories (admin เท่านั้น) */
public class CategoryRequestDao {
    private String category_name;
    private String category_descp;
    private String icon_url;

    public CategoryRequestDao(String category_name, String category_descp, String icon_url) {
        this.category_name = category_name;
        this.category_descp = category_descp;
        this.icon_url = icon_url;
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
