package com.example.project_prm.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.project_prm.Dao.RoleDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Role;
import com.example.project_prm.Entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoleRepository {
    private final RoleDao roleDao;
    private  ExecutorService executorService ;
    private LiveData<List<Role>> getAllRoleAdmin;

    public RoleRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        roleDao = db.roleDao();
        getAllRoleAdmin = roleDao.getAllRolesAdmin();
    }

    public void insert(Role role) {
        executorService.execute(() -> roleDao.insert(role));
    }

    public LiveData<List<Role>> getAllRoles() {
     return getAllRoleAdmin;
    }
}
