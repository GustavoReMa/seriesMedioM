package com.example.series.api;

import com.example.series.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    public static UserService createUserService() {
        return createRetrofit().create(UserService.class);
    }

    public static SerieService createSerieService() {
        return createRetrofit().create(SerieService.class);
    }

    public static SerieDescriptionService createSerieDescriptionService() {
        return createRetrofitDescription().create(SerieDescriptionService.class);
    }

    private static Retrofit createRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    private static Retrofit createRetrofitDescription() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_DESCRIPTION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }


}
