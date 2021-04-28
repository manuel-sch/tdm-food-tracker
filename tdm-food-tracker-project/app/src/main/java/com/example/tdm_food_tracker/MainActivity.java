package com.example.tdm_food_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private NetworkDataTransmitter dataTransmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testQueue(View view) {

    }
    private void testQueueSpoonacular(){
        String testUrl_1 = UrlRequestConstants.PRODUCT_SEARCH_URL + "zott monte";
        String test_upc = "041631000564";
        String monte_upc = "EAN 42203421";
        dataTransmitter = NetworkDataTransmitter.getInstance();
        dataTransmitter.createNewRequestQueueForContext(this);
        /*
        dataTransmitter.requestStringResponseForUrlWithContext
                (testUrl_1 + "&" + UrlRequestConstants.API_KEY , this);
        */

        dataTransmitter.requestStringResponseForUrlWithContext
                ("https://api.spoonacular.com/food/products/upc/" + monte_upc + "?" + UrlRequestConstants.API_KEY_SPOONACULAR , this);
    }

}