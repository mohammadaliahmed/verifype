package com.appsinventiv.verifype.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.PsychologyQuestionsAdapter;
import com.appsinventiv.verifype.Models.ApiResponse;
import com.appsinventiv.verifype.Models.FraudProfile;
import com.appsinventiv.verifype.Models.PsychologyQuestion;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.AppConfig;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.appsinventiv.verifype.Utils.UserClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PsychologyQuestions extends AppCompatActivity {

    RecyclerView recycler;
    private List<PsychologyQuestion> questions = new ArrayList<>();
    PsychologyQuestionsAdapter adapter;
    public static HashMap<String, List<String>> finalMap = new HashMap<>();
    Button submit;
    private HashMap<String, String> locationMap = new HashMap<>();
    private HashMap<String, String> deviceMap = new HashMap<>();
    private HashMap<String, String> userMap = new HashMap<>();
    ProgressBar progress;
    private double lng;
    private double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        getPermissions();
        this.setTitle("Psychology Questions");
        progress = findViewById(R.id.progress);
        recycler = findViewById(R.id.recycler);
        submit = findViewById(R.id.submit);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new PsychologyQuestionsAdapter(this, questions);
        recycler.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });

        getDataFromServer();


    }


    private void callApi() {
        progress.setVisibility(View.VISIBLE);

        Gson gson = new Gson();

        locationMap.put("latitude", ""+lat);
        locationMap.put("longitude", ""+lng);


        deviceMap.put("phone_type", "android");
        deviceMap.put("manufacturer", Build.MANUFACTURER);
        deviceMap.put("model", Build.MODEL);
        deviceMap.put("release_year", "");


        userMap.put("fname", SharedPrefs.getUser().getName());
        userMap.put("lname", SharedPrefs.getUser().getName());
        userMap.put("email", SharedPrefs.getUser().getEmail());
        userMap.put("phone", SharedPrefs.getUser().getPhone());


        HashMap<String, Object> apiMap = new HashMap<>();
        apiMap.put("psychology_questions", finalMap);
        apiMap.put("location", locationMap);
        apiMap.put("user_details", userMap);
        apiMap.put("device_details", deviceMap);

        JsonElement abc = gson.toJsonTree(apiMap);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        Call<ApiResponse> call = getResponse.score(abc);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    FraudProfile fraudProfile = response.body().getResult().getFraudProfile();
                    if (fraudProfile != null) {
                        PlayGame.fraudProfile = fraudProfile;
                        startActivity(new Intent(PsychologyQuestions.this,PlayGame.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0].equalsIgnoreCase("android.permission.ACCESS_FINE_LOCATION") && grantResults[0] == 0) {
            Intent intent = new Intent(PsychologyQuestions.this, GPSTrackerActivity.class);
            startActivityForResult(intent, 1);
        }
    }
    private void getPermissions() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    Intent intent = new Intent(PsychologyQuestions.this, GPSTrackerActivity.class);
                    startActivityForResult(intent, 1);
//                    CommonUtils.showToast("granted");
                }
            }
        }
        return true;
    }

    private void getDataFromServer() {
        progress.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        Call<ApiResponse> call = getResponse.psycho();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progress.setVisibility(View.GONE);

                if (response.body() != null && response.body().getResult() != null) {
                    questions = response.body().getResult().getPsychologyQuestions();
                    if (questions != null && questions.size() > 0) {
                        adapter.setItemList(questions);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                lng = extras.getDouble("Longitude");
                lat = extras.getDouble("Latitude");


            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}