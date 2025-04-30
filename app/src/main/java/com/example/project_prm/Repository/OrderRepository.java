package com.example.project_prm.Repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.project_prm.Dao.OrderDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final ClothingDatabase db;
    private final OrderDao orderDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public OrderRepository(Context context) {
        this.db = ClothingDatabase.getInstance(context);  // Lưu db vào biến thành viên
        this.orderDao = db.orderDao();
    }

    public void insert(Order order) {
        executorService.execute(() -> orderDao.insert(order));
    }

    public LiveData<List<Order>> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }
    public List<Order> getOrderByUserId(int userId) {
        return orderDao.getOrdersByUser(userId);
    }

    public void acceptOrder(int userId) {
        executorService.execute(() -> orderDao.acceptOrder(userId));
    }

    public void rejectOrder(int userId) {
        executorService.execute(() -> orderDao.rejectOrder(userId));
    }

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void insertOrderWithDetails(Order order, List<OrderDetail> details, Runnable onSuccess) {
        executorService.execute(() -> {
            long orderId = orderDao.insert(order);
            for (OrderDetail detail : details) {
                detail.setOrder_id((int) orderId);
                db.orderDetailDao().insert(detail);
            }

            // Chuyển callback về UI thread
            mainHandler.post(onSuccess);
        });
    }


}
