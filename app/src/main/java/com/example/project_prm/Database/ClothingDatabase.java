package com.example.project_prm.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.project_prm.Dao.*;
import com.example.project_prm.Entities.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        User.class, Role.class, Category.class, Product.class,
        Order.class, OrderDetail.class, Cart.class, Rating.class
}, version = 5, exportSchema = false)
public abstract class ClothingDatabase extends RoomDatabase {

    private static volatile ClothingDatabase INSTANCE;


    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract CartDao cartDao();
    public abstract RatingDao ratingDao();

    public static ClothingDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ClothingDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ClothingDatabase.class, "ClothingDatabase")
                            .createFromAsset("DB.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


//    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            databaseWriteExecutor.execute(() -> {
//                ClothingDatabase database = INSTANCE;
//                if (database != null) {
//                    ProductDao productDao = database.productDao();
//                    CategoryDao categoryDao = database.categoryDao();
//                    Category c1 = new Category("Áo");
//                    categoryDao.insert(c1);
//                }
//            });
//        }
//    };




}
