package com.example.project_prm.Repository;

import android.app.Application;

import com.example.project_prm.Dao.OrderDao;
import com.example.project_prm.Dao.ProductDao;
import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.Database.ClothingDatabase;

public class StatisticRepository {
    private OrderDao orderDao;
    private ProductDao productDao;
    private UserDao userDao;

    public StatisticRepository(Application application) {
        ClothingDatabase db = ClothingDatabase.getInstance(application);
        orderDao = db.orderDao();
        productDao = db.productDao();
        userDao = db.userDao();
    }

    public int getTodayOrders() {
        return orderDao.getTodayOrders();
    }

    public int getTotalOrders() {
        return orderDao.getTotalOrders();
    }

    public int getTotalProducts() {
        return productDao.getTotalProducts();
    }

    public int getTotalAccounts() {
        return userDao.getTotalAccounts();
    }
}

