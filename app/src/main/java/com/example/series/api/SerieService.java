package com.example.series.api;

import com.example.series.api.data.ActorList;
import com.example.series.api.data.EpisodeList;
import com.example.series.api.data.SerieDetailsList;
import com.example.series.api.data.SerieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SerieService {

    @GET("search/series")
    Call<SerieList> getSeries(@Header("Authorization") String authorization, @Query("name") String name);

    @GET("series/{id}")
    Call<SerieDetailsList> getDetailsSeries(@Header("Authorization") String authorization, @Path("id") int id);

    @GET("series/{id}/episodes/query")
    Call<EpisodeList> getEpisodes(@Header("Authorization") String authorization, @Path("id") int id, @Query("airedSeason") int season);

    @GET("series/{id}/actors")
    Call<ActorList> getActors(@Header("Authorization") String authorization, @Path("id") int id);

}
