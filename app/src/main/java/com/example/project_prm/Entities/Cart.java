package com.example.project_prm.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity(tableName = "cart")

public class Cart implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int user_id;
    private int product_id;
    private int quantity;
    private String created_at;
    public double price;

    public Cart() {
    }

    public Cart(int id, int user_id, int product_id, int quantity, String created_at, double price) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.created_at = created_at;
        this.price = price;
    }


    public Cart( int user_id, int product_id, int quantity, String created_at, double price) {

        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.created_at = created_at;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
