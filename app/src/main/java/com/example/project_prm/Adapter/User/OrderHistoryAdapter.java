package com.example.project_prm.Adapter.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.Admin.Order.OrderDetailActivity;
import com.example.project_prm.Adapter.Admin.ManageOrderAdapter;
import com.example.project_prm.Dao.OrderDetailDao;
import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderHistoryAdapter  extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private List<Order> orderList = new ArrayList<>();
    private List<Order> originalList = new ArrayList<>();
    private String currentQuery = "";

    private int selectedPosition = -1;
    private UserDao userDao;
    private OrderDetailDao orderDetailDao;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.userDao = ClothingDatabase.getInstance(context).userDao();
        this.orderDetailDao = ClothingDatabase.getInstance(context).orderDetailDao();
        if (orderList != null) {
            this.orderList = new ArrayList<>(orderList);
            this.originalList = new ArrayList<>(orderList);

        }

    }
    @NonNull
    @Override
    public OrderHistoryAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Order order = orderList.get(position);

        holder.userName.setText("Loading..."); // Hiển thị tạm thời

        new Thread(() -> {
            String username = userDao.getUserNameByIdOrder(order.getUser_id());
            ((Activity) holder.userName.getContext()).runOnUiThread(() -> {
                holder.userName.setText(username);
            });
        }).start();


        holder.totalPrice.setText(String.format("%,.0f VND", order.getTotal_price()));
        holder.created_at.setText(order.getCreated_at());
        holder.status.setText(order.getStatus());




        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("ORDER_ID", order.getId());
            context.startActivity(intent);
        });



    }
    public void setOrderList(List<Order> orders) {
        if (orders != null) {
            this.orderList.clear();
            this.originalList.clear();
            this.orderList.addAll(orders);
            this.originalList.addAll(orders);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }




    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView  userName, totalPrice, status, created_at;



        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tvUserName);
            totalPrice = itemView.findViewById(R.id.tvTotalPrice);
            status = itemView.findViewById(R.id.tvStatus);
            created_at = itemView.findViewById(R.id.tvCreateDate);

        }
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Order getOrderAt(int position) {
        return orderList.get(position);
    }
    public void clearSelection() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }
    public void filter( String status) {

        orderList.clear();

        if (  status.equals("All")) {
            orderList.addAll(originalList);
        } else {
            for (Order order : originalList) {

                boolean matchesStatus = status.equals("All") || order.getStatus().equals(status);

                if ( matchesStatus) {
                    orderList.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

}
