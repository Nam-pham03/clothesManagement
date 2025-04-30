package com.example.project_prm.ViewModel.Admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Entities.User;
import com.example.project_prm.Repository.UserRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class ManageAccountViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> allUser;
    public ManageAccountViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUser = userRepository.getAllUserAdmin();
    }

    public LiveData<List<User>> getAllUser(){
        return allUser;
    }
    public LiveData<User> getUserById(int userId){
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() ->{
            User user = userRepository.getUserById(userId);
            userLiveData.postValue(user);
        });
        return  userLiveData;
    }

    public void insert(User user){
        userRepository.insert(user);
    }
    public void update(User user){
        userRepository.update(user);
    }

    public void softDeleteUser(int userId) {
        userRepository.softDeleteUser(userId);
    }
    public void restoreUser(int userId) {
        userRepository.restoreUser(userId);
    }
}
