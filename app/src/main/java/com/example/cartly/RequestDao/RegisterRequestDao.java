package com.example.cartly.RequestDao;

/** ตรงกับ RegisterRequest (schemas.py) — ใช้กับ POST /api/auth/register */
public class RegisterRequestDao {
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String password_hash;
    private String phone_number; // optional ส่ง null ได้ถ้าไม่มี

    public RegisterRequestDao(String username, String first_name, String last_name, String email, String password_hash, String phone_number) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password_hash = password_hash;
        this.phone_number = phone_number;
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

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
