package com.example.project_prm.Activity.User.Shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.ProductViewModel;

import java.io.File;

public class DetailProductShopActivity extends AppCompatActivity {

    private ProductViewModel manageProductViewModel;
    private ImageView imageView;
    private TextView tvName, tvBrand, tvSalePrice, tvManufacture,
           tvUnit, tvStock,tvOriginalPrice, tvDiscount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_shop);

        imageView = findViewById(R.id.imageViewProductDetail);
        tvName = findViewById(R.id.tvProductNameDetail);
        tvBrand = findViewById(R.id.tvBrandDetail);
        tvSalePrice = findViewById(R.id.tvSalePriceDetail);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvManufacture = findViewById(R.id.tvManufacture);
        tvOriginalPrice = findViewById(R.id.tvOriginalPriceDetail);
        tvUnit = findViewById(R.id.tvUnits);

        tvStock = findViewById(R.id.tvStock);


        manageProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            int productId = intent.getIntExtra("PRODUCT_ID", -1);
            manageProductViewModel.getProductById(productId).observe(this, product -> {
                if (product != null) {
                    // Tính giá sau giảm giá
                    double originalPrice = product.getSale_price();
                    double discount = product.getDiscount();
                    double salePrice = originalPrice - (originalPrice * discount / 100);

                    tvOriginalPrice.setText(String.format("Original Price: %,.0f", originalPrice));
                    tvSalePrice.setText(String.format("Sale Price: %,.0f", salePrice));

                    tvName.setText("Name: "+ product.getName());
                    tvBrand.setText("Brand: " + product.getBrand());
                    tvOriginalPrice.setText(String.format("Original Price: %,.0f", product.getSale_price()));

                    tvDiscount.setText(String.format("Discount: %,.0f%%", product.getDiscount()));

                    tvUnit.setText("Unit: "+product.getUnit());

                    tvManufacture.setText("Manufacture: "+product.getManufacturer());
                    tvStock.setText("Stock: " +product.getStock());



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