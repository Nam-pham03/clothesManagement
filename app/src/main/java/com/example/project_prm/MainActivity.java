package com.example.project_prm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.User.Cart.CartActivity;
//import com.example.project_prm.Activity.User.Chat.ConstantKey;
import com.example.project_prm.Activity.User.Profile.LoginActivity;
import com.example.project_prm.Activity.User.Shop.ProductListActivity;

import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Database.DatabaseHelper;
import com.example.project_prm.Entities.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.zegocloud.zimkit.services.ZIMKit;

import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USER_ID = "user_id";
    private int userId = -1;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (!isUserLoggedIn()) {
//            redirectToLogin();
//            return;
//        }
        DatabaseHelper.copyDatabaseFromAssets(this);
//        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.content_frame));

        if (getIntent().hasExtra("USER_ID")) {
            userId = getIntent().getIntExtra("USER_ID", -1);
            saveUserIdToPreferences(userId);
        } else {
            userId = getUserIdFromPreferences();
        }
//        initZegocloud();
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();

    }
//    public void initZegocloud() {
//        ZIMKit.initWith(this.getApplication(), ConstantKey.appID, ConstantKey.appSign);
//        ZIMKit.initNotifications();
//    }




    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_REMEMBER, false) ||
                getIntent().hasExtra("USER_ID");
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUserIdToPreferences(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    private int getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

}

