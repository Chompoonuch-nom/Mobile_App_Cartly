package com.example.cartly;

import android.app.Application;

import com.example.cartly.Util.TokenManager;

/**
 * ต้องประกาศใน AndroidManifest: <application android:name=".CartlyApplication" ...>
 */
public class CartlyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TokenManager.init(this); // ต้องทำก่อนมีการยิง API ครั้งแรก
    }
}
