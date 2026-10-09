package com.example.cartly;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartly.ApiService.AuthApiService;
import com.example.cartly.RequestDao.RegisterRequestDao;
import com.example.cartly.ResponseDao.AuthResponseDao;
import com.example.cartly.Util.ApiCallback;
import com.example.cartly.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private boolean isPasswordVisibility = false;
    private boolean isRePasswordVisibility = false;
    private boolean privacy = false;

    private ActivitySignUpBinding signUpBinding;
    private final AuthApiService authApiService = new AuthApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen.installSplashScreen(this);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponent();
    }

    private void initComponent() {
        doLogin();
        doSignUp();
    }

    private void doLogin() {
        signUpBinding.tvBtnLogin.setOnClickListener(view -> {
            Intent intentLogin = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            finish();
        });
    }

    private void doSignUp() {
        signUpBinding.mvBtnOpenPassword.setOnClickListener(view -> {
            if (isPasswordVisibility) {
                //Close Password
                signUpBinding.edtEnterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                signUpBinding.mvBtnOpenPassword.setImageResource(R.drawable.icon_eye_slash);
                isPasswordVisibility = false;
            } else {
                //Open Password
                signUpBinding.edtEnterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                signUpBinding.mvBtnOpenPassword.setImageResource(R.drawable.icon_eye_open);
                isPasswordVisibility = true;
            }
            signUpBinding.edtEnterPassword.setSelection(signUpBinding.edtEnterPassword.getText().length());
        });

        signUpBinding.mvBtnOpenPasswordConfirm.setOnClickListener(view -> {
            if (isRePasswordVisibility) {
                //Close Password
                signUpBinding.edtReEnterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                signUpBinding.mvBtnOpenPasswordConfirm.setImageResource(R.drawable.icon_eye_slash);
                isRePasswordVisibility = false;
            } else {
                //Open Password
                signUpBinding.edtReEnterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                signUpBinding.mvBtnOpenPasswordConfirm.setImageResource(R.drawable.icon_eye_open);
                isRePasswordVisibility = true;
            }
            signUpBinding.edtReEnterPassword.setSelection(signUpBinding.edtReEnterPassword.getText().length());
        });

        signUpBinding.btnSignUpLayout.setOnClickListener(view -> {

            String email = signUpBinding.edtEnterEmail.getText().toString().trim();
            String first_name = signUpBinding.edtEnterFirstName.getText().toString().trim();
            String last_name = signUpBinding.edtEnterLastName.getText().toString().trim();
            String username = signUpBinding.edtEnterUsername.getText().toString().trim();
            String phone = signUpBinding.edtEnterPhone.getText().toString().trim();
            String password = signUpBinding.edtEnterPassword.getText().toString().trim();
            String re_password = signUpBinding.edtReEnterPassword.getText().toString().trim();

            // Check Email
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signUpBinding.warningEmailLayout.setVisibility(View.VISIBLE);
                signUpBinding.edtEnterEmail.requestFocus();
            } else {
                signUpBinding.warningEmailLayout.setVisibility(View.GONE);
            }

            // Check First Name
            if (first_name.isEmpty()) {
                signUpBinding.warningFirstNameLayout.setVisibility(View.VISIBLE);
                signUpBinding.edtEnterFirstName.requestFocus();
            } else {
                signUpBinding.warningFirstNameLayout.setVisibility(View.GONE);
            }

            // Check Last Name
            if (last_name.isEmpty()) {
                signUpBinding.warningLastNameLayout.setVisibility(View.VISIBLE);
                signUpBinding.edtEnterLastName.requestFocus();
            } else {
                signUpBinding.warningLastNameLayout.setVisibility(View.GONE);
            }

            // Check Username
            if (username.isEmpty()) {
                signUpBinding.warningUsernameLayout.setVisibility(View.VISIBLE);
                signUpBinding.edtEnterUsername.requestFocus();
            } else {
                signUpBinding.warningUsernameLayout.setVisibility(View.GONE);
            }

            // Check Phone
            if (phone.isEmpty()) {
                signUpBinding.warningPhoneLayout.setVisibility(View.VISIBLE);
                signUpBinding.edtEnterPhone.requestFocus();
            } else {
                signUpBinding.warningPhoneLayout.setVisibility(View.GONE);
            }

            // Check Password
            if (password.isEmpty()) {
                signUpBinding.warningPasswordLayout.setVisibility(View.VISIBLE);
                signUpBinding.enterPasswordLayout.requestFocus();
            } else {
                signUpBinding.warningPasswordLayout.setVisibility(View.GONE);
            }

            // Check Confirm Password
            if (re_password.isEmpty()) {
                signUpBinding.warningConfirmPasswordLayout.setVisibility(View.VISIBLE);
                signUpBinding.enterPasswordConfirmLayout.requestFocus();
            } else {
                signUpBinding.warningConfirmPasswordLayout.setVisibility(View.GONE);
            }

            // Check Privacy
            if (signUpBinding.checkboxPolicy.isChecked()) {
                privacy = true;
            } else {
                privacy = false;
            }

            if (!email.isEmpty() && !first_name.isEmpty() && !last_name.isEmpty()
                    && !username.isEmpty() && !phone.isEmpty()
                    && !password.isEmpty() && !re_password.isEmpty() && privacy) {
                if (password.equals(re_password)) {
                    signUpBinding.alertSignUpLayout.setVisibility(View.VISIBLE);
                    signUpBinding.alertSignUp.progressBar.setVisibility(View.VISIBLE);
                    signUpBinding.alertSignUp.alertSignUpSuccess.setVisibility(View.GONE);
                    signUpBinding.alertSignUp.alertSignUpFailed.setVisibility(View.GONE);
                    alertSignUpStatus(true);

                    // เรียก POST /api/auth/register ผ่าน AuthApiService
                    RegisterRequestDao request = new RegisterRequestDao(
                            username, first_name, last_name, email, password, phone.isEmpty() ? null : phone
                    );
                    authApiService.register(request, new ApiCallback<AuthResponseDao>() {
                        @Override
                        public void onSuccess(AuthResponseDao data, String message) {
                            if (isFinishing() || isDestroyed()) return;
                            alertSignUpStatus(true);
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String message, int httpCode) {
                            if (isFinishing() || isDestroyed()) return;
                            alertSignUpStatus(false);
                        }
                    });
                } else {
                    alertSignUpStatus(false);
                }
            } else {
                signUpBinding.alertSignUpLayout.setVisibility(View.GONE);
            }
        });

        signUpBinding.alertSignUp.btnLogInAgainLayout.setOnClickListener(view1 -> {
            Intent intentLogIn = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intentLogIn);
            finish();
        });
        signUpBinding.alertSignUp.btnTryAgainLayout.setOnClickListener(
                view1 -> signUpBinding.alertSignUpLayout.setVisibility(View.GONE));
        signUpBinding.alertSignUp.tvBtnLoginWarning.setOnClickListener(view1 -> {
            Intent intentLogIn = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intentLogIn);
            finish();
        });
    }

    private void alertSignUpStatus (boolean alertStatus) {
        if (alertStatus) {
            signUpBinding.alertSignUpLayout.setVisibility(View.VISIBLE);
            signUpBinding.alertSignUp.progressBar.setVisibility(View.GONE);
            signUpBinding.alertSignUp.alertSignUpSuccess.setVisibility(View.VISIBLE);
            signUpBinding.alertSignUp.alertSignUpFailed.setVisibility(View.GONE);
        } else {
            signUpBinding.alertSignUpLayout.setVisibility(View.VISIBLE);
            signUpBinding.alertSignUp.progressBar.setVisibility(View.GONE);
            signUpBinding.alertSignUp.alertSignUpSuccess.setVisibility(View.GONE);
            signUpBinding.alertSignUp.alertSignUpFailed.setVisibility(View.VISIBLE);
        }
    }
}