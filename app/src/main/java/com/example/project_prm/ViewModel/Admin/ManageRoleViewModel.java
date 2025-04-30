package com.example.project_prm.ViewModel.Admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_prm.Entities.Category;
import com.example.project_prm.Entities.Role;
import com.example.project_prm.Repository.CategoryRepository;
import com.example.project_prm.Repository.RoleRepository;

import java.util.List;

public class ManageRoleViewModel extends AndroidViewModel {
    private final RoleRepository repository;
    private final LiveData<List<Role>> allRoles;

    public ManageRoleViewModel(@NonNull Application application) {
        super(application);
        repository = new RoleRepository(application);
        allRoles = repository.getAllRoles();
    }

    public LiveData<List<Role>> getAllRoles() {
        return allRoles;
    }
}
