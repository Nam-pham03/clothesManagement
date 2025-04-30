package com.example.project_prm.Database;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper {
    private static final String DB_NAME = "DB.db"; // Đổi tên đúng với file trong assets
    private static final String DB_PATH = "/data/data/com.example.project_prm/databases/";

    public static void copyDatabaseFromAssets(Context context) {
        try {
            File databaseFolder = new File(DB_PATH);
            if (!databaseFolder.exists()) {
                databaseFolder.mkdirs(); // Tạo thư mục nếu chưa có
            }

            File databaseFile = new File(DB_PATH + DB_NAME);
            if (!databaseFile.exists()) {
                Log.d("DB_COPY", "Database không tồn tại, đang copy từ assets...");
                InputStream inputStream = context.getAssets().open(DB_NAME);
                OutputStream outputStream = new FileOutputStream(databaseFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                Log.d("DB_COPY", "Copy database thành công!");
            } else {
                Log.d("DB_COPY", "Database đã tồn tại, không cần copy.");
            }
        } catch (Exception e) {
            Log.e("DB_COPY", "Lỗi copy database: " + e.getMessage());
        }
    }
}
