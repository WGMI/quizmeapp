package com.example.quizmeapp.Network;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiClient {

    @POST("v1/completions")
    Call<JSONObject> request(@Header("Authorization") String token, @Body JSONObject params);
}
