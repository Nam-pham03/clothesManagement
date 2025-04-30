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

import com.example.project_prm.Activity.Admin.Account.DetailAccountActivity;
import com.example.project_prm.Activity.Admin.Product.DetailProductActivity;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.Entities.User;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageAccountViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountAdapter.UserViewHolder> {

    private List<User> userList = new ArrayList<>();
    private List<User> originalList = new ArrayList<>();
    private String currentQuery = "";
    private int selectedPosition = -1;
    private int currentUserId;

    public ManageAccountAdapter(List<User> userList, int currentUserId){
        if(userList != null){
        this.userList = new ArrayList<>(userList);
        this.originalList = new ArrayList<>(userList);
    }
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent,false);
        return  new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder,@SuppressLint("RecyclerView")  int position) {
        User user = userList.get(position);
        holder.name.setText(user.getUsername());
        holder.phone.setText(user.getPhone());
        holder.address.setText(user.getAddress());
        if (user.getIsDelete() == 1) {
            holder.status.setText("Deleted");
            holder.status.setTextColor(Color.RED);
        } else {
            holder.status.setText("Active");
            holder.status.setTextColor(Color.GREEN);
        }
        // Hiển thị ảnh sản phẩm từ đường dẫn
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            File imgFile = new File(user.getImage());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageViewAccount.setImageBitmap(bitmap);
            } else {
                holder.imageViewAccount.setImageResource(R.drawable.img_avatar);
            }
        } else {
            holder.imageViewAccount.setImageResource(R.drawable.img_avatar);
        }
        // Xử lý chọn radio button
        if (user.getId() == currentUserId) {
            holder.btnSelect.setVisibility(View.GONE);
        } else {
            holder.btnSelect.setVisibility(View.VISIBLE);
            holder.btnSelect.setChecked(selectedPosition == position);
            holder.btnSelect.setOnClickListener(v -> {
                if (selectedPosition == position) {
                    selectedPosition = -1;
                } else {
                    selectedPosition = position;
                }
                notifyDataSetChanged();
            });
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailAccountActivity.class);
            intent.putExtra("ID", user.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void setUserList(List<User> users) {
        if (users != null) {
            this.userList.clear();
            this.originalList.clear(); // Cập nhật danh sách gốc để lọc chính xác

            this.userList.addAll(users);
            this.originalList.addAll(users);

            notifyDataSetChanged(); // Cập nhật RecyclerView
        }
    }





    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, address, status;
        ImageView imageViewAccount;
        RadioButton btnSelect;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAccount = itemView.findViewById(R.id.imageViewAccount);
            name = itemView.findViewById(R.id.tvUserName);
            phone = itemView.findViewById(R.id.tvPhoneNumber);
            address = itemView.findViewById(R.id.tvAddress);
            status = itemView.findViewById(R.id.tvStatusAccount);
            btnSelect = itemView.findViewById(R.id.btnSelectAccount);
        }
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public User getUserAt(int position) {
        return userList.get(position);
    }
    public void clearSelection() {
        selectedPosition = -1; // Đặt lại vị trí đã chọn
        notifyDataSetChanged(); // Cập nhật lại danh sách
    }
    public void filterByName(String query) {

        this.currentQuery = query.toLowerCase();
        userList.clear();

        if (currentQuery.isEmpty()) {
            userList.addAll(originalList);
        } else {
            for (User user : originalList) {
                if (user.getUsername().toLowerCase().contains(currentQuery)) {
                    userList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }



}
