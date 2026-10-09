package com.example.cartly;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.cartly.Fragment.CartFragment;
import com.example.cartly.Fragment.HomeFragment;
import com.example.cartly.Fragment.ProfileFragment;
import com.example.cartly.Fragment.StoreFragment;
import com.example.cartly.Util.TokenManager;
import com.example.cartly.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call Splashscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!TokenManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        initComponent();

    }

    private void initComponent() {
        setBottomNavigation();
    }

    private void setBottomNavigation() {
        mainBinding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.homePage) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.cartPage) {
                loadFragment(new CartFragment());
                return true;
            } else if (id == R.id.profilePage) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

}