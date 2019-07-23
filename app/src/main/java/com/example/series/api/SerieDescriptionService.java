package com.example.series.api;

import com.example.series.api.data.SerieDescription;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SerieDescriptionService {

    @GET(".")
    Call<SerieDescription> getDescriptionSerie(@Query("i") String imdb, @Query("apikey") String apikey, @Query("plot") String plot);

}
