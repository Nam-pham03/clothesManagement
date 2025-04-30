package com.example.project_prm.Entities;

import androidx.room.Embedded;
import androidx.room.Ignore;

import java.io.Serializable;

public class CartWithProduct implements Serializable {
    @Embedded
    private Cart cart;

    private String productName;
    private double productPrice;
    private int productStock;

    public CartWithProduct() {
    }

    @Ignore
    public CartWithProduct(Cart cart, String productName, double productPrice, int productStock) {
        this.cart = cart;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock; // Gán giá trị stock
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

}