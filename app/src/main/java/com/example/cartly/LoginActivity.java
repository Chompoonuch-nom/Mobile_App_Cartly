package com.example.cartly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.cartly.ApiService.AuthApiService;
import com.example.cartly.RequestDao.LoginRequestDao;
import com.example.cartly.ResponseDao.ApiResponseDao;
import com.example.cartly.ResponseDao.AuthResponseDao;
import com.example.cartly.ResponseDao.LoginResponseDao;
import com.example.cartly.Retrofit.RetrofitAuthService;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivityLoginBinding;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * เรียก POST /api/auth/login ผ่าน AuthApiService
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private boolean isPasswordVisible = false;
    private Boolean user = null;
    private final int STATUS_LOADING = 0;
    private final int STATUS_SUCCESS = 1;
    private final int STATUS_FAILED = 2;
    private final AuthApiService authApiService = new AuthApiService();
    private SharedPreferences sharedPreferences;

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
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.hide(WindowInsetsCompat.Type.systemBars());

        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
    }
    private void initComponent() {
        doLogin();
        signUp();
    }

    // เรียก API ตอนผู้ใช้กดปุ่ม (action) ไม่ใช่ใน onCreate
    private void doLogin() {
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

            String email = loginBinding.edtEnterEmail.getText().toString();
            String password = loginBinding.edtEnterPassword.getText().toString();

            loginBinding.tvWarningUserName.setText(R.string.warning_enter_email_en);
            loginBinding.tvWarningPassword.setText(R.string.warning_enter_password_en);

            loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.VISIBLE);
            loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.GONE);
            loginBinding.alertLogin.alertLogInFailed.setVisibility(View.GONE);

            if (!email.isEmpty() && !password.isEmpty()) {
                loginBinding.warningEmailLayout.setVisibility(View.GONE);
                loginBinding.warningPasswordLayout.setVisibility(View.GONE);

                loginBinding.alertLogInLayout.setVisibility(View.VISIBLE);
                loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.VISIBLE);
                loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.GONE);
                loginBinding.alertLogin.alertLogInFailed.setVisibility(View.GONE);

                authApiService.login(new LoginRequestDao(email, password), new ApiCallback<AuthResponseDao>() {
                    @Override
                    public void onSuccess(AuthResponseDao data, String message) {
                        if (isFinishing() || isDestroyed()) return;
                        alertLoginStatus(true);

                        loginBinding.alertLogin.btnLetsEnjoyLayout.setOnClickListener(v -> {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }

                    @Override
                    public void onError(String message, int httpCode) {
                        if (isFinishing() || isDestroyed()) return;
                        alertLoginStatus(false);
                        Log.e("API_ERROR", "Error: "+ message);
                    }
                });
            } else {
                if (email.isEmpty() && password.isEmpty()) {
                    loginBinding.warningEmailLayout.setVisibility(View.VISIBLE);
                    loginBinding.warningPasswordLayout.setVisibility(View.VISIBLE);
                } else if (email.isEmpty()) {
                    loginBinding.warningEmailLayout.setVisibility(View.VISIBLE);
                    loginBinding.warningPasswordLayout.setVisibility(View.GONE);
                } else {
                    loginBinding.warningEmailLayout.setVisibility(View.GONE);
                    loginBinding.warningPasswordLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void alertLoginStatus (boolean statusAlert) {
        if (statusAlert) {
            loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.GONE);
            loginBinding.alertLogInLayout.setVisibility(View.VISIBLE);
            loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.VISIBLE);
            loginBinding.alertLogin.alertLogInFailed.setVisibility(View.GONE);
            loginBinding.alertLogin.btnLetsEnjoyLayout.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            });
        } else {
            loginBinding.alertLogin.progressBarAlertLogin.setVisibility(View.GONE);
            loginBinding.alertLogInLayout.setVisibility(View.VISIBLE);
            loginBinding.alertLogin.alertLogInSuccessfully.setVisibility(View.GONE);
            loginBinding.alertLogin.alertLogInFailed.setVisibility(View.VISIBLE);
            loginBinding.alertLogin.btnLogInAgainLayout.setOnClickListener(
                    v -> loginBinding.alertLogInLayout.setVisibility(View.GONE));

            loginBinding.alertLogin.btnForgotPassword.setOnClickListener(v -> {
                Toast.makeText(LoginActivity.this,R.string.forgot_your_password_en,Toast.LENGTH_LONG).show();
                loginBinding.alertLogInLayout.setVisibility(View.GONE);
            });
        }
    }

    private void signUp() {
        loginBinding.btnSignUp.setOnClickListener(view -> {
//            Toast.makeText(LoginActivity.this,R.string.sign_up_en,Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });
    }
}