package com.example.tdm_food_tracker.utils;

import android.content.Context;

import com.android.volley.Request;
import com.example.tdm_food_tracker.network.JsonRequest;
import com.example.tdm_food_tracker.network.NetworkDataTransmitterSingleton;
import com.example.tdm_food_tracker.constants.AppInfoConstants;
import com.example.tdm_food_tracker.constants.UrlRequestConstants;

public class RequestTester {

    private final Context context;
    private NetworkDataTransmitterSingleton dataTransmitter;

    public RequestTester(Context context){
        this.context = context;
        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(AppInfoConstants.getAppContext());
    }

    public void testBarcodeSearchQueueOpenFoodFacts(){

        String testUrl1 = UrlRequestConstants.OPENFOODFACTS_GET_PRODUCT_WITH_BARCODE;
        String nutellaBarcode = "3017620422003";
        String combinedUrl = testUrl1 + nutellaBarcode + ".json";
        JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.BARCODE, null);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, context);
    }

    public void testProductNameSearchQueueOpenFoodFacts(){

        String testUrl1 = UrlRequestConstants.OPENFOODFACTS_SEARCH_PRODUCT_WTIH_PRODUCT_NAME;
        String productName = "nutella";
        String combinedUrl = testUrl1 + productName;
        JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.PRODUCT_NAME,null);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, context);
    }

}
