package com.example.cartly.RequestDao;

public class AvatarRequestDao {
    private String avatar_url;

    public AvatarRequestDao(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatarUrl() { return avatar_url; }
    public void setAvatarUrl(String avatar_url) { this.avatar_url = avatar_url; }
}
