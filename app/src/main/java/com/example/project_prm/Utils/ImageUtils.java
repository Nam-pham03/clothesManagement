package com.example.project_prm.Utils;



import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static String saveImageToInternalStorage(Context context, Bitmap bitmap, String fileName) {
        File directory = context.getFilesDir(); // Lưu vào bộ nhớ trong
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath(); // Trả về đường dẫn ảnh
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

