package com.example.project_prm.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
@Entity(tableName = "user")


public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private String phone;
    private String gmail;
    private String address;
    private String image;
    private Integer role_id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private int isDelete;
    private boolean isGoogleUser;


    public User() {
    }

    public User(int id, String username, String password, String phone, String gmail, String address, String image, Integer role_id, String created_at, String updated_at, String deleted_at, int isDelete) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.gmail = gmail;
        this.address = address;
        this.image = image;
        this.role_id = role_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.isDelete = isDelete;
    }
    public User(String username, String password, String phone, String gmail, String address, String image, Integer role_id, String created_at, String updated_at, String deleted_at, int isDelete) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.gmail = gmail;
        this.address = address;
        this.image = image;
        this.role_id = role_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.isDelete = isDelete;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public boolean isGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(boolean googleUser) {
        isGoogleUser = googleUser;
    }
}
