package com.example.cartly.ResponseDao;

// ตรงกับ AuthResponse (schemas.py) — ผลลัพธ์จาก POST /api/auth/register และ /api/auth/login

public class AuthResponseDao {
    private String token;
    private long user_id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String user_role;

    public AuthResponseDao(String token, long user_id, String username, String first_name, String last_name, String email, String user_role) {
        this.token = token;
        this.user_id = user_id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.user_role = user_role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}
