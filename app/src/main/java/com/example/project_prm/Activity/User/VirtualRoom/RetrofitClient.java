package com.example.project_prm.Activity.User.VirtualRoom;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.segmind.com/"; // Thay thế với URL API của bạn

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS) // Thời gian chờ kết nối
                    .writeTimeout(60, TimeUnit.SECONDS)   // Thời gian chờ khi gửi dữ liệu
                    .readTimeout(60, TimeUnit.SECONDS)    // Thời gian chờ khi nhận dữ liệu
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // Sử dụng OkHttpClient với cấu hình timeout
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
