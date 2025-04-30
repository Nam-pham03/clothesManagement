package com.example.project_prm.Adapter.Admin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Dao.ProductDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.R;
import java.util.List;

public class ManageOrderDetailAdapter extends RecyclerView.Adapter<ManageOrderDetailAdapter.ViewHolder> {
    private List<OrderDetail> orderDetails;
    private ProductDao productDao;
    private Context context;

    public ManageOrderDetailAdapter(Context context, List<OrderDetail> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.productDao = ClothingDatabase.getInstance(context).productDao(); // Khởi tạo productDao
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderdetail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);

        // Hiển thị dữ liệu tạm thời
        holder.tvProductName.setText("Loading...");
        holder.tvQuantity.setText("Quantity: " + detail.getQuantity());
        holder.tvPrice.setText(String.format("%,.0f VND", detail.getPrice()));

        // Lấy tên sản phẩm từ database
        new Thread(() -> {
            String productName = productDao.getProductNameByOrder(detail.getProduct_id()); // Gọi đúng phương thức
            ((Activity) context).runOnUiThread(() -> {
                holder.tvProductName.setText(productName);
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
