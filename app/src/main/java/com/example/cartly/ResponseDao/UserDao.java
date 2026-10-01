package com.example.cartly.ResponseDao;

/**
 * ตรงกับ UserOut (schemas.py) — ข้อมูลผู้ใช้ที่ API ส่งกลับ (ไม่มี password_hash ติดมาด้วย)
 * ใช้กับ GET /api/users/me
 */

public class UserDao {
    private long user_id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;   // อาจเป็น null
    private String avatar_url;     // อาจเป็น null
    private String user_role;           // "CUSTOMER" หรือ "ADMIN"
    private String created_at;     // ISO-8601 string เช่น "2026-09-28T10:00:00"

    public UserDao(long user_id, String username, String first_name, String last_name, String email, String phone_number, String avatar_url, String user_role, String created_at) {
        this.user_id = user_id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.avatar_url = avatar_url;
        this.user_role = user_role;
        this.created_at = created_at;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
