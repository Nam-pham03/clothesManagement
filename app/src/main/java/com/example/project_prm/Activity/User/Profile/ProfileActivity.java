package com.example.project_prm.Activity.User.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.UserViewModel;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private ImageButton  btnSettings, btnCamera, btnBack;
    private ImageView ivProfilePicture;
    private EditText etFileUpload, etName, etEmail, etPhoneNumber, etZipCode;
    private Button btnBrowse, btnSave, btnChangePassword;
    private TextView tvUserId;
    private File profilePictureFile;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userId = getCurrentUserId();

        initializeComponents();

        setupButtonListeners();

        observeProfileResults();

        userViewModel.fetchUserProfile(userId);
        observeUserProfile();
    }

    @SuppressLint("SetTextI18n")
    private void initializeComponents() {
        btnBack = findViewById(R.id.btnBack);
        btnSettings = findViewById(R.id.btnSettings);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnCamera = findViewById(R.id.btnCamera);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvUserId = findViewById(R.id.tvUserId);
        etFileUpload = findViewById(R.id.etFileUpload);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etZipCode = findViewById(R.id.etZipCode);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnSave = findViewById(R.id.btnSave);
        tvUserId.setText("UserID: " + userId);
    }

    private void setupButtonListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSettings.setOnClickListener(v -> Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show());
        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });
        btnBrowse.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            browseFile.launch(intent);
        });
        btnSave.setOnClickListener(v -> onSaveButtonClicked());
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PasswordChangeActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });



    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        ivProfilePicture.setImageURI(selectedImageUri);

                        String realPath = getRealPathFromURI(selectedImageUri);
                        if (realPath != null) {
                            profilePictureFile = new File(realPath);
                        } else {
                            Toast.makeText(this, "Không thể lấy đường dẫn hình ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }


    private final ActivityResultLauncher<Intent> browseFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData();
                    assert fileUri != null;
                    etFileUpload.setText(fileUri.getPath());
                }
            }
    );

    private void onSaveButtonClicked() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String zipCode = etZipCode.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = "";
        if (profilePictureFile != null && profilePictureFile.exists()) {
            imagePath = profilePictureFile.getAbsolutePath();
        }
        userViewModel.updateProfile(userId, name, email, phoneNumber, zipCode, imagePath);
    }

    private void observeProfileResults() {
        userViewModel.getProfileResult().observe(this, profileResult -> {
            if (profileResult != null) {
                if (profileResult.isSuccess()) {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, profileResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void observeUserProfile() {
        userViewModel.getUserProfile().observe(this, userProfile -> {
            if (userProfile != null) {
                if (userProfile.getUsername() != null && !userProfile.getUsername().isEmpty()) {
                    tvUserId.setText(userProfile.getUsername());
                    etName.setText(userProfile.getUsername());
                } else {
                    tvUserId.setText("Unknown User");
                }

                if (userProfile.getEmail() != null && !userProfile.getEmail().isEmpty()) {
                    etEmail.setText(userProfile.getEmail());
                }
                if (userProfile.getPhone() != null && !userProfile.getPhone().isEmpty()) {
                    etPhoneNumber.setText(userProfile.getPhone());
                }
                if (userProfile.getAddress() != null && !userProfile.getAddress().isEmpty()) {
                    etZipCode.setText(userProfile.getAddress());
                }
                if (userProfile.getImage() != null && !userProfile.getImage().isEmpty()) {
                    File imgFile = new File(userProfile.getImage());
                    if (imgFile.exists()) {
                        try {
                            Uri imageUri = Uri.fromFile(imgFile);
                            ivProfilePicture.setImageURI(imageUri);
                        } catch (Exception e) {
                            Log.e("ProfileActivity", "Error loading profile image: " + e.getMessage());
                        }
                    }
                }
                Log.d("ProfileActivity", "User profile loaded: " +
                        "username=" + userProfile.getUsername() +
                        ", email=" + userProfile.getEmail() +
                        ", phone=" + userProfile.getPhone() +
                        ", address=" + userProfile.getAddress());
            } else {
                Log.e("ProfileActivity", "User profile is null");
                tvUserId.setText("Unknown User");
            }
        });
    }


    private int getCurrentUserId() {
        return getIntent().getIntExtra("USER_ID", -1);
    }
}