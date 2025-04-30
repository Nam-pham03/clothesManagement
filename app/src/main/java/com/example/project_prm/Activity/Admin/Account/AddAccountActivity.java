package com.example.project_prm.Activity.Admin.Account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Entities.Product;
import com.example.project_prm.Entities.Role;
import com.example.project_prm.Entities.User;
import com.example.project_prm.R;
import com.example.project_prm.Utils.ImageUtils;
import com.example.project_prm.ViewModel.Admin.ManageAccountViewModel;
import com.example.project_prm.ViewModel.Admin.ManageRoleViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAccountActivity extends AppCompatActivity {

    private EditText etUserName, etPhone, etMail, etAddress, etPassword;
    private Spinner spinnerRole;
    private Button btnSaveUser, btnUploadImage,btnRemoveImage;
    private ImageView imgUser;
    private ManageAccountViewModel manageAccountViewModel;
    private ManageRoleViewModel manageRoleViewModel;
    private List<Role> roleList = new ArrayList<>();
    private ArrayAdapter<String> roleAdapter;
    private int selectedRoleId = -1;
    private String imagePath = null;

    private ImageView imgTogglePassword;
    private boolean isPasswordVisible = false;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        etUserName = findViewById(R.id.etUserName);
        etPhone = findViewById(R.id.etPhone);
        etMail = findViewById(R.id.etGmail);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSaveUser = findViewById(R.id.btnSaveUser);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        imgUser = findViewById(R.id.imgUser);
        imgTogglePassword = findViewById(R.id.imgTogglePassword);

        imgTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Ẩn mật khẩu
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgTogglePassword.setImageResource(R.drawable.ic_visible_off);
                } else {
                    // Hiển thị mật khẩu
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgTogglePassword.setImageResource(R.drawable.ic_visible);
                }
                isPasswordVisible = !isPasswordVisible;
                etPassword.setSelection(etPassword.getText().length());
            }
        });
        manageAccountViewModel = new ViewModelProvider(this).get(ManageAccountViewModel.class);
        manageRoleViewModel = new ViewModelProvider(this).get(ManageRoleViewModel.class);
        btnSaveUser.setOnClickListener(v -> saveUser());
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        imgUser.setImageURI(imageUri);

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            String fileName = "account_" + System.currentTimeMillis() + ".png";
                            imagePath = ImageUtils.saveImageToInternalStorage(this, bitmap, fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        btnUploadImage.setOnClickListener(v -> openFileChooser());



        btnUploadImage.setOnClickListener(v -> {
            openFileChooser();

        });



        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        loadRoles();


    }

    private void loadRoles(){
        manageRoleViewModel.getAllRoles().observe(this, roles -> {
            roleList = roles;
            List<String> roleName = new ArrayList<>();
            for(Role role : roles){
                roleName.add(role.getName());
            }
            roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleName);
            roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(roleAdapter);

        });
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void saveUser() {
        String username = etUserName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String mail = etMail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || phone.isEmpty() || mail.isEmpty() || address.isEmpty() || password.isEmpty() ) {
            Toast.makeText(this, "Please enter require field!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            etMail.setError("Please enter a valid email address");
            etMail.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(phone).matches() || !phone.matches("^[0-9]{9,15}$")) {
            etPhone.setError("Please enter a valid phone number (9-15 digits)");
            etPhone.requestFocus();
            return;
        }


        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        int selectedPosition = spinnerRole.getSelectedItemPosition();
        if (selectedPosition >= 0 && selectedPosition < roleList.size()) {
            selectedRoleId = roleList.get(selectedPosition).getId();
        }

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        User newUser = new User(
                username, password, phone, mail, address,imagePath, selectedRoleId,
                 currentTime, "","", 0);
        manageAccountViewModel.insert(newUser);

        Toast.makeText(this, "Add successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}