package com.example.tdm_food_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RequestTester reqTester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppInfoConstants utilConstants = new AppInfoConstants(this);
        reqTester = new RequestTester(this);
    }

    public void testQueue(View view) {
        reqTester.testSearchQueueOpenFoodFacts();
    }


}