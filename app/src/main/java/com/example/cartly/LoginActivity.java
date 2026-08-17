package com.example.cartly;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartly.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call SplashScreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hideSystemUI();
        initComponent();
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
    private void initComponent() {
        login();
        register();
    }

    private void login() {
        loginBinding.btnShowPassword.setOnClickListener(view -> {
            if (isPasswordVisible) {
                //Close Password
                loginBinding.edtEnterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loginBinding.btnShowPassword.setImageResource(R.drawable.icon_eye_slash);
                isPasswordVisible = false;
            } else {
                //Show Password
                loginBinding.edtEnterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loginBinding.btnShowPassword.setImageResource(R.drawable.icon_eye_open);
                isPasswordVisible = true;
            }
            //ให้เคอร์เซอร์อยู่ท้ายสุดของข้อความ
            loginBinding.edtEnterPassword.setSelection(loginBinding.edtEnterPassword.getText().length());
        });

        loginBinding.btnLogInLayout.setOnClickListener(view -> {
            String user_name = "chom";
            String pass = "1111";
            String username = loginBinding.edtEnterUserName.getText().toString();
            String password = loginBinding.edtEnterPassword.getText().toString();

            loginBinding.tvWarningUserName.setText(R.string.warning_enter_username_en);
            loginBinding.tvWarningPassword.setText(R.string.warning_enter_password_en);

            loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.VISIBLE);
            loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.GONE);
            loginBinding.alertLogin.alertLogInFailed.setVisibility(View.GONE);

            if (!username.isEmpty() && !password.isEmpty()) {
                loginBinding.warningUserNameLayout.setVisibility(View.GONE);
                loginBinding.warningPasswordLayout.setVisibility(View.GONE);

                loginBinding.alertLogInLayout.setVisibility(View.VISIBLE);
                loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.GONE);
                loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.VISIBLE);
                loginBinding.alertLogin.alertLogInFailed.setVisibility(View.GONE);

                if (username.equals(user_name) && password.equals(pass)) {
                    loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.GONE);
                    loginBinding.alertLogInLayout.setVisibility(View.VISIBLE);
                    loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.VISIBLE);
                    loginBinding.alertLogin.alertLogInFailed.setVisibility(View.GONE);

                    loginBinding.alertLogin.btnLetsEnjoyLayout.setOnClickListener(v -> {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    });
                } else {
                    loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.GONE);
                    loginBinding.alertLogInLayout.setVisibility(View.VISIBLE);
                    loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.GONE);
                    loginBinding.alertLogin.alertLogInFailed.setVisibility(View.VISIBLE);

                    loginBinding.alertLogin.btnLogInAgainLayout.setOnClickListener(v -> {
                        loginBinding.alertLogInLayout.setVisibility(View.GONE);
                    });

                    loginBinding.alertLogin.btnForgotPassword.setOnClickListener(v -> {
                        Toast.makeText(LoginActivity.this,R.string.forgot_your_password_en,Toast.LENGTH_LONG);
                        loginBinding.alertLogInLayout.setVisibility(View.GONE);
                    });
                }
            } else {
                if (username.isEmpty() && password.isEmpty()) {
                    loginBinding.warningUserNameLayout.setVisibility(View.VISIBLE);
                    loginBinding.warningPasswordLayout.setVisibility(View.VISIBLE);
                } else if (username.isEmpty()) {
                    loginBinding.warningUserNameLayout.setVisibility(View.VISIBLE);
                    loginBinding.warningPasswordLayout.setVisibility(View.GONE);
                } else {
                    loginBinding.warningUserNameLayout.setVisibility(View.GONE);
                    loginBinding.warningPasswordLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void register() {
        loginBinding.btnSignUp.setOnClickListener(view -> {
            Toast.makeText(LoginActivity.this,R.string.sign_up_en,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        });
    }


}