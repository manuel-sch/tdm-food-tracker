package com.example.tdm_food_tracker.utils;

import android.content.Context;
import android.util.Log;

import com.example.tdm_food_tracker.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class JsonHandler {

    public static String TAG;

    public static String parseJsonFileFromAssetsToString(Context context, String path) {
        TAG = context.getClass().getSimpleName();
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

    public static JSONObject parseJsonFileFromAssetsToJsonObject(Context context, String path) {
        TAG = context.getClass().getSimpleName();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(parseJsonFileFromAssetsToString(context, path));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;

    }

    public static Product parseJsonObjectToProduct(Context context, JSONObject jsonObject) throws JSONException {
        TAG = context.getClass().getSimpleName();
        JSONObject productJsonObject = jsonObject.getJSONObject("product");
        Product product = setUpProductEntityAndGetItForProductJsonObject(productJsonObject);
        Log.d(TAG, "parseJsonObjectToProduct: product " + product);
        return product;

    }


    public  static Product setUpProductEntityAndGetItForProductJsonObject(JSONObject productJsonObject) throws JSONException {
        Product product = new Product();

        product.setBarcode(getBarcodeFromResponseJsonObject(productJsonObject));
        product.setProductName(getProductNameFromProductJsonObject(productJsonObject));
        product.setGenericName(getGenericNameFromProductJsonObject(productJsonObject));
        product.setBrand(getBrandFromResponseJsonObject(productJsonObject));
        product.setImageUrl(getImageUrlFromResponseJsonObject(productJsonObject));
        product.setAllergens(getAllergensFromResponseJsonObject(productJsonObject));
        product.setCategories(getCategoriesFromResponseJsonObject(productJsonObject));
        product.setIngredients(getIngredientsFromResponseJsonObject(productJsonObject));
        product.setEcoScore(getEcoscoreFromResponseJsonObject(productJsonObject));
        product.setNovaGroup(getNovaGroupFromResponseJsonObject(productJsonObject));
        product.setNutrientLevel(getNutriScoreFromResponseJsonObject(productJsonObject));
        product.setQuantity(getQuantityFromResponseJsonObject(productJsonObject));

        return product;
    }

    public  static String getQuantityFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String quantity = jsonObject.getString("product_quantity");
        Log.d(TAG, "getQuantityFromResponseJsonObject: quantity " + quantity);
        return quantity;
    }

    public  static String getNutriScoreFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String nutriScore = jsonObject.getString("nutriscore_grade");
        Log.d(TAG, "getNutriScoreFromResponseJsonObject: nutriScore " + nutriScore);
        return nutriScore;
    }

    public  static String getNovaGroupFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONObject nutriments = jsonObject.getJSONObject("nutriments");
        String novaGroup = nutriments.getString("nova-group");
        Log.d(TAG, "getNovaGroupFromResponseJsonObject: novaGroup " + novaGroup);
        return novaGroup;
    }

    public  static String getIngredientsFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String ingredients = jsonObject.getString("ingredients_text_de");
        Log.d(TAG, "getIngredientsFromResponseJsonObject: ingredients: " + ingredients);
        return ingredients;
    }

    public  static String getEcoscoreFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONObject ecoscore_data = jsonObject.getJSONObject("ecoscore_data");
        String ecoscore = ecoscore_data.getString("grade");
        Log.d(TAG, "getEcoscoreFromResponseJsonObject: " + ecoscore);
        return ecoscore;
    }

    public  static String getCategoriesFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONArray jsonCategories = jsonObject.getJSONArray("categories_hierarchy");
        StringBuilder categories = new StringBuilder();
        for (int i = 0; i < jsonCategories.length(); i++) {
            categories.append(jsonCategories.get(i).toString().substring(3));
            if (jsonCategories.get(i) != null)
                categories.append(",");
        }
        Log.d(TAG, "getCategoriesFromResponseJsonObject: allergen: " + categories);
        return categories.toString();
    }

    public  static String getAllergensFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONArray jsonAllergens = jsonObject.getJSONArray("allergens_hierarchy");
        StringBuilder allergens = new StringBuilder();
        for (int i = 0; i < jsonAllergens.length(); i++) {
            allergens.append(jsonAllergens.get(i).toString().substring(3));
            if (jsonAllergens.get(i) != null)
                allergens.append(",");
        }
        Log.d(TAG, "getAllergensFromResponseJsonObject: allergen: " + allergens);
        return allergens.toString();
    }

    public  static String getImageUrlFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String image_url = jsonObject.getString("image_url");
        Log.d(TAG, "getImageUrlFromResponseJsonObject: " + image_url);
        return image_url;
    }

    public  static String getBrandFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String brand = jsonObject.getString("brands");
        Log.d(TAG, "getBrandFromResponseJsonObject: " + brand);
        return brand;
    }

    public  static String getGenericNameFromProductJsonObject(JSONObject jsonObject) throws JSONException {
        String generic_name;
        if (!jsonObject.getString("generic_name_de").isEmpty())
            generic_name = jsonObject.getString("generic_name_de");
        else if (!jsonObject.getString("generic_name_en").isEmpty())
            generic_name = jsonObject.getString("generic_name_en");
        else
            generic_name = jsonObject.getString("generic_name");
        Log.d(TAG, "getGenericNameFromProductJsonObject: " + generic_name);
        return generic_name;
    }

    public  static String getProductNameFromProductJsonObject(JSONObject jsonObject) throws JSONException {
        String product_name = jsonObject.getString("product_name");
        Log.d(TAG, "getProductNameFromProductJsonObject: " + product_name);
        return product_name;
    }

    public  static String getBarcodeFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String barcode = jsonObject.getString("code");
        Log.d(TAG, "getBarcodeFromResponseJsonObject: " + barcode);
        return barcode;
    }

      /*
        Falls nur Igredientname aus Array geholt werden soll werden soll, dann über einelne Objekte aus Array
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
