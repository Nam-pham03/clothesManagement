package com.example.project_prm.Activity.User.Cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.Activity.User.ZaloPay.OrderPayment;
import com.example.project_prm.Adapter.User.CartAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;
import com.example.project_prm.ViewModel.User.CartViewModel;
import com.example.project_prm.ViewModel.User.ProductViewModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivity {
    private CartViewModel cartViewModel;
    private ManageOrderViewModel orderViewModel;
    private ProductViewModel productViewModel;
    private CartAdapter cartAdapter;
    TextView tvTotalPrice;
    CheckBox checkboxSelectAll;

    private int currentUserId ; // Giả sử từ hệ thống đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_cart, findViewById(R.id.content_frame));
        RecyclerView recyclerView = findViewById(R.id.recyclerCartView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        checkboxSelectAll = findViewById(R.id.checkboxSelectAll);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        cartViewModel = new CartViewModel(getApplication(), currentUserId);
        orderViewModel = new ViewModelProvider(this).get(ManageOrderViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartAdapter = new CartAdapter(cartWithProduct -> cartViewModel.delete(cartWithProduct.getCart()), cartViewModel, this::updateTotalPrice,
                selectedItems -> {
                    checkboxSelectAll.setChecked(selectedItems.size() == cartAdapter.getItemCount());
                    updateTotalPrice();
                });
        recyclerView.setAdapter(cartAdapter);

        cartViewModel.getCartItems().observe(this, cartWithProducts -> {
            cartAdapter.setCartList(cartWithProducts);
            updateTotalPrice();
            if (cartWithProducts.isEmpty()) {
                checkboxSelectAll.setVisibility(View.GONE);
            } else {
                checkboxSelectAll.setVisibility(View.VISIBLE);
            }
        });

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> showConfirmDialog());

        checkboxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cartAdapter.getSelectedItems().addAll(cartAdapter.cartList);
            } else {
                cartAdapter.getSelectedItems().clear();
            }
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        });
    }

    private void updateTotalPrice() {
        cartViewModel.getCartItems().observe(this, cartWithProducts -> {
            double totalPrice = 0;
            for (CartWithProduct item : cartWithProducts) {
                totalPrice += item.getCart().getQuantity() * item.getCart().getPrice();
            }
            tvTotalPrice.setText("Total: " + String.format("%,.0f", totalPrice) + " VND");
        });
    }
    private void showConfirmDialog() {
        if (cartAdapter.getSelectedItems().isEmpty()) {
            Toast.makeText(this, "Please select at least one item to place an order!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Yes", (dialog, which) -> processOrder())
                .setNegativeButton("No", null)
                .show();
    }


    private void processOrder() {
        if (cartAdapter.getSelectedItems().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<CartWithProduct> selectedItems = new ArrayList<>(cartAdapter.getSelectedItems());
        double totalPrice = 0;
        StringBuilder productNames = new StringBuilder();
        ArrayList<Integer> quantities = new ArrayList<>();
        for (CartWithProduct item : selectedItems) {
            if (item.getCart().getQuantity() > item.getProductStock()) {
                Toast.makeText(this, "Sản phẩm " + item.getProductName() + " không đủ hàng!", Toast.LENGTH_SHORT).show();
                return;
            }
            totalPrice += item.getCart().getQuantity() * item.getCart().getPrice();
            productNames.append(item.getProductName()).append(", ");
            quantities.add(item.getCart().getQuantity());
        }

        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2); // Xóa dấu ", " cuối cùng
        }


        Intent intent = new Intent(CartActivity.this, OrderPayment.class);
        intent.putExtra("product_names", productNames.toString());
        intent.putExtra("quantity", quantities);
        intent.putExtra("total", totalPrice);
        intent.putExtra("cart_items", (Serializable) selectedItems); // Truyền danh sách giỏ hàng
        startActivity(intent);

    }


}

