package com.example.project_prm.Activity.User.VirtualRoom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VirtualRoomActivity extends AppCompatActivity {
    private ImageView modelPreview, clothingPreview, resultImage;
    private TextView loaderText;
    private Spinner categorySpinner;

    private Button uploadModelButton, uploadClothingButton, tryOnButton;
    private ProgressBar progressBar;  // Declare ProgressBar

    private Uri modelImageUri, clothingImageUri;

    // Declare ActivityResultLauncher for both model and clothing image selection
    private ActivityResultLauncher<Intent> modelActivityResultLauncher;
    private ActivityResultLauncher<Intent> clothingActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_room);
        modelPreview = findViewById(R.id.modelPreview);
        clothingPreview = findViewById(R.id.clothingPreview);
        resultImage = findViewById(R.id.resultImage);
        categorySpinner = findViewById(R.id.categorySpinner); // Reference Spinner
        uploadModelButton = findViewById(R.id.uploadModelButton);
        uploadClothingButton = findViewById(R.id.uploadClothingButton);
        tryOnButton = findViewById(R.id.tryOnButton);
        progressBar = findViewById(R.id.loaderProgressBar);  // Initialize ProgressBar

        // Set up Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Initialize ActivityResultLauncher for model image selection
        modelActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        modelImageUri = result.getData().getData();
                        modelPreview.setImageURI(modelImageUri);
                    }
                });

        // Initialize ActivityResultLauncher for clothing image selection
        clothingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        clothingImageUri = result.getData().getData();
                        clothingPreview.setImageURI(clothingImageUri);
                    }
                });

        // Set OnClickListeners for buttons
        uploadModelButton.setOnClickListener(v -> openImagePicker("model"));
        uploadClothingButton.setOnClickListener(v -> openImagePicker("clothing"));
        tryOnButton.setOnClickListener(v -> tryOnClothing());
    }
    private void openImagePicker(String type) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (type.equals("model")) {
            modelActivityResultLauncher.launch(intent);
        } else {
            clothingActivityResultLauncher.launch(intent);
        }
    }

    private void tryOnClothing() {
        if (modelImageUri == null || clothingImageUri == null) {
            Toast.makeText(this, "Please upload both model and clothing images.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show ProgressBar while processing
        progressBar.setVisibility(ProgressBar.VISIBLE); // Show progress bar
        resultImage.setVisibility(ImageView.GONE);

        try {
            String modelImageBase64 = encodeImageToBase64(modelImageUri);
            String clothingImageBase64 = encodeImageToBase64(clothingImageUri);
            String category = categorySpinner.getSelectedItem().toString(); // Get selected category

            // Prepare request body
            TryOnRequest request = new TryOnRequest(modelImageBase64, clothingImageBase64, category, 35, 2, 12467, true);

            // Create Retrofit instance
            Retrofit retrofit = RetrofitClient.getInstance();
            TryOnApi tryOnApi = retrofit.create(TryOnApi.class);

            // Call API asynchronously
            Call<TryOnResponse> call = tryOnApi.tryOn(request);
            call.enqueue(new Callback<TryOnResponse>() {
                @Override
                public void onResponse(Call<TryOnResponse> call, Response<TryOnResponse> response) {
                    // Hide ProgressBar when finished
                    progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar

                    if (response.isSuccessful() && response.body() != null) {
                        String base64Image = response.body().getImage();
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        resultImage.setImageBitmap(decodedByte);
                        resultImage.setVisibility(ImageView.VISIBLE);
                    } else {
                        Toast.makeText(VirtualRoomActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TryOnResponse> call, Throwable t) {
                    // Hide ProgressBar when failed
                    progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar
                    Toast.makeText(VirtualRoomActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Hide ProgressBar when error occurs
            progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeImageToBase64(Uri imageUri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}