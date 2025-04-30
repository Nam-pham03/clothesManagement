package com.example.project_prm.Dao;

import androidx.room.*;
import com.example.project_prm.Entities.Rating;
import java.util.List;

@Dao
public interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Rating rating);

    @Update
    void update(Rating rating);

    @Delete
    void delete(Rating rating);

    @Query("SELECT * FROM rating WHERE product_id = :productId")
    List<Rating> getRatingsByProduct(int productId);
}
