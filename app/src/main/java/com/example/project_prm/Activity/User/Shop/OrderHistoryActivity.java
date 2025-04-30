package com.example.project_prm.Activity.User.Shop;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.Admin.ManageOrderAdapter;
import com.example.project_prm.Adapter.User.OrderHistoryAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.R;
import com.example.project_prm.Repository.OrderRepository;
import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;
import com.example.project_prm.ViewModel.User.OrderHistoryViewModel;

import java.util.ArrayList;

public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private OrderHistoryViewModel orderHistoryViewModel;

    private OrderHistoryAdapter orderHistoryAdapter;
    private OrderRepository orderRepository;
    private ImageView ivFilter; // Thêm ImageView filter
    private String selectedStatus = "All"; // Trạng thái filter mặc định
    private int currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_order);
        getLayoutInflater().inflate(R.layout.activity_order_history, findViewById(R.id.content_frame));
        recyclerView = findViewById(R.id.recyclerViewOrderHistory);

        ivFilter = findViewById(R.id.ivFilterOrder); // Ánh xạ ImageView filter

        orderHistoryAdapter = new OrderHistoryAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột

        recyclerView.setAdapter(orderHistoryAdapter);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        orderRepository = new OrderRepository(this);
        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        orderHistoryViewModel.getOrderByUserId(currentUserId).observe(this, orders -> {
            orderHistoryAdapter.setOrderList(orders);
        });

        // Xử lý khi nhấn vào icon Filter
        ivFilter.setOnClickListener(v -> showFilterMenu(v));


    }

    private void showFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_menu_order, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_by_pending) {
                selectedStatus = "Pending";
            } else if (itemId == R.id.filter_by_completed) {
                selectedStatus = "Completed";
            } else if (itemId == R.id.filter_by_cancelled) {
                selectedStatus = "Cancelled";
            } else if (itemId == R.id.filter_by_payment) {
                selectedStatus = "Payment";
            } else {
                selectedStatus = "All";
            }

            orderHistoryAdapter.filter( selectedStatus);
            return true;
        });
        popup.show();

    }


}