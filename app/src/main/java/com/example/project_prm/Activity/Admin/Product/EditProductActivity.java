package com.example.project_prm.Activity.Admin.Product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Entities.Category;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.Repository.CategoryRepository;
import com.example.project_prm.Repository.ProductRepository;
import com.example.project_prm.Utils.ImageUtils;
import com.example.project_prm.ViewModel.Admin.ManageProductViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditProductActivity extends AppCompatActivity {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private Product existingProduct;

    private EditText etProductNameEdit, etBrandEdit, etProductCodeEdit, etStockEdit, etUnitEdit,
            etSalePriceEdit, etDiscountEdit, etDealerPriceEdit, etManufacturerEdit;
    private ImageView imgProductEdit;
    private Spinner spinnerCategoryEdit;
    private Button btnSaveProductEdit, btnUploadImageEdit, btnBack;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ManageProductViewModel manageProductViewModel;
    private List<Category> categoryList = new ArrayList<>();

    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Ánh xạ view từ XML
        etProductNameEdit = findViewById(R.id.etProductNameEdit);
        etBrandEdit = findViewById(R.id.etBrandEdit);
        etProductCodeEdit = findViewById(R.id.etProductCodeEdit);
        etStockEdit = findViewById(R.id.etStockEdit);
        etUnitEdit = findViewById(R.id.etUnitEdit);
        etSalePriceEdit = findViewById(R.id.etSalePriceEdit);
        etDiscountEdit = findViewById(R.id.etDiscountEdit);
        etDealerPriceEdit = findViewById(R.id.etDealerPriceEdit);
        etManufacturerEdit = findViewById(R.id.etManufacturerEdit);
        imgProductEdit = findViewById(R.id.imgProductEdit);
        spinnerCategoryEdit = findViewById(R.id.spinnerCategoryEdit);
        btnSaveProductEdit = findViewById(R.id.btnSaveProductEdit);
        btnUploadImageEdit = findViewById(R.id.btnUploadImageEdit);
        btnBack = findViewById(R.id.btnBackEdit);

        btnSaveProductEdit.setOnClickListener(v -> saveProduct());
        btnUploadImageEdit.setOnClickListener(v -> openFileChooser());
        btnBack.setOnClickListener(v -> finish());

        manageProductViewModel = new ViewModelProvider(this).get(ManageProductViewModel.class);

        // Khởi tạo repository
        productRepository = new ProductRepository(getApplication());
        categoryRepository = new CategoryRepository(getApplication());

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductData(productId);
        }else{
            Toast.makeText(this, "PLease choose product to edit!", Toast.LENGTH_SHORT).show();
        }

        setupImagePicker();
    }
    private void loadProductData(int productId) {
        ManageProductViewModel manageProductViewModel = new ViewModelProvider(this).get(ManageProductViewModel.class);
        manageProductViewModel.getProductById(productId).observe(this, this::updateUI);
    }
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                Bitmap bitmap;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImageUri);
                                    bitmap = ImageDecoder.decodeBitmap(source);
                                } else {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                }

                                // Tạo tên file ảnh duy nhất
                                String fileName = "product_" + System.currentTimeMillis() + ".png";

                                // Lưu ảnh vào bộ nhớ trong và lấy đường dẫn
                                imagePath = ImageUtils.saveImageToInternalStorage(this, bitmap, fileName);

                                // Hiển thị ảnh lên ImageView
                                imgProductEdit.setImageBitmap(bitmap);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Error when choose image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }



    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }


    private void updateUI(Product product) {
        if (product != null) {
            existingProduct = product;
            etProductNameEdit.setText(product.getName());
            etBrandEdit.setText(product.getBrand());
            etProductCodeEdit.setText(product.getProduct_code());
            etStockEdit.setText(String.valueOf(product.getStock()));
            etUnitEdit.setText(product.getUnit());
            etSalePriceEdit.setText(String.valueOf(product.getSale_price()));
            etDiscountEdit.setText(String.valueOf(product.getDiscount()));
            etDealerPriceEdit.setText(String.valueOf(product.getDealer_price()));
            etManufacturerEdit.setText(product.getManufacturer());

            if (product.getImage() != null && !product.getImage().isEmpty()) {
                File imageFile = new File(product.getImage());
                if (imageFile.exists()) {
                    imgProductEdit.setImageURI(Uri.fromFile(imageFile)); // Hiển thị ảnh từ file nội bộ
                    imagePath = product.getImage();
                }
            }


            if (spinnerCategoryEdit != null) {
                loadCategorySpinner(product.getCategory_id());
            }
        }
    }

    private void saveProduct() {
        String name = etProductNameEdit.getText().toString().trim();
        String brand = etBrandEdit.getText().toString().trim();
        String productCode = etProductCodeEdit.getText().toString().trim();
        String stockStr = etStockEdit.getText().toString().trim();
        String salePriceStr = etSalePriceEdit.getText().toString().trim();
        String discountStr = etDiscountEdit.getText().toString().trim();
        String dealerPriceStr = etDealerPriceEdit.getText().toString().trim();
        String manufacturer = etManufacturerEdit.getText().toString().trim();
        String unit = etUnitEdit.getText().toString().trim();

        if (name.isEmpty() || brand.isEmpty() || productCode.isEmpty() || stockStr.isEmpty() || salePriceStr.isEmpty()) {
            Toast.makeText(this, "Enter full require information", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        double salePrice = Double.parseDouble(salePriceStr);
        double discount = discountStr.isEmpty() ? 0.0 : Double.parseDouble(discountStr);
        double dealerPrice = dealerPriceStr.isEmpty() ? 0.0 : Double.parseDouble(dealerPriceStr);

        int selectedPosition = spinnerCategoryEdit.getSelectedItemPosition();
        int selectedCategoryId = categoryList.get(selectedPosition).getId();

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Product updatedProduct = new Product(
                existingProduct.getId(),
                name, selectedCategoryId, brand, productCode, stock, unit,
                salePrice, discount, dealerPrice, manufacturer, imagePath,
                existingProduct.getCreated_at(), currentTime, 0
        );



        manageProductViewModel.update(updatedProduct);

        Toast.makeText(this, "Update successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }



    private void loadCategorySpinner(int selectedCategory) {
        categoryRepository.getAllCategories().observe(this, categories -> {
            if (categories != null) {
                categoryList = categories;
                List<String> categoryNames = new ArrayList<>();
                for (Category category : categories) {
                    categoryNames.add(category.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategoryEdit.setAdapter(adapter);

                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == selectedCategory) {
                        spinnerCategoryEdit.setSelection(i);
                        break;
                    }
                }
            }
        });
    }
}
