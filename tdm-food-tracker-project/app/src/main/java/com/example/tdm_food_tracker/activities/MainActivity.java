package com.example.tdm_food_tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm_food_tracker.constants.AppInfoConstants;
import com.example.tdm_food_tracker.databinding.ActivityMainBinding;
import com.example.tdm_food_tracker.utils.RequestTester;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RequestTester reqTester;
    // ViewBinding ermöglicht direktes zugreifen auf Views aus Layout über Objekt mit .viewName
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


    public void buttonToFormClick(View view) {
        Log.d(TAG, "buttonToFormClick: Form Activity should be started now.");
        Intent intent = new Intent(this, FormEditActivity.class);
        startActivity(intent);
    }

}