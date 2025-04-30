package com.example.project_prm.ViewModel.User;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Entities.Product;
import com.example.project_prm.Repository.ProductRepository;
import java.util.List;
import java.util.concurrent.Executors;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repository;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<Product>> allProductsForUser;
    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
        allProductsForUser = repository.getAllProductsForUser();
    }

    public LiveData<Product> getProductById(int productId) {
        MutableLiveData<Product> productLiveData = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            Product product = repository.getProductById(productId);
            productLiveData.postValue(product);
        });
        return productLiveData;
    }
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> getAllProductsForUser() {
        return allProductsForUser;
    }
    public void updateProductStock(int productId, int newStock) {
         repository.updateProductStock(productId, newStock);
    }

    public void insert(Product product) {
        repository.insert(product);
    }
}
