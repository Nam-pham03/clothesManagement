package com.example.project_prm.Activity.User.ChatBot;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeminiRetrofitClient {
    private static Retrofit retrofit = null;

    // Get Instance of Retrofit for Gemini API
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://generativelanguage.googleapis.com/")  // Gemini API base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
