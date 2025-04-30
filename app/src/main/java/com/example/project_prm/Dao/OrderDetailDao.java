package com.example.project_prm.Dao;

import androidx.room.*;
import com.example.project_prm.Entities.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderDetail orderDetail);

    @Update
    void update(OrderDetail orderDetail);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query("SELECT * FROM order_detail WHERE order_id = :orderId")
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);

    @Query("SELECT od.*, p.name AS productName, p.sale_price AS productPrice " +
            "FROM order_detail od " +
            "INNER JOIN product p ON od.product_id = p.id " +
            "WHERE od.order_id = :orderId")
    List<OrderDetail> getOrderDetailsWithProducts(int orderId);
}
