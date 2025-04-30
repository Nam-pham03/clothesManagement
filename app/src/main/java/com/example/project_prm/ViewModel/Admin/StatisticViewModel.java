package com.example.project_prm.ViewModel.Admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Repository.StatisticRepository;

import java.util.concurrent.Executors;

public class StatisticViewModel extends AndroidViewModel {
    private StatisticRepository repository;
    private MutableLiveData<Integer> todayOrders = new MutableLiveData<>();
    private MutableLiveData<Integer> totalOrders = new MutableLiveData<>();
    private MutableLiveData<Integer> totalProducts = new MutableLiveData<>();
    private MutableLiveData<Integer> totalAccounts = new MutableLiveData<>();

    public StatisticViewModel(@NonNull Application application) {
        super(application);
        repository = new StatisticRepository(application);
        loadStatistics();
    }

    private void loadStatistics() {
        Executors.newSingleThreadExecutor().execute(() -> {
            todayOrders.postValue(repository.getTodayOrders());
            totalOrders.postValue(repository.getTotalOrders());
            totalProducts.postValue(repository.getTotalProducts());
            totalAccounts.postValue(repository.getTotalAccounts());
        });
    }

    public LiveData<Integer> getTodayOrders() {
        return todayOrders;
    }

    public LiveData<Integer> getTotalOrders() {
        return totalOrders;
    }

    public LiveData<Integer> getTotalProducts() {
        return totalProducts;
    }

    public LiveData<Integer> getTotalAccounts() {
        return totalAccounts;
    }
}

