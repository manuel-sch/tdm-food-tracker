package com.example.tdm_food_tracker;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonHandlerSingleton {

    private static JsonHandlerSingleton instance = null;
    private static Context context;

    private JsonHandlerSingleton(Context _context){
        context = _context;
    }

    public String parseJsonFileFromAssetsToString(String path){
        String json;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d(context.getClass().getSimpleName(), "parseJsonFileFromAssetsToString: " + json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public JSONObject parseJsonFileFromAssetsToJsonObject(String path){
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(parseJsonFileFromAssetsToString(path));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;

    }



    public static JsonHandlerSingleton getInstance(Context context){
        if(instance == null)
            instance = new JsonHandlerSingleton(context);
        return instance;
    }


}
