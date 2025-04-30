package com.example.project_prm.Activity.User.Profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.UserViewModel;

public class PasswordChangeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText etEmail, etOldPassword, etNewPassword, etConfirmPassword;
    private ImageButton btnShowNewPassword, btnShowConfirmPassword;
    private Button btnSave;
    private ImageButton btnBack;
    private int userId;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userId = getIntent().getIntExtra("USER_ID", -1);

        initializeComponents();
        setupObservers();
        setupButtonListeners();
    }

    private void initializeComponents() {
        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnShowNewPassword = findViewById(R.id.btnShowNewPassword);
        btnShowConfirmPassword = findViewById(R.id.btnShowConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupObservers() {
        userViewModel.getPasswordChangeResult().observe(this, result -> {
            if (result.isSuccess()) {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getUserProfile().observe(this, userProfile -> {
            if (userProfile != null && userProfile.getEmail() != null) {
                etEmail.setText(userProfile.getEmail());
            }
        });

        if (userId != -1) {
            userViewModel.fetchUserProfile(userId);
        }
    }

    private void setupButtonListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnShowNewPassword.setOnClickListener(v -> togglePasswordVisibility(etNewPassword, isNewPasswordVisible = !isNewPasswordVisible));

        btnShowConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(etConfirmPassword, isConfirmPasswordVisible = !isConfirmPasswordVisible));

        btnSave.setOnClickListener(v -> attemptChangePassword());
    }

    private void togglePasswordVisibility(EditText editText, boolean makeVisible) {
        if (makeVisible) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }

    private void attemptChangePassword() {
        String email = etEmail.getText().toString().trim();
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(oldPassword)) {
            etOldPassword.setError("Current password is required");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("New password is required");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password is required");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            return;
        }

        userViewModel.changePassword(userId, email, oldPassword, newPassword);
    }
}