package com.example.project_prm.ViewModel.User;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_prm.Entities.Cart;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.Repository.CartRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository repository;
    private LiveData<List<CartWithProduct>> cartItems;

    public CartViewModel(@NonNull Application application, int userId) {
        super(application);
        repository = new CartRepository(application, userId);
        cartItems = repository.getCartItems();
    }

    public LiveData<List<CartWithProduct>> getCartItems() {
        return cartItems;
    }

    public void insert(Cart cart) {
        repository.insert(cart);
    }

    public void delete(Cart cart) {
        repository.delete(cart);
    }

    public void deleteAllCartItems(int userId) {
        repository.deleteAllCartItems(userId);
    }
    public void update(Cart cart) {
        new Thread(() -> repository.update(cart)).start();
    }

}