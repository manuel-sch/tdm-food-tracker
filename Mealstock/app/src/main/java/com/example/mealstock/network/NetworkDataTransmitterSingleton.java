package com.example.mealstock.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealstock.activities.MainActivity;
import com.example.mealstock.constants.AppInfoConstants;
import com.example.mealstock.fragments.ProductInsertFragment;
import com.example.mealstock.models.Product;
import com.example.mealstock.utils.JsonHandler;
import com.example.mealstock.utils.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// https://developer.android.com/training/volley
public class NetworkDataTransmitterSingleton {

    private static NetworkDataTransmitterSingleton instance = null;
    private final Context queueContext;
    private final ImageLoader imageLoader;
    private RequestQueue queue;


    private NetworkDataTransmitterSingleton(Context context) {
        queueContext = context;
        queue = getRequestQueue();
        imageLoader = new ImageLoader(queue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static NetworkDataTransmitterSingleton getInstance(Context queueContext) {
        if (instance == null)
            instance = new NetworkDataTransmitterSingleton(queueContext);
        return instance;
    }

    public void cancelAllRequestsForContext(Context context) {
        if (instance != null)
            queue.cancelAll(context.getClass().getSimpleName());
    }

    public void requestJsonObjectResponseForJsonRequestWithContext(JsonRequest jsonReq, Context context) {
        final String TAG = context.getClass().getSimpleName();

        if (jsonReq.getJsonObject() != null)
            Log.d(TAG, "requestJsonObjectResponseForJsonRequestWithContext: " + jsonReq.getJsonObject().toString());
        if (jsonReq.getUrl() != null)
            Log.d(TAG, "requestJsonObjectResponseForJsonRequestWithContext: " + jsonReq.getUrl());

        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();

        for(Fragment frag : fragmentManager.getFragments()){
            Log.d(TAG, "requestJsonObjectResponseForJsonRequestWithContext: " + frag.getId());
        };

        MainActivity mainActivity = (MainActivity) context;
        Log.d(TAG, "requestJsonObjectResponseForJsonRequestWithContext: derzeitiges Fragment - "+ mainActivity.getSupportFragmentManager().getFragments().get(0).toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(jsonReq.getHttpMethod(), jsonReq.getUrl(), jsonReq.getJsonObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: " + response.toString(1));
                    if (response.getString("status_verbose").equals("product found")) {
                        if (jsonReq.getRequestMethod() == RequestMethod.BARCODE_SEARCH) {
                            @SuppressLint("ResourceType") Fragment navHostFragment = fragmentManager.findFragmentById(2131361993);
                            ProductInsertFragment scanFragment = (ProductInsertFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);

                            Product newProduct = JsonHandler.parseJsonObjectToProduct(context, response);
                            mainActivity.setProgressBarVisibilityWithBool(false);
                            scanFragment.setBarcodeProduct(newProduct);
                            scanFragment.showProductAddDialog();

                        } else if (jsonReq.getRequestMethod() == RequestMethod.PRODUCT_NAME) {

                        } else {
                            String errorMessage = "No Requestmethod!";
                            Log.e(TAG, "onResponse: " + errorMessage);
                            mainActivity.showShortSnackBarWithText(errorMessage);
                        }
                    } else{
                        String errorMessage = "Keine Produkte gefunden!";
                        Log.d(TAG, "onResponse: " +  errorMessage);
                        mainActivity.showShortSnackBarWithText(errorMessage);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                mainActivity.setProgressBarVisibilityWithBool(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "getHeaders: " + AppInfoConstants.getAppName() + " " + AppInfoConstants.getAppVersion());
                // Represents our Requests as Request from our Application for API Server.
                params.put("User-Agent", AppInfoConstants.getAppName() + " - Android - " + AppInfoConstants.getAppVersion());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        jsonObjectRequest.setTag(TAG);
        queue.add(jsonObjectRequest);
    }

    public void requestStringResponseForUrlWithContext(String url, Context context) {
        final String tag = context.getClass().getSimpleName();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(tag, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, "onErrorResponse: " + error.getMessage());
            }
        });
        stringRequest.setTag(tag);
        queue.add(stringRequest);
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(queueContext);
        }
        return queue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }


}
