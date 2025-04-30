package com.example.project_prm.ViewModel.Admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.Repository.OrderRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class ManageOrderViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;
    private LiveData<List<Order>> allOrder;
    public ManageOrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        allOrder = orderRepository.getAllOrders();
    }

    public LiveData<List<Order>> getAllOrder(){
        return  allOrder;
    }

    public LiveData<Order> getOrderById(int orderId){
        MutableLiveData<Order> orderMutableLiveData = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() ->{
            Order order = orderRepository.getOrderById(orderId);
            orderMutableLiveData.postValue(order);
        });
        return  orderMutableLiveData;

    }
    public MutableLiveData<Order> getOrderByUserId(int userId){
        MutableLiveData<Order> orderMutableLiveData = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() ->{
            List<Order> order = orderRepository.getOrderByUserId(userId);
            orderMutableLiveData.postValue((Order) order);
        });
        return  orderMutableLiveData;

    }
    public void insertOrderWithDetails(Order order, List<OrderDetail> details, Runnable onSuccess) {
        orderRepository.insertOrderWithDetails(order, details, onSuccess);
    }
}
