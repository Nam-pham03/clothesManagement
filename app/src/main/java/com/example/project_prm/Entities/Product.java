package com.example.project_prm.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "product")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int category_id;
    private String brand;
    private String product_code;
    private int stock;
    private String unit;
    private double sale_price;
    private double discount;
    private double dealer_price;
    private String manufacturer;
    private String image;
    private String created_at;
    private String updated_at;

    private int isDelete;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDealer_price() {
        return dealer_price;
    }

    public void setDealer_price(double dealer_price) {
        this.dealer_price = dealer_price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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



    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public  Product(){}

    public Product(String name, int category_id, String brand, String product_code, int stock,
                   String unit, double sale_price, double discount, double dealer_price,
                   String manufacturer, String image, String created_at, String updated_at, int isDelete) {
        this.name = name;
        this.category_id = category_id;
        this.brand = brand;
        this.product_code = product_code;
        this.stock = stock;
        this.unit = unit;
        this.sale_price = sale_price;
        this.discount = discount;
        this.dealer_price = dealer_price;
        this.manufacturer = manufacturer;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.isDelete = isDelete;
    }
    public Product(int id, String name, int category_id, String brand, String product_code, int stock,
                   String unit, double sale_price, double discount, double dealer_price,
                   String manufacturer, String image, String created_at, String updated_at, int isDelete) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.brand = brand;
        this.product_code = product_code;
        this.stock = stock;
        this.unit = unit;
        this.sale_price = sale_price;
        this.discount = discount;
        this.dealer_price = dealer_price;
        this.manufacturer = manufacturer;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.isDelete = isDelete;
    }
    public Product(String name, double sale_price, String brand) {
        this.name = name;
        this.sale_price = sale_price;
        this.brand = brand;
    }
    public double getFinalPrice() {
        return sale_price - (sale_price * discount / 100);
    }


}
