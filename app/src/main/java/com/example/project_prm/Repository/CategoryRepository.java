package com.example.project_prm.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Dao.CategoryDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepository(Application application) {
        ClothingDatabase db = ClothingDatabase.getInstance(application);
        categoryDao = db.categoryDao();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();  // Trả về LiveData trực tiếp từ DAO
    }


    public void insert(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> categoryDao.insert(category));
    }
}
