package com.example.cartly;

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
import com.example.cartly.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private boolean userStatus = true;


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

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        userStatus = getIntent().getBooleanExtra("User",true);
        initComponent();

    }

    private void initComponent() {
        setBottomNavigation();
    }

    private void setBottomNavigation() {
        if (userStatus) {
            mainBinding.customerBottomNavigationView.setVisibility(View.VISIBLE);
            mainBinding.adminBottomNavigationView.setVisibility(View.GONE);
            mainBinding.customerBottomNavigationView.setOnItemSelectedListener(menuItem -> {
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
        } else {
            mainBinding.customerBottomNavigationView.setVisibility(View.GONE);
            mainBinding.adminBottomNavigationView.setVisibility(View.VISIBLE);
            mainBinding.adminBottomNavigationView.setOnItemSelectedListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.homePage) {
                    loadFragment(new HomeFragment());
                    return true;
                } else if (id == R.id.storePage) {
                    loadFragment(new StoreFragment());
                    return true;
                } else if (id == R.id.profilePage) {
                    loadFragment(new ProfileFragment());
                    return true;
                }
                return false;
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}