package com.example.tdm_food_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private Context APP_CONTEXT;

    private NetworkDataTransmitterSingleton dataTransmitter;
    private JsonHandlerSingleton jsonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APP_CONTEXT = this.getApplicationContext();
    }

    public void testQueue(View view) {
        testSearchQueueFoodRepo();
    }

    private void testSearchQueueFoodRepo(){
        jsonHandler = JsonHandlerSingleton.getInstance(this);
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(APP_CONTEXT);

        String testUrl_1 = UrlRequestConstants.FOODREPO_POST_PRODUCT_SEARCH;
        //JSONObject searchJsonObject = jsonHandler.parseJsonFileFromAssetsToJsonObject("search_test_01.json");
        JSONObject searchJsonObject = jsonHandler.parseJsonFileFromAssetsToJsonObject("search_test_01.json");
        JsonRequest jsonReq = new JsonRequest(testUrl_1, Request.Method.POST, searchJsonObject);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, this);
    }

    private void testBarcodesQueueFoodRepo(){
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(APP_CONTEXT);

        String testUrl_1 = UrlRequestConstants.FOODREPO_GET_BARCODES;
        JsonRequest jsonReq = new JsonRequest(testUrl_1, Request.Method.GET, null);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, this);
    }


    private void testQueueSpoonacular(){
        String testUrl_1 = UrlRequestConstants.SPOONACULAR_PRODUCT_SEARCH + "zott monte";
        String test_upc = "041631000564";
        String monte_upc = "EAN 42203421";
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(APP_CONTEXT);
        /*
        dataTransmitter.requestStringResponseForUrlWithContext
                (testUrl_1 + "&" + UrlRequestConstants.API_KEY , this);
        */

        dataTransmitter.requestStringResponseForUrlWithContext
                ("https://api.spoonacular.com/food/products/upc/" + monte_upc +
                        "?" + UrlRequestConstants.API_KEY_SPOONACULAR , this);
    }

}