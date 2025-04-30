package com.example.project_prm.Adapter.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.Admin.Product.DetailProductActivity;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.ProductViewHolder> {
    private List<Product> productList = new ArrayList<>();
    private List<Product> originalList = new ArrayList<>();
    private String currentQuery = "";
    private boolean isPriceAscending = true;



    private int selectedPosition = -1;


    public ManageProductAdapter(List<Product> productList) {

        if (productList != null) {
            this.productList = new ArrayList<>(productList);
            this.originalList = new ArrayList<>(productList);

        }
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.brand.setText(product.getBrand());
        holder.price.setText(String.format("%,.0f VND", product.getSale_price()));
        if (product.getIsDelete() == 1) {
            holder.status.setText("Deleted");
            holder.status.setTextColor(Color.RED);
        } else {
            holder.status.setText("Active");
            holder.status.setTextColor(Color.GREEN);
        }

        // Hiển thị ảnh sản phẩm từ đường dẫn
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            File imgFile = new File(product.getImage());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageViewProduct.setImageBitmap(bitmap);
            } else {
                holder.imageViewProduct.setImageResource(R.drawable.img_avatar);
            }
        } else {
            holder.imageViewProduct.setImageResource(R.drawable.img_avatar);
        }

        // Xử lý chọn radio button
        holder.btnSelect.setChecked(selectedPosition == position);
        holder.btnSelect.setOnClickListener(v -> {
            if (selectedPosition == position) {
                selectedPosition = -1;
            } else {
                selectedPosition = position;
            }
            notifyDataSetChanged();
        });

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> products) {
        this.originalList = new ArrayList<>(products);
        applyFilters();
    }


    public void filterByName(String query) {
        this.currentQuery = query.toLowerCase();
        applyFilters();
    }

    public void sortByPrice(boolean ascending) {
        this.isPriceAscending = ascending;
        applyFilters();
    }


    private void applyFilters() {
        List<Product> filteredList = new ArrayList<>();


        for (Product product : originalList) {
            if (product.getName().toLowerCase().contains(currentQuery)) {
                filteredList.add(product);
            }
        }


        if (isPriceAscending) {
            Collections.sort(filteredList, Comparator.comparingDouble(Product::getSale_price));
        } else {
            filteredList.sort((p1, p2) -> Double.compare(p2.getSale_price(), p1.getSale_price()));
        }


        productList = filteredList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, brand, status;
        ImageView imageViewProduct;
        RadioButton btnSelect;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            name = itemView.findViewById(R.id.tvProductName);
            brand = itemView.findViewById(R.id.tvBrandName);
            price = itemView.findViewById(R.id.tvProductPrice);
            status = itemView.findViewById(R.id.tvStatus);
            btnSelect = itemView.findViewById(R.id.btnSelect);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Product getProductAt(int position) {
        return productList.get(position);
    }
    public void clearSelection() {
        selectedPosition = -1; // Đặt lại vị trí đã chọn
        notifyDataSetChanged(); // Cập nhật lại danh sách
    }


}


