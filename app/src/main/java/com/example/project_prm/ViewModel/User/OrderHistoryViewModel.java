package com.example.project_prm.ViewModel.User;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Entities.Order;
import com.example.project_prm.Repository.OrderRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class OrderHistoryViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;
    private MutableLiveData<List<Order>> ordersByUserId;

    public OrderHistoryViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        ordersByUserId = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getOrderByUserId(int userId) {
        MutableLiveData<List<Order>> orderMutableLiveData = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Order> orders = orderRepository.getOrderByUserId(userId);
            orderMutableLiveData.postValue(orders);
        });
        return orderMutableLiveData;
    }

}



