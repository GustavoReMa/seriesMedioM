package com.example.series;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.series.activity.SerieSearchActivity;
import com.example.series.api.ServiceFactory;
import com.example.series.api.UserService;
import com.example.series.api.data.LoginData;
import com.example.series.api.data.UserToken;
import com.example.series.utils.Constants;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.rotateloading_login)
    RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_login)
    public void login() {
        rotateLoading.start();
        UserService userService = ServiceFactory.createUserService();
        Call<UserToken> userTokenCall = userService.login(new LoginData("PPDZ39EGKOEHNR3R",
                "JOEZYXMFGR0RDBXA", "tavromero2yu"));

        userTokenCall.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                rotateLoading.stop();
                final UserToken userToken = response.body();
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    Log.d(TAG, "Response token: " + gson.toJson(Objects.requireNonNull(userToken).token));
                    String token = gson.toJson(userToken.token);
                    Log.d(TAG,"Token con comillas: " + token);
                    token = token.substring(1,token.length()-1);
                    Log.d(TAG,"Token sin comillas: " + token);

                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAME_PREFS, Activity.MODE_PRIVATE);
                    //sharedPreferences.edit().putString(Constants.PREFS_KEY_TOKEN, gson.toJson(userToken.token)).apply();
                    sharedPreferences.edit().putString(Constants.PREFS_KEY_TOKEN, token).apply();
                    Intent intent = new Intent(MainActivity.this, SerieSearchActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, R.string.wrong_username_password, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}


