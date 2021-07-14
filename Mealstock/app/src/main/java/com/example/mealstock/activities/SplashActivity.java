package com.example.mealstock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private Runnable runnable = new Runnable(){
        public void run(){
            if(!isFinishing()){
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        }
    };

    protected void onResume(){
        super.onResume();
        handler.postDelayed(runnable,50);
    }

    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
