package com.example.series.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerieList {

    @SerializedName("data")
    private List<Serie> data;

    public List<Serie> getData() {
        return data;
    }

    public void setData(List<Serie> data) {
        this.data = data;
    }
}
