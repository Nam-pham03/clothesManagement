package com.example.project_prm.Activity.User.VirtualRoom;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TryOnApi {
    @Headers("x-api-key: SG_f27584180111f666")
    @POST("v1/try-on-diffusion")
    Call<TryOnResponse> tryOn(@Body TryOnRequest request);
}
