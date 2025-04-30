package com.example.project_prm.Activity.Admin.Order;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.Adapter.Admin.ManageOrderDetailAdapter;
import com.example.project_prm.Dao.OrderDetailDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.R;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ManageOrderDetailAdapter adapter;
    private OrderDetailDao orderDetailDao;
    private int orderId;
    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        recyclerView = findViewById(R.id.recyclerViewOrderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderDetailDao = ClothingDatabase.getInstance(this).orderDetailDao(); // Khởi tạo đúng database

        orderId = getIntent().getIntExtra("ORDER_ID", -1);

        if (orderId != -1) {
            loadOrderDetails();
        } else {
            Toast.makeText(this, "Order ID không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        btnBack = findViewById(R.id.btnBackDetail);
        btnBack.setOnClickListener(v -> finish());

    }

    private void loadOrderDetails() {
        new Thread(() -> {
            List<OrderDetail> orderDetails = orderDetailDao.getOrderDetailsByOrderId(orderId);
            runOnUiThread(() -> {
                adapter = new ManageOrderDetailAdapter(this, orderDetails);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
