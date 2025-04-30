package com.example.project_prm.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.Cart;
import com.example.project_prm.Entities.CartWithProduct;

import java.util.List;
@Dao
public interface CartDao {
    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT cart.*, product.name AS productName, product.sale_price AS productPrice, product.stock AS productStock " +
            "FROM cart " +
            "INNER JOIN product ON cart.product_id = product.id " +
            "WHERE cart.user_id = :userId")
    LiveData<List<CartWithProduct>> getCartItemsWithProductByUserId(int userId);

    @Query("DELETE FROM cart WHERE user_id = :userId")
    void deleteAllCartItemsByUserId(int userId);
}
