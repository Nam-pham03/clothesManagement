package com.example.project_prm.Activity.User.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Entities.User;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageAccountViewModel;
import com.example.project_prm.ViewModel.User.UserViewModel;
import com.zegocloud.zimkit.services.ZIMKit;

import im.zego.zim.enums.ZIMErrorCode;

public class LoginActivity extends AppCompatActivity {

    EditText userIdInput;
    TextView usernameTextView;

    int currentUserId;
    String userName;
    String avatar;
    ManageAccountViewModel userViewModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_chat);

        // Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(this).get(ManageAccountViewModel.class);

        // Lấy dữ liệu đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        userName = sharedPreferences.getString("username", "User");

        // Quan sát LiveData từ ViewModel
        userViewModel.getUserById(currentUserId).observe(this, user -> {
            if (user != null) {
                // Kiểm tra nếu image có giá trị hợp lệ
                if (user.getImage() != null && !user.getImage().isEmpty()) {
                    avatar = user.getImage();
                } else {
                    avatar = ""; // Hoặc set giá trị mặc định
                }

                // Gọi connectUser sau khi có dữ liệu
                connectUser(String.valueOf(currentUserId), userName, avatar);
            } else {
                Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        });

        // Khởi tạo Zego SDK
        ZIMKit.initWith(this.getApplication(), ConstantKey.appID, ConstantKey.appSign);
        ZIMKit.initNotifications();

    }


    // Phương thức đăng nhập người dùng
    public void connectUser(String userId, String userName, String userAvatar) {
        ZIMKit.connectUser(userId, userName, userAvatar, errorInfo -> {
            if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                // Nếu kết nối thành công, chuyển sang ConversationActivity
                toConversationActivity();
            } else {
                // Hiển thị thông báo lỗi bằng Toast
                String errorMessage = "Login failed with error code: " + errorInfo.code;
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Phương thức chuyển sang Activity Conversation
    private void toConversationActivity() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }


}
