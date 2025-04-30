package com.example.project_prm.Adapter.Admin;

import static im.zego.connection.internal.ZegoConnectionImpl.context;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.Admin.Order.OrderDetailActivity;
import com.example.project_prm.Activity.Admin.Product.DetailProductActivity;
import com.example.project_prm.Dao.OrderDetailDao;
import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.Entities.User;
import com.example.project_prm.R;

import java.util.ArrayList;
import java.util.List;

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.OrderViewHolder> {
    private List<Order> orderList = new ArrayList<>();
    private List<Order> originalList = new ArrayList<>();
    private String currentQuery = "";

    private int selectedPosition = -1;
    private List<Order> selectedOrders = new ArrayList<>();
    private UserDao userDao;
    private OrderDetailDao orderDetailDao;

    public ManageOrderAdapter(Context context,List<Order> orderList) {
        this.userDao = ClothingDatabase.getInstance(context).userDao();
        this.orderDetailDao = ClothingDatabase.getInstance(context).orderDetailDao();
        if (orderList != null) {
            this.orderList = new ArrayList<>(orderList);
            this.originalList = new ArrayList<>(orderList);

        }

    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder,@SuppressLint("RecyclerView") int position) {

        Order order = orderList.get(position);
        holder.orderID.setText(String.valueOf(order.getId()));
        holder.userName.setText("Loading..."); // Hiển thị tạm thời

        new Thread(() -> {
            String username = userDao.getUserNameByIdOrder(order.getUser_id());
            ((Activity) holder.userName.getContext()).runOnUiThread(() -> {
                holder.userName.setText(username);
            });
        }).start();


        holder.totalPrice.setText(String.format("%,.0f VND", order.getTotal_price()));
        holder.status.setText(order.getStatus());

        if ("Completed".equals(order.getStatus()) ||"Cancelled".equals(order.getStatus()) ) {
            holder.checkBoxSelect.setVisibility(View.GONE);
        } else {
            holder.checkBoxSelect.setVisibility(View.VISIBLE);
        }

        holder.checkBoxSelect.setChecked(selectedOrders.contains(order));

        holder.checkBoxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedOrders.add(order);
            } else {
                selectedOrders.remove(order);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("ORDER_ID", order.getId());
            context.startActivity(intent);
        });



    }
    public void setOrderList(List<Order> orders) {
        if(orders != null){
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
        TextView orderID, userName, totalPrice, status;


        CheckBox checkBoxSelect;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.tvOrderId);
            userName = itemView.findViewById(R.id.tvUserName);
            totalPrice = itemView.findViewById(R.id.tvTotalPrice);
            status = itemView.findViewById(R.id.tvStatus);
            checkBoxSelect = itemView.findViewById(R.id.checkBoxSelectOrder);
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
    public List<Order> getSelectedOrders() {
        return new ArrayList<>(selectedOrders);
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
