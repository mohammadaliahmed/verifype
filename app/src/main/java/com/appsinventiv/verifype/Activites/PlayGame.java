package com.appsinventiv.verifype.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.ScoreAdapter;
import com.appsinventiv.verifype.Models.ApiResponse;
import com.appsinventiv.verifype.Models.Compromise;
import com.appsinventiv.verifype.Models.FraudProfile;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.AppConfig;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.appsinventiv.verifype.Utils.UserClient;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayGame extends AppCompatActivity {


    Button getStarted,tutorial;
    RecyclerView recycler;
    ScoreAdapter adapter;
    private List<Compromise> compromiseList = new ArrayList<>();
    TextView detailedDescription, scoreDescription, score;
    public static FraudProfile fraudProfile = null;
    ImageView scoreImage;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        this.setTitle("Play game");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        tutorial = findViewById(R.id.tutorial);
        getStarted = findViewById(R.id.getStarted);
        scoreImage = findViewById(R.id.scoreImage);
        recycler = findViewById(R.id.recycler);
        detailedDescription = findViewById(R.id.detailedDescription);
        scoreDescription = findViewById(R.id.scoreDescription);
        score = findViewById(R.id.score);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ScoreAdapter(this, compromiseList);
        recycler.setAdapter(adapter);


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.showToast("Development in progress");
            }
        });
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.showToast("Development in progress");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        updateData();
    }

    public void updateData() {
        if (fraudProfile != null) {
            compromiseList = fraudProfile.getFraudProfileResults().getCompromise();
            if (compromiseList != null && compromiseList.size() > 0) {
                adapter.setItemList(compromiseList);
            }
            if (fraudProfile.getFraudProfileResults().getScore() != null) {
                mDatabase.child("Scores").child(SharedPrefs.getUser().getPhone()).
                        child(""+System.currentTimeMillis()).setValue(fraudProfile.getFraudProfileResults());
                score.setText("Overall Score: " + fraudProfile.getFraudProfileResults().getScore().getScore());
                scoreDescription.setText("Remarks: " + fraudProfile.getFraudProfileResults().getScore().getScoreDescription());
                detailedDescription.setText( fraudProfile.getFraudProfileResults().getScore().getDetailedDesciption());
                setUpScoreImage(Integer.parseInt(fraudProfile.getFraudProfileResults().getScore().getScore()));
            }
        }
    }

    private void setUpScoreImage(int score) {
        if(score>0&& score<=250){
            Glide.with(this).load(R.drawable.very_poor).into(scoreImage);
        }else if(score>251&& score<=500){
            Glide.with(this).load(R.drawable.poor).into(scoreImage);
        }else if(score>501&& score<=600){
            Glide.with(this).load(R.drawable.average).into(scoreImage);
        }else if(score>601&& score<=700){
            Glide.with(this).load(R.drawable.fair).into(scoreImage);
        }else if(score>701&& score<=800){
            Glide.with(this).load(R.drawable.good).into(scoreImage);
        }else if(score>801&& score<=900){
            Glide.with(this).load(R.drawable.great).into(scoreImage);
        }else if(score>901){
            Glide.with(this).load(R.drawable.excellent).into(scoreImage);
        }else {

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