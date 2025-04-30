package com.example.project_prm.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("UPDATE user SET isDelete = 1 WHERE id = :userId")
    void softDeleteUser(int userId);
    @Query("UPDATE user SET isDelete = 0 WHERE id = :userId")
    void restoreUser(int userId);


    @Query("SELECT username FROM user WHERE id = :userId")
    String getUserNameByIdOrder(int userId);
    @Query("SELECT COUNT(*) FROM user where isDelete = 0")
    int getTotalAccounts();
    @Query("SELECT * FROM user WHERE id = :userId AND isDelete = 0")
    User getUserById(int userId);

    @Query("UPDATE user SET username = :username, password = :password, gmail = :email, phone = :phone, address = :address, image = :image, updated_at = :updatedAt WHERE id = :userId")
    void updateUserProfile(int userId, String username, String password, String email, String phone, String address, String image, String updatedAt);

    @Query("SELECT * FROM user WHERE username = :username AND isDelete = 0")
    User getUserByUsername(String username);

    @Query("SELECT * FROM user WHERE gmail = :email AND isDelete = 0")
    User getUserByEmail(String email);

    @Query("SELECT * FROM user WHERE username = :username AND password = :password AND isDelete = 0")
    User login(String username, String password);

    @Query("SELECT * FROM user WHERE gmail = :email AND isDelete = 0 AND isGoogleUser = 1")
    User googleLogin(String email);

    @Query("SELECT * FROM user WHERE isDelete = 0")
  List<User> getAllUsers();
    @Query("SELECT * FROM user Order By created_at desc ")
    LiveData< List<User>> getAllUsersAdmin();
    @Query("SELECT * FROM user WHERE id = :userId AND isDelete = 0")
    User getUserByIdSync(int userId);

    @Query("UPDATE user SET password = :newPassword, updated_at = :updatedAt WHERE id = :userId AND password = :oldPassword")
    int changePassword(int userId, String oldPassword, String newPassword, String updatedAt);

    @Query("SELECT * FROM user WHERE  gmail = :email AND isDelete = 0")
    User getUserForPasswordReset(String email);

    @Query("UPDATE user SET password = :newPassword, updated_at = :updatedAt WHERE id = :userId")
    void updatePassword(int userId, String newPassword, String updatedAt);

    @Query("SELECT role_id FROM User WHERE id = :userId")
    LiveData<Integer> getRoleById(int userId);



}