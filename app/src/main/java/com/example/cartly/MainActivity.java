package com.example.cartly;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cartly.Fragment.CartFragment;
import com.example.cartly.Fragment.HomeFragment;
import com.example.cartly.Fragment.OrderFragment;
import com.example.cartly.Fragment.ProfileFragment;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String KEY_TAB = "select_tab";
    private int currentTabId = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call Splashscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.bottomNav.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (requireLogin(id) && !TokenManager.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                return false; // ไม่เปลี่ยน tab
            }
            showTab(id);
            return true;
        });

        int initial = savedInstanceState != null
                ? savedInstanceState.getInt(KEY_TAB, R.id.nav_home) : R.id.nav_home;
        if (requireLogin(initial) && !TokenManager.isLoggedIn()) initial = R.id.nav_home;
        selectTab(initial);

        // ปุ่ม Back: ถ้าไม่ได้อยู่หน้าแรกให้กลับหน้าแรกก่อน แล้วค่อยออกจากแอป
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentTabId != R.id.nav_home) {
                    selectTab(R.id.nav_home);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TAB, currentTabId);
    }

    // --------Tabs
    /** สลับ tab จากโค้ด (เช่น ปุ่ม "ประวัติคำสั่งซื้อ" ในหน้าโปรไฟล์) */
    public void selectTab(int tabId) {
        if (requireLogin(tabId) && !TokenManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        binding.bottomNav.getMenu().findItem(tabId).setChecked(true);
        showTab(tabId);
    }

    private boolean requireLogin(int tabId) {
        return tabId != R.id.nav_home;
    }

    private void showTab(int tabId) {
        currentTabId = tabId;
        binding.toolbar.setTitle(titleFor(tabId));

        FragmentManager fm = getSupportFragmentManager();
        String targetTag = tagFor(tabId);
        FragmentTransaction tx = fm.beginTransaction().setReorderingAllowed(true);

        for (int id : new int[]{R.id.nav_home, R.id.nav_cart, R.id.nav_orders, R.id.nav_profile}) {
            Fragment f = fm.findFragmentByTag(tagFor(id));
            if (f != null && id != tabId) tx.hide(f);
        }

        Fragment target = fm.findFragmentByTag(targetTag);
        if (target == null) {
            tx.add(binding.fragmentContainer.getId(), createFragment(tabId), targetTag);
        } else {
            tx.show(target);
        }
        tx.commit();
    }

    private Fragment createFragment(int tabId) {
        if (tabId == R.id.nav_cart) return new CartFragment();
        if (tabId == R.id.nav_orders) return new OrderFragment();
        if (tabId == R.id.nav_profile) return new ProfileFragment();
        return new HomeFragment();
    }

    private String tagFor(int tabId) {
        if (tabId == R.id.nav_cart) return "tab_cart";
        if (tabId == R.id.nav_orders) return "tab_orders";
        if (tabId == R.id.nav_profile) return "tab_profile";
        return "tab_home";
    }

    private String titleFor(int tabId) {
        if (tabId == R.id.nav_cart) return "ตะกร้าสินค้า";
        if (tabId == R.id.nav_orders) return "คำสั่งซื้อของฉัน";
        if (tabId == R.id.nav_profile) return "โปรไฟล์ของฉัน";
        return "Cartly";
    }

    /** Fragment เรียกเมื่อ API ตอบ 401 (token หมดอายุ/ไม่ถูกต้อง) -> ล้าง session แล้วกลับหน้า Login */
    public void onSessionExpire() {
        TokenManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (TokenManager.isAdmin()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item != null && item.getItemId() == R.id.action_add_product) {
            startActivity(new Intent(this, ProductFormActivity.class));
            return true;
        }
        if (item != null && item.getItemId() == R.id.action_manage_categories) {
            startActivity(new Intent(this, CategoryManageActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}