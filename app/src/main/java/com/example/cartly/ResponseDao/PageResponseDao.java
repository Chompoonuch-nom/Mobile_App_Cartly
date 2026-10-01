package com.example.cartly.ResponseDao;

import java.util.List;

/**
 * ใช้กับ endpoint ที่มีการแบ่งหน้า (pagination) เช่น GET /api/products
 * รูปแบบ: { "content": [...], "page": 0, "size": 10, "total_elements": 50, "total_pages": 5 }
 * ตัวอย่างการใช้งานกับ Retrofit:
 *   Call<ApiResponseDao<PageResponseDao<ProductDao>>> getProducts(...);
 */

public class PageResponseDao<T> {
    private List<T> content;
    private int page;
    private int size;
    private int total_elements;
    private int total_pages;

    public PageResponseDao(List<T> content, int page, int size, int total_elements, int total_pages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.total_elements = total_elements;
        this.total_pages = total_pages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal_elements() {
        return total_elements;
    }

    public void setTotal_elements(int total_elements) {
        this.total_elements = total_elements;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
