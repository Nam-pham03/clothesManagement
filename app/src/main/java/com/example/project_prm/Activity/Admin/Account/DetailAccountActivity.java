package com.example.project_prm.Activity.Admin.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageAccountViewModel;

import java.io.File;

public class DetailAccountActivity extends AppCompatActivity {

    private ManageAccountViewModel manageAccountViewModel;
    private ImageView imageView;
    private TextView tvUserName, tvPhone, tvMail, tvAddress, tvStatus, tvCreate, tvUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_account);
        imageView = findViewById(R.id.imageViewUserDetail);
        tvUserName = findViewById(R.id.tvUserNameDetail);
        tvPhone = findViewById(R.id.tvPhoneDetail);
        tvMail = findViewById(R.id.tvEmailDetail);
        tvAddress = findViewById(R.id.tvAddressDetail);
        tvStatus = findViewById(R.id.tvStatusDetail);
        tvCreate = findViewById(R.id.tvCreate);
        tvUpdate = findViewById(R.id.tvUpdate);
        manageAccountViewModel = new ViewModelProvider(this).get(ManageAccountViewModel.class);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("ID")){
            int userID = intent.getIntExtra("ID",-1);
            manageAccountViewModel.getUserById(userID).observe(this, user -> {
                if(user!= null){
                     tvUserName.setText("UserName: "+ user.getUsername());
                    tvPhone.setText("Phone: "+ user.getPhone());
                    tvMail.setText("Mail: "+ user.getGmail());
                    tvAddress.setText("Address: "+ user.getAddress());
                    tvUpdate.setText("Update date: " +user.getUpdated_at());
                    tvCreate.setText("Update date: " +user.getCreated_at());
                    if(user.getIsDelete() == 1){
                        tvStatus.setText("Deleted");
                        tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }else{
                        tvStatus.setText("Active");
                        tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    }
                    if (user.getImage() != null && !user.getImage().isEmpty()) {
                        File imgFile = new File(user.getImage());
                        if (imgFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.img_avatar);
                        }
                    } else {
                        imageView.setImageResource(R.drawable.img_avatar);
                    }
                }


            });
        }
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());
    }
}