package com.example.project_prm.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

import com.example.project_prm.Dao.ProductDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Product;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;
    private ExecutorService executorService;
    private LiveData<List<Product>> allProductsForUser;

    public ProductRepository(Application application) {
        ClothingDatabase db = ClothingDatabase.getInstance(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProductsAdmin();
        allProductsForUser = productDao.getAllProductsUser();
        executorService = Executors.newSingleThreadExecutor();
    }
    public LiveData<List<Product>> getAllProductsForUser() {
        return allProductsForUser;
    }
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        new InsertProductAsyncTask(productDao).execute(product);
    }
    public void update(Product product) {

        executorService.execute(() -> productDao.update(product));
    }
    public void softDelete(int productId) {
        executorService.execute(() -> productDao.softDelete(productId));
    }
    public void restoreProduct(int productId) {
        executorService.execute(() -> productDao.restoreProduct(productId));
    }


    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }
    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insert(products[0]);
            return null;
        }
    }
    public void updateProductStock(int productId, int quantity) {
        executorService.execute(() -> productDao.updateStock(productId, quantity));
    }

}
