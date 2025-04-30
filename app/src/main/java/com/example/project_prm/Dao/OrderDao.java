package com.example.project_prm.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE user_id = :userId order by created_at desc ")
    List<Order> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders order by created_at desc")
    LiveData<List<Order>> getAllOrders();

    // Update order
    @Query("UPDATE orders SET status = 'Completed' WHERE id = :orderId")
    void acceptOrder(int orderId);
    @Query("UPDATE orders SET status = 'Cancelled' WHERE id = :orderId")
    void rejectOrder(int orderId);

    // Statistic
    @Query("SELECT COUNT(*) FROM orders WHERE created_at BETWEEN datetime('now', 'start of day') AND datetime('now', 'start of day', '+1 day', '-1 second')")
    int getTodayOrders();

    @Query("SELECT COUNT(*) FROM orders")
    int getTotalOrders();
}
