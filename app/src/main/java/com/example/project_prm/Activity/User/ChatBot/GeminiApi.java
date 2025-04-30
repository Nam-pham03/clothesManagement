package com.example.project_prm.Activity.User.ChatBot;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GeminiApi {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyDbP_Km_8FeZ3fl3CAg4z-YcLTvH1zih3k")
    Call<GeminiResponse> generateResponse(@Body GeminiRequest request);
}
