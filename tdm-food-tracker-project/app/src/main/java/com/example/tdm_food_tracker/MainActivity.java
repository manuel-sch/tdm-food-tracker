package com.example.tdm_food_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.tdm_food_tracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RequestTester reqTester;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = activityMainBinding.getRoot();
        setContentView(viewRoot);
        AppInfoConstants utilConstants = new AppInfoConstants(this);
        reqTester = new RequestTester(this);
    }

    public void testQueue(View view) {

        reqTester.testBarcodeSearchQueueOpenFoodFacts();
        //reqTester.testProductNameSearchQueueOpenFoodFacts();
    }


}