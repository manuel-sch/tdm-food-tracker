package com.example.tdm_food_tracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// https://developer.android.com/training/volley
public class NetworkDataTransmitterSingleton {

    private static NetworkDataTransmitterSingleton instance = null;
    private RequestQueue queue;
    private static Context queueContext;
    private ImageLoader imageLoader;


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

    public void cancelAllRequestsForContext(Context context) {
        if (instance != null)
            queue.cancelAll(context.getClass().getSimpleName());
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

    public void requestJsonObjectResponseForJsonRequestWithContext(JsonRequest jsonReq, Context context) {
        final String tag = context.getClass().getSimpleName();
        Log.d(tag, "requestJsonObjectResponseForJsonRequestWithContext: " + jsonReq.getJsonObject().toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(jsonReq.getRequestMethod(), jsonReq.getUrl(), jsonReq.getJsonObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization",  "Token token=" + UrlRequestConstants.API_KEY_FOODREPO);
                //headers.put("Authorization", "UrlRequestConstants.API_KEY_FOODREPO");
                //params.put("Authorization",  "Token");
                //params.put("token",  UrlRequestConstants.API_KEY_FOODREPO);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        jsonObjectRequest.setTag(tag);
        queue.add(jsonObjectRequest);
    }

    public static NetworkDataTransmitterSingleton getInstance(Context queueContext) {
        if (instance == null)
            instance = new NetworkDataTransmitterSingleton(queueContext);
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(queueContext.getApplicationContext());
        }
        return queue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }


}
