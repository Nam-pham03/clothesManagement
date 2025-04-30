package com.example.project_prm.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM category WHERE id = :categoryId")
    Category getCategoryById(int categoryId);

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();
}
