package com.appsinventiv.verifype.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.appsinventiv.verifype.Utils.UserClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayGame extends AppCompatActivity {


    Button getStarted;
    RecyclerView recycler;
    ScoreAdapter adapter;
    private List<Compromise> compromiseList = new ArrayList<>();
    TextView detailedDescription, scoreDescription, score;
    CardView scoreLayout;
    public static FraudProfile fraudProfile = null;

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
        getStarted = findViewById(R.id.getStarted);
        scoreLayout = findViewById(R.id.scoreLayout);
        recycler = findViewById(R.id.recycler);
        detailedDescription = findViewById(R.id.detailedDescription);
        scoreDescription = findViewById(R.id.scoreDescription);
        score = findViewById(R.id.score);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayGame.this, PsychologyQuestions.class));
            }
        });
        adapter = new ScoreAdapter(this, compromiseList);
        recycler.setAdapter(adapter);


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
                scoreLayout.setVisibility(View.VISIBLE);
                score.setText("Score: " + fraudProfile.getFraudProfileResults().getScore().getScore());
                scoreDescription.setText("Remarks: " + fraudProfile.getFraudProfileResults().getScore().getScoreDescription());
                detailedDescription.setText("Score: " + fraudProfile.getFraudProfileResults().getScore().getDetailedDesciption());
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