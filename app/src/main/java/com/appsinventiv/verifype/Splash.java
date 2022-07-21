package com.appsinventiv.verifype;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
//                if (SharedPrefs.getUser() != null) {
//                    Intent i = new Intent(Splash.this, MainActivity.class);
//                    startActivity(i);
//                } else {
                Intent i = new Intent(Splash.this, HomeActivity.class);
                startActivity(i);
//                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
        //alias:amrozanew
        //key:amrozanew
        //pass:amrozanew

    }


}