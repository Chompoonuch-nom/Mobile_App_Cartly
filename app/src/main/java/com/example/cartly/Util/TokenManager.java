package com.example.cartly.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * เก็บ/อ่าน JWT token และข้อมูล session พื้นฐานใน SharedPreferences
 * ต้องเรียก TokenManager.init(this) ครั้งเดียวใน CartlyApplication.onCreate()
 */

public class TokenManager {
    private static final String PREF_NAME = "cartly_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";

    private static SharedPreferences prefs;
    public static void init(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public static void saveSession(String token, long userId, String role) {
        if (prefs == null) return;
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_ROLE, role)
                .apply();
    }
    public static String getToken() {
        return prefs == null ? null : prefs.getString(KEY_TOKEN, null);
    }
    public static long getUserId() {
        return prefs == null ? -1 : prefs.getLong(KEY_USER_ID, -1);
    }
    public static String getRole() {
        return prefs == null ? null : prefs.getString(KEY_ROLE, null);
    }
    public static boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }
    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getRole());
    }
    public static void clear() {
        if (prefs != null) prefs.edit().clear().apply();
    }
}
