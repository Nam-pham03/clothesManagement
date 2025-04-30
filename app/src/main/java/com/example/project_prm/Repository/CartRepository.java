package com.example.project_prm.Repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.project_prm.Dao.CartDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Cart;
import com.example.project_prm.Entities.CartWithProduct;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CartRepository {
    private CartDao cartDao;
    private LiveData<List<CartWithProduct>> cartItems;
    private ExecutorService executorService;

    public CartRepository(Application application, int userId) {
        ClothingDatabase db = ClothingDatabase.getInstance(application);
        cartDao = db.cartDao();
        cartItems = cartDao.getCartItemsWithProductByUserId(userId);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CartWithProduct>> getCartItems() {
        return cartItems;
    }

    public void insert(Cart cart) {
        executorService.execute(() -> cartDao.insert(cart));
    }

    public void delete(Cart cart) {
        executorService.execute(() -> cartDao.delete(cart));
    }
    public  void update(Cart cart){
        executorService.execute(() -> cartDao.update(cart));
    }

    public void deleteAllCartItems(int userId) {
        executorService.execute(() -> cartDao.deleteAllCartItemsByUserId(userId));
    }
}