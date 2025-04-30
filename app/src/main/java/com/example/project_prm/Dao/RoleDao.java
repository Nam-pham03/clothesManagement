package com.example.project_prm.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.Role;
import java.util.List;

@Dao
public interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Role role);

    @Update
    void update(Role role);

    @Delete
    void delete(Role role);

    @Query("SELECT * FROM role WHERE id = :roleId")
    Role getRoleById(int roleId);

    @Query("SELECT * FROM role")
    LiveData<List<Role>> getAllRolesAdmin();


}
