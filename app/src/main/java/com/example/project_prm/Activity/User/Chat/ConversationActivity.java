package com.example.project_prm.Activity.User.Chat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zegocloud.zimkit.common.ZIMKitRouter;
import com.zegocloud.zimkit.common.enums.ZIMKitConversationType;

public class ConversationActivity extends AppCompatActivity {

    FloatingActionButton actionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation);



        actionButton = findViewById(R.id.floating_btn);

        actionButton.setOnClickListener(v->{
            showPopupMenu();
        });

    }


    void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, actionButton);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.new_chat) {
                showNewChatDialog();
                return true;
            }
            if (menuItem.getItemId() == R.id.logout) {
                Intent intent = new Intent(this, ProductListActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    void showNewChatDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New chat");

        EditText edittext = new EditText(this);
        edittext.setHint("UserName");
        builder.setView(edittext);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ZIMKitRouter.toMessageActivity(ConversationActivity.this, edittext.getText().toString(), ZIMKitConversationType.ZIMKitConversationTypePeer);
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.create().show();

    }

}