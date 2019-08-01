package com.example.series.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.series.R;
import com.example.series.adapter.SerieDetailsAdapter;
import com.example.series.api.SerieService;
import com.example.series.api.ServiceFactory;
import com.example.series.api.data.Serie;
import com.example.series.api.data.SerieList;
import com.example.series.utils.Constants;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerieSearchActivity extends AppCompatActivity {

    private String TAG = "SerieSearchActivity";
    private List<Serie> mDataSeries = new ArrayList<>();
    private String token = "";
    private String notFound = "";

    @BindView(R.id.edt_search_series)
    EditText edtSearchSeries;
    @BindView(R.id.btn_search_series)
    ImageView btnSeriesSearch;
    @BindView(R.id.btn_delete_text)
    ImageView btnDeleteText;
    @BindView(R.id.txt_series_notFound)
    TextView txtSeriesNotFound;
    @BindView(R.id.recycler_view_series)
    RecyclerView recyclerView;
    @BindView(R.id.rotateloading_series)
    RotateLoading rotateLoading;
    SerieDetailsAdapter serieDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        edtSearchSeries.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>49){
                    Toast.makeText(getApplicationContext(),"No puedes escribir más de 50 carácteres",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    btnDeleteText.setVisibility(View.GONE);
                    btnSeriesSearch.setVisibility(View.VISIBLE);
                }else{
                    btnDeleteText.setVisibility(View.GONE);
                    btnSeriesSearch.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @OnClick(R.id.btn_search_series)
    public void searchSeries() {
        String name = edtSearchSeries.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.enter_text, Toast.LENGTH_SHORT).show();
        } else {
            activateRotateloading();
            mDataSeries.clear();
            btnSeriesSearch.setVisibility(View.GONE);
            btnDeleteText.setVisibility(View.VISIBLE);
            txtSeriesNotFound.setText("");
            txtSeriesNotFound.setVisibility(View.GONE);
            getToken();
            SerieService serieService = ServiceFactory.createSerieService();
            Call<SerieList> seriesCall = serieService.getSeries("Bearer " + token, name);

            seriesCall.enqueue(new Callback<SerieList>() {
                @Override
                public void onResponse(Call<SerieList> call, Response<SerieList> response) {
                    deactivateRotateloading();
                    Gson gson = new Gson();
                    mDataSeries.clear();
                    if (response.isSuccessful()) {

                        if (response.body() != null) {
                            mDataSeries.addAll(response.body().getData());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            serieDetailsAdapter = new SerieDetailsAdapter(getApplicationContext(), mDataSeries);
                            recyclerView.setAdapter(serieDetailsAdapter);
                            serieDetailsAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getApplicationContext(),"No se encontro ",Toast.LENGTH_SHORT).show();
                        }
                        //Log.d(TAG, "Response: " + gson.toJson(response.body().getData()));


                    } else {
                        if (response.code() == 404){
                            notFound = getString(R.string.not_found) + name;
                            txtSeriesNotFound.setVisibility(View.VISIBLE);
                            txtSeriesNotFound.setText(notFound);
                        }
                        Log.e(TAG, "Error en algún parámetro al buscar las series");
                    }
                }

                @Override
                public void onFailure(Call<SerieList> call, Throwable t) {
                    deactivateRotateloading();
                    t.printStackTrace();
                }
            });
        }
    }


    private void activateRotateloading() {
        rotateLoading.setVisibility(View.VISIBLE);
        rotateLoading.start();
    }

    private void deactivateRotateloading() {
        rotateLoading.setVisibility(View.GONE);
        rotateLoading.stop();
    }

    @OnClick(R.id.btn_delete_text)
    public void deleteText() {
        btnSeriesSearch.setVisibility(View.VISIBLE);
        btnDeleteText.setVisibility(View.GONE);
        txtSeriesNotFound.setText("");
        txtSeriesNotFound.setVisibility(View.GONE);
        edtSearchSeries.setText("");
        mDataSeries.clear();
        serieDetailsAdapter.notifyDataSetChanged();
    }

    private void getToken() {
        SharedPreferences mPrefs = getSharedPreferences(Constants.NAME_PREFS, Activity.MODE_PRIVATE);
        token = mPrefs.getString("token", null);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}


