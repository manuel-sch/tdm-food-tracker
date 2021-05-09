package com.example.tdm_food_tracker;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class JsonHandlerSingleton {

    private static JsonHandlerSingleton instance = null;
    private static Context context;
    final String TAG;

    private JsonHandlerSingleton(Context _context){
        context = _context;
        TAG = context.getClass().getSimpleName();
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

    public Product parseJsonObjectToProduct(JSONObject jsonObject) throws JSONException {

        JSONObject productJsonObject = jsonObject.getJSONObject("product");

        String barcode = getBarcodeFromResponseJsonObject(jsonObject);
        String productName = getProductNameFromProductJsonObject(jsonObject);
        String genericName = getGenericNameFromProductJsonObject(jsonObject);
        String brand = getBrandFromResponseJsonObject(jsonObject);
        String imageUrl = getImageUrlFromResponseJsonObject(jsonObject);
        String[] allergens = getAllergensFromResponseJsonObject(jsonObject);
        String[] categories = getCategoriesFromResponseJsonObject(jsonObject);
        String ingredients = getIngredientsFromResponseJsonObject(jsonObject);
        String ecoScore = getEcoscoreFromResponseJsonObject(jsonObject);
        String novaGroup = getNovaGroupFromResponseJsonObject(jsonObject);
        String nutrientScore = getNutriScoreFromResponseJsonObject(jsonObject);
        String quantity = getQuantityFromResponseJsonObject(jsonObject);


        Product product = new Product(barcode, productName, genericName, brand,
                imageUrl, allergens, categories, ingredients,
                nutrientScore, novaGroup, ecoScore, quantity, 0);

        Log.d(TAG, "parseJsonObjectToProduct: product " + product);

        return product;

        /*
        JSONArray jsonIngredients = product.getJSONArray("ingredients_hierarchy");
        String[] ingredients = new String[jsonIngredients.length()];
        for(int i = 0; i < jsonIngredients.length(); i++){
            ingredients[i] = jsonIngredients.get(i).toString().substring(3);
            Log.d(TAG, "parseJsonObjectToProduct: ingredient: " + ingredients[i]);
        }
        */

        /*
        Falls mehr auf Ingredients eingegangen werden soll, dann über einelne Objekte aus Array
        JSONArray jsonIngredients = product.getJSONArray("ingredients");
        String[] ingredients = new String[jsonIngredients.length()];
        for(int i = 0; i < jsonIngredients.length(); i++){
            ingredients[i] = jsonIngredients.getJSONObject(i).getString("id").substring(3);
            Log.d(TAG, "parseJsonObjectToProduct: ingredients: " + ingredients[i]);

         */
        //String image_url = product.getString("image_url");
        //Log.d(TAG, "parseJsonObjectToProduct: " + image_url);
    }

    private String getQuantityFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String quantity = jsonObject.getString("product_quantity");
        Log.d(TAG, "getQuantityFromResponseJsonObject: quantity " + quantity);
        return quantity;
    }

    private String getNutriScoreFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String nutriScore = jsonObject.getString("nutriscore_grade");
        Log.d(TAG, "getNutriScoreFromResponseJsonObject: nutriScore " + nutriScore);
        return nutriScore;
    }

    private String getNovaGroupFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONObject nutriments = jsonObject.getJSONObject("nutriments");
        String novaGroup = nutriments.getString("nova-group");
        Log.d(TAG, "getNovaGroupFromResponseJsonObject: novaGroup " + novaGroup);
        return novaGroup;
    }

    private String getIngredientsFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String ingredients = jsonObject.getString("ingredients_text_de");
        Log.d(TAG, "getIngredientsFromResponseJsonObject: ingredients: " + ingredients);
        return ingredients;
    }

    private String getEcoscoreFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONObject ecoscore_data = jsonObject.getJSONObject("ecoscore_data");
        String ecoscore = ecoscore_data.getString("grade");
        Log.d(TAG, "getEcoscoreFromResponseJsonObject: " + ecoscore);
        return ecoscore;
    }

    private String[] getCategoriesFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONArray jsonCategories = jsonObject.getJSONArray("categories_hierarchy");
        String[] categories = new String[jsonCategories.length()];
        for(int i = 0; i < jsonCategories.length(); i++){
            categories[i] = jsonCategories.get(i).toString().substring(3);
            Log.d(TAG, "getCategoriesFromResponseJsonObject: allergen: " + categories[i]);
        }
        return categories;
    }

    private String[] getAllergensFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONArray jsonAllergens = jsonObject.getJSONArray("allergens_hierarchy");
        String[] allergens = new String[jsonAllergens.length()];
        for(int i = 0; i < jsonAllergens.length(); i++){
            allergens[i] = jsonAllergens.get(i).toString().substring(3);
            Log.d(TAG, "getAllergensFromResponseJsonObject: allergen: " + allergens[i]);
        }
        return allergens;
    }

    private String getImageUrlFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String image_url = jsonObject.getString("image_url");
        Log.d(TAG, "getImageUrlFromResponseJsonObject: " + image_url);
        return image_url;
    }

    private String getBrandFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String brand = jsonObject.getString("brands");
        Log.d(TAG, "getBrandFromResponseJsonObject: " + brand);
        return brand;
    }

    private String getGenericNameFromProductJsonObject(JSONObject jsonObject) throws JSONException {
        String generic_name;
        if(!jsonObject.getString("generic_name_de").isEmpty())
            generic_name = jsonObject.getString("generic_name_de");
        else if(!jsonObject.getString("generic_name_en").isEmpty())
            generic_name = jsonObject.getString("generic_name_en");
        else
            generic_name = jsonObject.getString("generic_name");
        Log.d(TAG, "getGenericNameFromProductJsonObject: " + generic_name);
        return generic_name;
    }

    private String getProductNameFromProductJsonObject(JSONObject jsonObject) throws JSONException {
        String product_name = jsonObject.getString("product_name");
        Log.d(TAG, "getProductNameFromProductJsonObject: " + product_name);
        return product_name;
    }

    private String getBarcodeFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String barcode = jsonObject.getString("code");
        Log.d(TAG, "getBarcodeFromResponseJsonObject: " + barcode);
        return barcode;
    }


    public static JsonHandlerSingleton getInstance(Context context){
        if(instance == null)
            instance = new JsonHandlerSingleton(context);
        return instance;
    }


}
