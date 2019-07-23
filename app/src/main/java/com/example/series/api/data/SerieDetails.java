package com.example.series.api.data;

import java.io.Serializable;

public class SerieDetails implements Serializable {

    private int id;
    private String seriesName;
    private String banner;
    private String seriesId;
    private String firstAired;
    private String runtime;
    private String[] genre;
    private String overview;
    private int lastUpdated;
    private String airsDayOfWeek;
    private String airsTime;
    private String rating;
    private String imdbId;
    private int addedBy;
    private float siteRating;
    private int siteRatingCount;

    public SerieDetails(int id, String seriesName, String banner, String seriesId, String firstAired, String runtime, String[] genre, String overview, int lastUpdated, String airsDayOfWeek, String airsTime, String rating, String imdbId, int addedBy, float siteRating, int siteRatingCount) {
        this.id = id;
        this.seriesName = seriesName;
        this.banner = banner;
        this.seriesId = seriesId;
        this.firstAired = firstAired;
        this.runtime = runtime;
        this.genre = genre;
        this.overview = overview;
        this.lastUpdated = lastUpdated;
        this.airsDayOfWeek = airsDayOfWeek;
        this.airsTime = airsTime;
        this.rating = rating;
        this.imdbId = imdbId;
        this.addedBy = addedBy;
        this.siteRating = siteRating;
        this.siteRatingCount = siteRatingCount;
    }

    public int getId() {
        return id;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getBanner() {
        return banner;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public String getRuntime() {
        return runtime;
    }

    public String[] getGenre() {
        return genre;
    }

    public String getOverview() {
        return overview;
    }

    public int getLastUpdated() {
        return lastUpdated;
    }

    public String getAirsDayOfWeek() {
        return airsDayOfWeek;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public String getRating() {
        return rating;
    }

    public String getImdbId() {
        return imdbId;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public float getSiteRating() {
        return siteRating;
    }

    public int getSiteRatingCount() {
        return siteRatingCount;
    }

}
