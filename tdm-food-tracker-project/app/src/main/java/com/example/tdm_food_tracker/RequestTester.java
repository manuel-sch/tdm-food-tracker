package com.example.tdm_food_tracker;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONObject;

public class RequestTester {

    private final Context context;
    private NetworkDataTransmitterSingleton dataTransmitter;
    private final JsonHandlerSingleton jsonHandler;

    public RequestTester(Context context){
        this.context = context;
        this.jsonHandler = JsonHandlerSingleton.getInstance(context);
        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(AppInfoConstants.getAppContext());
    }

    public void testSearchQueueOpenFoodFacts(){

        String testUrl1 = UrlRequestConstants.OPENFOODFACTS_GET_PRODUCT_WITH_BARCODE;
        String nutellaBarcode = "3017620422003";
        String combinedUrl = testUrl1 + nutellaBarcode + ".json";
        JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, null);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, context);
    }

    public void testSearchQueueFoodRepo(){

        String testUrl_1 = UrlRequestConstants.FOODREPO_POST_PRODUCT_SEARCH;
        //JSONObject searchJsonObject = jsonHandler.parseJsonFileFromAssetsToJsonObject("search_test_01.json");
        JSONObject searchJsonObject = jsonHandler.parseJsonFileFromAssetsToJsonObject("search_test_01.json");
        JsonRequest jsonReq = new JsonRequest(testUrl_1, Request.Method.POST, searchJsonObject);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, context);
    }

    public void testBarcodesQueueFoodRepo(){
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(AppInfoConstants.getAppContext());

        String testUrl_1 = UrlRequestConstants.FOODREPO_GET_BARCODES;
        JsonRequest jsonReq = new JsonRequest(testUrl_1, Request.Method.GET, null);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, context);
    }


    public void testQueueSpoonacular(){
        String testUrl_1 = UrlRequestConstants.SPOONACULAR_PRODUCT_SEARCH + "zott monte";
        String test_upc = "041631000564";
        String monte_upc = "EAN 42203421";
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(AppInfoConstants.getAppContext());
        /*
        dataTransmitter.requestStringResponseForUrlWithContext
                (testUrl_1 + "&" + UrlRequestConstants.API_KEY , this);
        */

        dataTransmitter.requestStringResponseForUrlWithContext
                ("https://api.spoonacular.com/food/products/upc/" + monte_upc +
                        "?" + UrlRequestConstants.API_KEY_SPOONACULAR , context);
    }

}
