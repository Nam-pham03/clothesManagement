package com.example.project_prm.Activity.User.ZaloPay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.Activity.User.ZaloPay.Api.CreateOrder;
import com.example.project_prm.Adapter.User.CheckOutAdapter;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;
import com.example.project_prm.ViewModel.User.CartViewModel;
import com.example.project_prm.ViewModel.User.ProductViewModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPayment extends AppCompatActivity {

    Button btnThanhToan, btnBack;
    private ManageOrderViewModel orderViewModel;
    private RecyclerView recyclerView;
    private CheckOutAdapter adapter;
    private List<CartWithProduct> cartItems;
    private Queue<List<CartWithProduct>> orderQueue;  // Queue to handle orders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        btnThanhToan = findViewById(R.id.buttonThanhToan);
        btnBack = findViewById(R.id.btnBack);
        orderViewModel = new ViewModelProvider(this).get(ManageOrderViewModel.class);

        Intent intent = getIntent();
        ArrayList<Integer> quantities = (ArrayList<Integer>) intent.getSerializableExtra("quantities");
        Double total = intent.getDoubleExtra("total", 0.0);

        btnThanhToan.setOnClickListener(v -> processPayment(total));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        recyclerView = findViewById(R.id.recyclerViewCheckOut);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = (List<CartWithProduct>) getIntent().getSerializableExtra("cart_items");

        if (cartItems != null) {
            adapter = new CheckOutAdapter(cartItems);
            recyclerView.setAdapter(adapter);
        }
        btnBack.setOnClickListener(v -> finish());

        orderQueue = new LinkedList<>(); // Initialize the queue
    }

    private void processPayment(double total) {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.format("%.0f", total));
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(OrderPayment.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        // After payment success, update the database and process the queue
                        updateDatabaseAfterPayment();
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        showPaymentResult("Hủy thanh toán");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        showPaymentResult("Lỗi thanh toán");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDatabaseAfterPayment() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0;

        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        CartViewModel cartViewModel = new CartViewModel(getApplication(), getUserId());

        // Add the cart items to the queue
        orderQueue.add(cartItems);

        // Process orders in the queue
        while (!orderQueue.isEmpty()) {
            List<CartWithProduct> currentOrder = orderQueue.poll();
            for (CartWithProduct item : currentOrder) {
                if (item.getCart().getQuantity() > item.getProductStock()) {
                    // Product quantity exceeds stock, notify user and skip this order
                    Toast.makeText(this, "Product: " + item.getProductName() + " invalid quantity!", Toast.LENGTH_SHORT).show();
                    return;
                }
                totalPrice += item.getCart().getQuantity() * item.getProductPrice();
                orderDetails.add(new OrderDetail(0, 0, item.getCart().getProduct_id(), item.getCart().getQuantity(), item.getProductPrice()));

                // Update the product stock in the database
                productViewModel.updateProductStock(item.getCart().getProduct_id(), item.getCart().getQuantity());
            }

            // Create a new order after processing the cart items
            Order order = new Order(0, getUserId(), totalPrice, "Payment",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()), "");

            // Insert the order with details into the database
            orderViewModel.insertOrderWithDetails(order, orderDetails, () -> {
                // After inserting the order, remove the items from the cart
                for (CartWithProduct item : cartItems) {
                    cartViewModel.delete(item.getCart());
                }
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });

            showPaymentResult("Thanh toán thành công");
        }
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private void showPaymentResult(String message) {
        Intent intent = new Intent(OrderPayment.this, ProductListActivity.class);
        intent.putExtra("result", message);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
