package com.example.project_prm.Dao ;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("UPDATE product SET isDelete = 1 WHERE id = :productId")
    void softDelete(int productId);
    @Query("UPDATE product SET isDelete = 0 WHERE id = :productId")
    void restoreProduct(int productId);


    @Query("SELECT * FROM product WHERE id = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM product  Order By created_at desc")
    LiveData<List<Product>> getAllProductsAdmin();
    @Query("SELECT name  FROM product WHERE id = :productId")
    String getProductNameByOrder(int productId);

    @Query("SELECT COUNT(*) FROM Product where isDelete = 0")
    int getTotalProducts();
    @Query("SELECT * FROM product where isDelete = 0  Order By created_at desc  ")
    LiveData<List<Product>> getAllProductsUser();
    @Query("UPDATE product SET stock = stock - :quantity WHERE id = :productId AND stock >= :quantity")
    void updateStock(int productId, int quantity);



}
