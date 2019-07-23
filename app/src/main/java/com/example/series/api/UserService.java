package com.example.series.api;

import com.example.series.api.data.LoginData;
import com.example.series.api.data.UserToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("login")
    Call<UserToken> login(@Body LoginData loginData);


}
