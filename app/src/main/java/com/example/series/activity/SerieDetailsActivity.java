package com.example.series.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.series.R;
import com.example.series.api.SerieDescriptionService;
import com.example.series.api.SerieService;
import com.example.series.api.ServiceFactory;
import com.example.series.api.data.ActorList;
import com.example.series.api.data.EpisodeList;
import com.example.series.api.data.SerieDescription;
import com.example.series.api.data.SerieDetails;
import com.example.series.api.data.SerieDetailsList;
import com.example.series.fragment.ActorsFragment;
import com.example.series.fragment.DetailsFragment;
import com.example.series.fragment.EpisodesFragment;
import com.example.series.interfaces.IListenerClick;
import com.example.series.utils.Constants;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SerieDetailsActivity extends AppCompatActivity implements IListenerClick {

    private static final String TAG = "SerieDetailsActivity";
    Boolean loading = false;
    SerieService serieService;
    Bundle args;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.name_serie_tab1)
    TextView seriesName;
    @BindView(R.id.melon_left_toolbar)
    ImageView melonLeft;
    @BindView(R.id.melon_right_toolbar)
    ImageView melonRight;

    private FragmentTabHost tabHost;
    private String token = "", titleSerie = "",imdId = "";
    private SerieDetails serieDetails;
    private SerieDescription serieDescription;
    private EpisodeList episodeSerieDataList;
    private ActorList actorList;
    private int numberSeason;
    private int id = 0;

    public Boolean getLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading = loading;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_series_details_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seriesName = findViewById(R.id.name_serie_tab1);

        btnBack.setVisibility(View.VISIBLE);
        melonLeft.setVisibility(View.GONE);
        melonRight.setVisibility(View.VISIBLE);


        serieService = ServiceFactory.createSerieService();
        Intent intent = getIntent();
        args = intent.getBundleExtra("serie");
        id = args.getInt("id");
        titleSerie = args.getString("seriesName");
        seriesName.setText(titleSerie);


        tabHost = findViewById(android.R.id.tabhost);
        getToken();

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Detalles"), DetailsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Episodios"), EpisodesFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Actores"), ActorsFragment.class, null);

        navigateToFragmentDetails(this.token, this.id);


        tabHost.setOnTabChangedListener(s -> {
            switch (s) {
                case "tab1":
                    navigateToFragmentDetails(this.token, this.id);
                    break;
                case "tab2":
                    navigateToFragmentEpisodes(this.token, this.id, 1);
                    break;
                case "tab3":
                    navigateToFragmentActors(this.token, this.id);
                    break;
            }

        });

    }


    private void navigateToFragmentDetails(String token, int id) {
        setLoading(true);
/*        if (tabHost != null) {
            tabHost.setActivated(false);
            tabHost.setEnabled(false);
            tabHost.setPressed(false);
            tabHost.dispatchSetActivated(false);
            tabHost.dispatchSetSelected(false);
        }*/
        Call<SerieDetailsList> serieDetailsListCall = serieService.getDetailsSeries("Bearer " + token, id);
        serieDetailsListCall.enqueue(new Callback<SerieDetailsList>() {
            @Override
            public void onResponse(Call<SerieDetailsList> call, Response<SerieDetailsList> response) {
                Gson gson = new Gson();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    serieDetails = response.body().getData();
                    Log.d(TAG, "Response detalles de serie: " + gson.toJson(response.body().getData()));
                    args.putSerializable("details", gson.toJson(serieDetails));
                    imdId = serieDetails.getImdbId();
                    if(imdId.equals("")){
                        setLoading(false);
                        if (!getLoading()) {
                            DetailsFragment detailsFragment = new DetailsFragment();
                            detailsFragment.setArguments(args);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.layout_details_series, detailsFragment);
                            transaction.commit();
                        }
                    }else{
                        SerieDescriptionService serieDescriptionService = ServiceFactory.createSerieDescriptionService();
                        serieDescriptionService.
                                getDescriptionSerie(serieDetails.getImdbId(), Constants.OMDB_APIKEY, Constants.PLOT)
                                .enqueue(new Callback<SerieDescription>() {
                                    @Override
                                    public void onResponse(Call<SerieDescription> call, Response<SerieDescription> response) {
                                        setLoading(false);
                                        if (response.isSuccessful()) {
                                            Log.d(TAG, "Response serieDescription: " + gson.toJson(response.body()));
                                            serieDescription = response.body();

                                            if(serieDescription.getTotalSeasons().equals("N/A") || serieDescription.getTotalSeasons() == null){
                                                numberSeason = 0;
                                            }else{
                                                numberSeason = Integer.parseInt(serieDescription.getTotalSeasons());
                                            }

                                            if (!getLoading()) {
                                                args.putSerializable("description", gson.toJson(serieDescription));
                                                DetailsFragment detailsFragment = new DetailsFragment();
                                                detailsFragment.setArguments(args);
                                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                                transaction.replace(R.id.layout_details_series, detailsFragment);
                                                transaction.commit();
                                            }

                                        } else {
                                            Log.e(TAG, "Error en algún parámetro al buscar la descripcion");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SerieDescription> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                    }
                } else {
                    Log.e(TAG, "Error en algún parámetro al buscar los detalles");
                }
            }

            @Override
            public void onFailure(Call<SerieDetailsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void navigateToFragmentEpisodes(String token, int id, int season) {
        setLoading(true);
        Call<EpisodeList> episodesSerie = serieService.getEpisodes("Bearer " + token, id, season);

        episodesSerie.enqueue(new Callback<EpisodeList>() {
            @Override
            public void onResponse(Call<EpisodeList> call, Response<EpisodeList> response) {
                setLoading(false);

                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    assert response.body() != null;
                    episodeSerieDataList = response.body();
                    Log.d(TAG, "Response episodios " + gson.toJson(response.body()));
                    args.putSerializable("episodes", gson.toJson(episodeSerieDataList));
                    args.putInt("numberSeason", numberSeason);
                    args.putInt("position",season);
                    args.remove("notFound");
                    if (!getLoading()) {
                        EpisodesFragment episodesFragment = new EpisodesFragment();
                        episodesFragment.setArguments(args);
                        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.layout_episodes_series, episodesFragment);
                        transaction2.commit();
                    }
                } else {
                    if(response.code()==404){
                        if (!getLoading()) {
                            args.putSerializable("notFound","notFound");
                            EpisodesFragment episodesFragment = new EpisodesFragment();
                            episodesFragment.setArguments(args);
                            FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                            transaction2.replace(R.id.layout_episodes_series, episodesFragment);
                            transaction2.commit();
                        }
                    }
                    Log.e(TAG, "Error en algún parámetro al buscar episodios");
                }
            }

            @Override
            public void onFailure(Call<EpisodeList> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void navigateToFragmentActors(String token, int id) {
        setLoading(true);
        Call<ActorList> actorsDataListCall = serieService.getActors("Bearer " + token, id);

        actorsDataListCall.enqueue(new Callback<ActorList>() {
            @Override
            public void onResponse(Call<ActorList> call, Response<ActorList> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    assert response.body() != null;
                    actorList = response.body();

                    args.putSerializable("actors", gson.toJson(actorList));
                    if (!getLoading()) {
                        ActorsFragment actorsFragment = new ActorsFragment();
                        actorsFragment.setArguments(args);
                        FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                        transaction3.replace(R.id.layout_actors_series, actorsFragment);
                        transaction3.commit();
                    }

                } else {
                    if(response.code()==404){
                        if (!getLoading()) {
                            args.putString("notFound","notFound");
                            ActorsFragment actorsFragment = new ActorsFragment();
                            actorsFragment.setArguments(args);
                            FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                            transaction3.replace(R.id.layout_actors_series, actorsFragment);
                            transaction3.commit();
                        }
                    }
                    Log.e(TAG, "Error en algún parámetro al buscar actores");
                }
            }

            @Override
            public void onFailure(Call<ActorList> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void getToken() {
        SharedPreferences mPrefs = getSharedPreferences(Constants.NAME_PREFS, Activity.MODE_PRIVATE);
        token = mPrefs.getString("token", null);
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    public void getPositionClicked(int pos) {
        navigateToFragmentEpisodes(this.token, this.id, pos);
    }

}
