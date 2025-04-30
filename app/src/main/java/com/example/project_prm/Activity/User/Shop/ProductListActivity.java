package com.example.project_prm.Activity.User.Shop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.User.ProductListAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.Cart;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.ProductViewModel;
import com.example.project_prm.ViewModel.User.CartViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductListActivity extends BaseActivity {
    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;
    private ProductListAdapter productAdapter;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_product_list, findViewById(R.id.content_frame));

        // Lấy ID người dùng từ hệ thống đăng nhập
        currentUserId = getCurrentUserId();

        // Khởi tạo Adapter với sự kiện thêm vào giỏ hàng
        productAdapter = new ProductListAdapter(new ArrayList<>(), this::addToCart);

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewShop);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(productAdapter);

        // Khởi tạo ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProductsForUser().observe(this, products -> {
            if (products != null && !products.isEmpty()) {
                productAdapter.setProductList(products);
                Log.d( "PoruducList","Loaded " + products.size() + " products");
            } else {
                Log.d( "PoruducList","No products found");
            }
        });
        cartViewModel = new CartViewModel(getApplication(), currentUserId);

        // Xử lý sự kiện lọc (sort)
        ImageView ivFilter = findViewById(R.id.ivFilterList);
        ivFilter.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ProductListActivity.this, ivFilter);
            popup.getMenuInflater().inflate(R.menu.filter_menu_product, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.filter_by_price_desc) {
                    productAdapter.sortByPrice(false); // Giá giảm dần
                } else if (item.getItemId() == R.id.filter_by_price_inc) {
                    productAdapter.sortByPrice(true); // Giá tăng dần
                }
                return true;
            });

            popup.show();
        });

        // Xử lý tìm kiếm
        EditText etSearch = findViewById(R.id.etSearchBar);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.filterByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void addToCart(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Quantity (Stock: " + product.getStock() + ")");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter quantity");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String quantityStr = input.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (quantity > product.getStock()) {
                Toast.makeText(this, "Quantity exceeds stock limit", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            cartViewModel.getCartItems().observe(this, cartItems -> {
                boolean exists = false;
                for (CartWithProduct cartItem : cartItems) {
                    if (cartItem.getCart().getProduct_id() == product.getId()) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    Toast.makeText(this, "Product is already in the cart!", Toast.LENGTH_SHORT).show();
                } else {
                    double finalPrice = product.getFinalPrice();
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    Cart cart = new Cart(currentUserId, product.getId(), quantity, currentTime, finalPrice);
                    Log.d("AddToCart", "Cart: " + cart.toString());

                    cartViewModel.insert(cart);
                    Toast.makeText(this, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        return userId;
    }
}
