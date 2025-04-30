package com.example.project_prm.Activity.Admin.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.Admin.Product.EditProductActivity;
import com.example.project_prm.Activity.Admin.Product.ManageProductActivity;
import com.example.project_prm.Adapter.Admin.ManageAccountAdapter;
import com.example.project_prm.Adapter.Admin.ManageProductAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.Entities.User;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageAccountViewModel;
import com.example.project_prm.ViewModel.Admin.ManageProductViewModel;

import java.util.ArrayList;

public class ManageAccountActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ManageAccountAdapter manageAccountAdapter;
    private ManageAccountViewModel manageAccountViewModel;
    private int currentUserId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_manage_account, findViewById(R.id.content_frame));
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        manageAccountAdapter = new ManageAccountAdapter(new ArrayList<>(), currentUserId);
        recyclerView = findViewById(R.id.recyclerViewAccount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            int selectedPosition = manageAccountAdapter.getSelectedPosition();
            if (selectedPosition != -1) {
                User selectedUser= manageAccountAdapter.getUserAt(selectedPosition);
                Intent intent = new Intent(ManageAccountActivity.this, EditAccountActivity.class);
                intent.putExtra("ID", selectedUser.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(manageAccountAdapter);

        manageAccountViewModel = new ViewModelProvider(this).get(ManageAccountViewModel.class);
        manageAccountViewModel.getAllUser().observe(this,manageAccountAdapter::setUserList);

        findViewById(R.id.btnAdd).setOnClickListener(v ->{
            Intent intent = new Intent(ManageAccountActivity.this, AddAccountActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            int selectedPosition = manageAccountAdapter.getSelectedPosition();
            if (selectedPosition != -1) {
                User selectedUser = manageAccountAdapter.getUserAt(selectedPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Continue your action?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (selectedUser.getIsDelete() == 1) {
                                manageAccountViewModel.restoreUser(selectedUser.getId());

                            } else {
                                manageAccountViewModel.softDeleteUser(selectedUser.getId());
                            }
                            manageAccountAdapter.clearSelection();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }else{
                Toast.makeText(this, "Choose one account", Toast.LENGTH_SHORT).show();
            }

        });
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                manageAccountAdapter.filterByName(s.toString()); // Gọi hàm tìm kiếm khi nhập dữ liệu
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



    }
}