package com.example.project_prm.Activity.Admin.Product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageProductViewModel;

import java.io.File;

public class DetailProductActivity extends AppCompatActivity {

    private ManageProductViewModel manageProductViewModel;
    private ImageView imageView;
    private TextView tvName, tvBrand, tvPrice, tvStatus, tvProductCode, tvDealer, tvManufacture,
    tvCreate_at, tvUpdate_at, tvDiscount, tvUnit, tvStock;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        imageView = findViewById(R.id.imageViewProductDetail);
        tvName = findViewById(R.id.tvProductNameDetail);
        tvBrand = findViewById(R.id.tvBrandDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvStatus = findViewById(R.id.tvStatusDetail);
        tvManufacture = findViewById(R.id.tvManufacture);
        tvCreate_at = findViewById(R.id.tvCreate);
        tvUnit = findViewById(R.id.tvUnit);
        tvUpdate_at = findViewById(R.id.tvUpdate);
        tvProductCode = findViewById(R.id.tvProductCode);
        tvDealer = findViewById(R.id.tvDealerPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvStock = findViewById(R.id.tvStock);


        manageProductViewModel = new ViewModelProvider(this).get(ManageProductViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            int productId = intent.getIntExtra("PRODUCT_ID", -1);
            manageProductViewModel.getProductById(productId).observe(this, product -> {
                if (product != null) {
                    tvName.setText("Name: "+ product.getName());
                    tvBrand.setText("Brand: " + product.getBrand());
                    tvPrice.setText("Sale Price: "+ String.format("%,.0f", product.getSale_price()));
                    tvDealer.setText("Dealer Price: "+String.format("%,.0f", product.getDealer_price()));
                    tvDiscount.setText("Discount: "+String.format("%,.0f", product.getDiscount()));
                    tvUnit.setText("Unit: "+product.getUnit());
                    tvCreate_at.setText("Create Date: "+product.getCreated_at());
                    tvUpdate_at.setText("Update Date: "+product.getUpdated_at());
                    tvProductCode.setText("Code: "+product.getProduct_code());
                    tvManufacture.setText("Manufacture: "+product.getManufacturer());
                    tvStock.setText("Stock: " +product.getStock());

                    if (product.getIsDelete() == 1) {
                        tvStatus.setText("Deleted");
                        tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        tvStatus.setText("Active");
                        tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    }

                    // Hiển thị ảnh sản phẩm
                    if (product.getImage() != null && !product.getImage().isEmpty()) {
                        File imgFile = new File(product.getImage());
                        if (imgFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.img_avatar);
                        }
                    } else {
                        imageView.setImageResource(R.drawable.img_avatar);
                    }
                }
            });
        }
               findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

    }
}