package com.example.project_prm.Activity.Admin.Order;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.Admin.ManageOrderAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.R;
import com.example.project_prm.Repository.OrderRepository;
import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManageOrderActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ManageOrderViewModel manageOrderViewModel;
    private ImageButton btnAccept, btnReject;
    private ManageOrderAdapter manageOrderAdapter;
    private OrderRepository orderRepository;
    private ImageView ivFilter; // Thêm ImageView filter
    private String selectedStatus = "All"; // Trạng thái filter mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_order);
        getLayoutInflater().inflate(R.layout.activity_manage_order, findViewById(R.id.content_frame));
        recyclerView = findViewById(R.id.recyclerViewOrder);
        btnAccept = findViewById(R.id.btnAcceptOrder);
        btnReject = findViewById(R.id.btnRejectOrder);
        ivFilter = findViewById(R.id.ivFilterOrder); // Ánh xạ ImageView filter

        manageOrderAdapter = new ManageOrderAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manageOrderAdapter);

        orderRepository = new OrderRepository(this);
        manageOrderViewModel = new ViewModelProvider(this).get(ManageOrderViewModel.class);
        manageOrderViewModel.getAllOrder().observe(this, manageOrderAdapter::setOrderList);


        // Xử lý khi nhấn vào icon Filter
        ivFilter.setOnClickListener(v -> showFilterMenu(v));

        // Xử lý khi nhấn nút Chấp nhận
        btnAccept.setOnClickListener(v -> confirmUpdateOrderStatus("Completed"));

        // Xử lý khi nhấn nút Từ chối
        btnReject.setOnClickListener(v -> confirmUpdateOrderStatus("Cancelled"));
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

            manageOrderAdapter.filter( selectedStatus);
            return true;
        });
        popup.show();

    }

    private void confirmUpdateOrderStatus(String newStatus) {
        List<Order> selectedOrders = manageOrderAdapter.getSelectedOrders();
        if (selectedOrders.isEmpty()) {
            Toast.makeText(this, "Please select at least one order", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Order Update")
                .setMessage("Are you sure you want to mark " + selectedOrders.size() + " orders as " + newStatus + "?")
                .setPositiveButton("Yes", (dialog, which) -> updateOrdersStatus(selectedOrders, newStatus))
                .setNegativeButton("No", null)
                .show();
    }

    private void updateOrdersStatus(List<Order> orders, String newStatus) {
        for (Order order : orders) {
            if ("Completed".equals(newStatus)) {
                orderRepository.acceptOrder(order.getId());
            } else {
                orderRepository.rejectOrder(order.getId());
            }
            order.setStatus(newStatus);
        }
        manageOrderAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Orders updated to " + newStatus, Toast.LENGTH_SHORT).show();
    }
}
