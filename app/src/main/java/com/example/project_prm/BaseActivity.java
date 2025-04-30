package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Activity.Admin.Account.ManageAccountActivity;
import com.example.project_prm.Activity.Admin.Order.ManageOrderActivity;
import com.example.project_prm.Activity.Admin.Product.ManageProductActivity;
import com.example.project_prm.Activity.Admin.Statistic.StatisticActivity;
import com.example.project_prm.Activity.User.Cart.CartActivity;
import com.example.project_prm.Activity.User.ChatBot.ChatbotActivity;
import com.example.project_prm.Activity.User.LiveStream.LiveActivity;

import com.example.project_prm.Activity.User.Profile.LoginActivity;
import com.example.project_prm.Activity.User.Profile.ProfileActivity;
import com.example.project_prm.Activity.User.Shop.OrderHistoryActivity;
import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.Activity.User.VirtualRoom.VirtualRoomActivity;
import com.example.project_prm.ViewModel.User.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private UserViewModel  userViewModel;
    protected BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        updateMenuBasedOnRole(navigationView);

        View headerView = navigationView.getHeaderView(0);
        TextView tvWelcome = headerView.findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome");

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(this, ProductListActivity.class);
            } else if (item.getItemId() == R.id.nav_cart) {
                intent = new Intent(this, CartActivity.class);
            } else if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(this, ProfileActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true; // Xử lý mở/đóng Navigation Drawer
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        long appID = 1417572090;
        String appSign = "ba52a1c32adc615039d095fc50c93d52e1885463edae4433e8d628a5914f2e1b";
        String userID = Build.MANUFACTURER + "_" + generateUserID();
        String userName = userID + "_Name";
        String liveID = "test_live_id";
        if (id == R.id.nav_statistic) {
            startActivity(new Intent(this, StatisticActivity.class));
        } else if (id == R.id.nav_product) {
            startActivity(new Intent(this, ManageProductActivity.class));
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(this, ManageAccountActivity.class));
        } else if (id == R.id.nav_order) {
            startActivity(new Intent(this, ManageOrderActivity.class));
        } else if (id == R.id.nav_profile) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("user_id", -1);

            if (userId != -1) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        } else if (id == R.id.nav_order_history) {
                Intent intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
        } else if (id == R.id.nav_live) {
            Intent intent = new Intent(this, com.example.project_prm.Activity.User.Chat.LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_virtual_room) {
            Intent intent = new Intent(this, VirtualRoomActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat_bot) {
            Intent intent = new Intent(this, ChatbotActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_start_live) {  // Start Live
            startLiveStream(appID, appSign, userID, userName, liveID);
        } else if (id == R.id.nav_watch_live) {  // Watch Live
            watchLiveStream(appID, appSign, userID, userName, liveID);
        }else{
            showLogoutConfirmationDialog();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {
        // Đăng xuất khỏi Firebase Authentication
        if (mAuth != null) {
            mAuth.signOut();
        }

        // Đăng xuất khỏi GoogleSignInClient
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                // Sau khi đăng xuất hoàn tất, chuyển về màn hình đăng nhập
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });

            // Xóa tài khoản Google để khi đăng nhập lại không tự động chọn tài khoản trước đó
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, task -> {
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            });
        }

        // Xóa thông tin đăng nhập đã lưu
        clearSavedLoginInfo();
    }

    private void clearSavedLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private String generateUserID() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < 5) {
            int nextInt = random.nextInt(10);
            if (builder.length() == 0 && nextInt == 0) {
                continue;
            }
            builder.append(nextInt);
        }
        return builder.toString();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void updateMenuBasedOnRole(NavigationView navigationView) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            return; // Không làm gì nếu userId không hợp lệ
        }

        userViewModel.getRoleById(userId).observe(this, roleId -> {
            if (roleId == null) return; // Kiểm tra nếu role null


            Menu menu = navigationView.getMenu();

            if (roleId == 2) { // Nếu là User
                menu.findItem(R.id.nav_statistic).setVisible(false);
                menu.findItem(R.id.nav_product).setVisible(false);
                menu.findItem(R.id.nav_account).setVisible(false);
                menu.findItem(R.id.nav_order).setVisible(false);
                menu.findItem(R.id.nav_start_live).setVisible(false);

            } else { // Nếu là Admin (role_id == 1)
                menu.findItem(R.id.nav_statistic).setVisible(true);
                menu.findItem(R.id.nav_product).setVisible(true);
                menu.findItem(R.id.nav_account).setVisible(true);
                menu.findItem(R.id.nav_order).setVisible(true);
                menu.findItem(R.id.nav_watch_live).setVisible(false);
            }
        });
    }
    private void startLiveStream(long appID, String appSign, String userID, String userName, String liveID) {
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra("host", true);
        intent.putExtra("appID", appID);
        intent.putExtra("appSign", appSign);
        intent.putExtra("userID", userID);
        intent.putExtra("userName", userName);
        intent.putExtra("liveID", liveID);
        startActivity(intent);
    }

    private void watchLiveStream(long appID, String appSign, String userID, String userName, String liveID) {
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra("appID", appID);
        intent.putExtra("appSign", appSign);
        intent.putExtra("userID", userID);
        intent.putExtra("userName", userName);
        intent.putExtra("liveID", liveID);
        startActivity(intent);
    }



}
