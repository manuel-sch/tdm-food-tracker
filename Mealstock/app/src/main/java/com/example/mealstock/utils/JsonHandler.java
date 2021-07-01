package com.example.mealstock.utils;

import android.content.Context;
import android.util.Log;

import com.example.mealstock.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JsonHandler {

    public static String TAG;

    public static Product parseJsonObjectWithSingleProductToProduct(Context context, JSONObject jsonObject) throws JSONException {
        TAG = context.getClass().getSimpleName();
        JSONObject productJsonObject = jsonObject.getJSONObject("product");
        Product product = setUpProductEntityAndGetItForProductJsonObject(productJsonObject);
        //Log.d(TAG, "parseJsonObjectToProduct: product " + product);
        return product;
    }

    public static List<Product> parseJsonArrayWithMultipleProductsToProductList(Context context, JSONObject jsonObject) throws JSONException {
        TAG = context.getClass().getSimpleName();
        List<Product> products = new ArrayList<>();
        if (jsonObject.has("products")) {
            JSONArray productsJsonArray = jsonObject.getJSONArray("products");
            for (int i = 0; i < productsJsonArray.length(); i++) {
                products.add(setUpProductEntityAndGetItForProductJsonObject(productsJsonArray.getJSONObject(i)));
            }
        } else if (jsonObject.has("product")) {
            JSONObject productJsonObject = jsonObject.getJSONObject("product");
            Product product = setUpProductEntityAndGetItForProductJsonObject(productJsonObject);
            products.add(product);
        }
        Log.d(TAG, "parseJsonArrayWithMultipleProductsToProductList: " + products);
        return products;

    }

    public static Product setUpProductEntityAndGetItForProductJsonObject(JSONObject productJsonObject) throws JSONException {


        Product product = new Product();

        product.setBarcode(getBarcodeFromResponseJsonObject(productJsonObject));
        product.setProductName(getProductNameFromProductJsonObject(productJsonObject));
        product.setGenericName(getGenericNameFromProductJsonObject(productJsonObject));
        product.setBrands(getBrandFromResponseJsonObject(productJsonObject));
        product.setImageUrl(getImageUrlFromResponseJsonObject(productJsonObject));
        product.setAllergens(getAllergensFromResponseJsonObject(productJsonObject));
        product.setCategories(getCategoriesFromResponseJsonObject(productJsonObject));
        product.setIngredients(getIngredientsFromResponseJsonObject(productJsonObject));
        product.setEcoScore(getEcoscoreFromResponseJsonObject(productJsonObject));
        product.setNovaGroup(getNovaGroupFromResponseJsonObject(productJsonObject));
        product.setNutrientLevel(getNutriScoreFromResponseJsonObject(productJsonObject));
        product.setNutritionFacts(getNutritionFactsFromResponseJsonObject(productJsonObject));
        product.setQuantity(getQuantityFromResponseJsonObject(productJsonObject));

        return product;
    }

    public static double getQuantityFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        double quantity = 0;
        if (jsonObject.has("product_quantity"))
            quantity = Double.parseDouble(jsonObject.getString("product_quantity"));
        //Log.d(TAG, "getQuantityFromResponseJsonObject: quantity " + quantity);
        return quantity;
    }

    public static String getNutriScoreFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String nutriScore = "";
        if (jsonObject.has("nutriscore_grade")) {
            nutriScore = jsonObject.getString("nutriscore_grade");
            //Log.d(TAG, "getNutriScoreFromResponseJsonObject: nutriScore " + nutriScore);
        }

        return nutriScore;
    }


    public static String getNovaGroupFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONObject nutriments = jsonObject.getJSONObject("nutriments");
        String novaGroup = "";
        if (nutriments.has("nova-group")) {
            novaGroup = nutriments.getString("nova-group");
            //Log.d(TAG, "getNovaGroupFromResponseJsonObject: novaGroup " + novaGroup);
        }

        return novaGroup;
    }

    public static String getIngredientsFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String ingredients = "";
        if (jsonObject.has("ingredients_text_de") && !jsonObject.getString("ingredients_text_de").isEmpty()) {
            ingredients = jsonObject.getString("ingredients_text_de");
            //Log.d(TAG, "getIngredientsFromResponseJsonObject: ingredients: " + ingredients);
        } else if (jsonObject.has("ingredients_text_en") && !jsonObject.getString("ingredients_text_en").isEmpty()) {
            ingredients = jsonObject.getString("ingredients_text_en");
            //Log.d(TAG, "getIngredientsFromResponseJsonObject: ingredients: " + ingredients);
        } else if (jsonObject.has("ingredients_text") && !jsonObject.getString("ingredients_text").isEmpty()) {
            ingredients = jsonObject.getString("ingredients_text");
            //Log.d(TAG, "getIngredientsFromResponseJsonObject: ingredients: " + ingredients);
        }


        return ingredients;
    }

    public static String getEcoscoreFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        JSONObject ecoscore_data = jsonObject.getJSONObject("ecoscore_data");
        String ecoscore = "";
        if (ecoscore_data.has("grade")) {
            ecoscore = ecoscore_data.getString("grade");
            //Log.d(TAG, "getEcoscoreFromResponseJsonObject: " + ecoscore);
        }


        return ecoscore;
    }

    public static String getCategoriesFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        StringBuilder categories = new StringBuilder();
        if (jsonObject.has("categories_hierarchy")) {
            JSONArray jsonCategories = jsonObject.getJSONArray("categories_hierarchy");
            for (int i = 0; i < jsonCategories.length(); i++) {
                categories.append(jsonCategories.get(i).toString().substring(3));
                if (jsonCategories.get(i) != null)
                    categories.append(",");
            }
            // Log.d(TAG, "getCategoriesFromResponseJsonObject: allergen: " + categories);
        }

        return categories.toString();
    }

    public static String getAllergensFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        StringBuilder allergens = new StringBuilder();
        if (jsonObject.has("allergens_hierarchy")) {
            JSONArray jsonAllergens = jsonObject.getJSONArray("allergens_hierarchy");
            for (int i = 0; i < jsonAllergens.length(); i++) {
                allergens.append(jsonAllergens.get(i).toString().substring(3));
                if (jsonAllergens.get(i) != null)
                    allergens.append(",");
            }
            //Log.d(TAG, "getAllergensFromResponseJsonObject: allergen: " + allergens);
        }

        return allergens.toString();
    }

    public static String getImageUrlFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String image_url = "";
        if (jsonObject.has("image_url")) {
            image_url = jsonObject.getString("image_url");
            //Log.d(TAG, "getImageUrlFromResponseJsonObject: " + image_url);
        }

        return image_url;
    }

    public static String getBrandFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String brand = "";
        if (jsonObject.has("brands")) {
            brand = jsonObject.getString("brands");
            //Log.d(TAG, "getBrandFromResponseJsonObject: " + brand);
        }

        return brand;
    }

    public static String getGenericNameFromProductJsonObject(JSONObject jsonObject) throws JSONException {
        String generic_name = "";
        if (jsonObject.has("generic_name_de") && !jsonObject.getString("generic_name_de").isEmpty()) {
                generic_name = jsonObject.getString("generic_name_de");
        } else if (jsonObject.has("generic_name_en") && !jsonObject.getString("generic_name_en").isEmpty()) {
                generic_name = jsonObject.getString("generic_name_en");
        } else if (jsonObject.has("generic_name_en") && !jsonObject.getString("generic_name_en").isEmpty()) {
                generic_name = jsonObject.getString("generic_name_en");
        } else if (jsonObject.has("generic_name") && !jsonObject.getString("generic_name").isEmpty()) {
                generic_name = jsonObject.getString("generic_name");
        }

        //Log.d(TAG, "getGenericNameFromProductJsonObject: " + generic_name);
        return generic_name;
    }

    public static String getProductNameFromProductJsonObject(JSONObject jsonObject) throws JSONException {
        String product_name = "";
        if (jsonObject.has("product_name_de") && !jsonObject.getString("product_name_de").isEmpty()) {
                product_name = jsonObject.getString("product_name_de");
        } else if (jsonObject.has("product_name_en") && !jsonObject.getString("product_name_en").isEmpty()) {
                product_name = jsonObject.getString("product_name_en");
        } else if (jsonObject.has("product_name") && !jsonObject.getString("product_name").isEmpty()) {
                product_name = jsonObject.getString("product_name");
        } else if (jsonObject.has("abbreviated_product_name") && !jsonObject.getString("abbreviated_product_name").isEmpty()) {
            product_name = jsonObject.getString("abbreviated_product_name");
        } else{
            product_name = getGenericNameFromProductJsonObject(jsonObject);
        }
        Log.d(TAG, "getProductNameFromProductJsonObject: " + product_name);



        return product_name;
    }

    public static String getBarcodeFromResponseJsonObject(JSONObject jsonObject) throws JSONException {
        String barcode = "";
        if (jsonObject.has("code")) {
            barcode = jsonObject.getString("code");
            //Log.d(TAG, "getBarcodeFromResponseJsonObject: " + barcode);
        }

        return barcode;
    }

    private static HashMap<String, String> getNutritionFactsFromResponseJsonObject(JSONObject productJsonObject) {
        HashMap<String, String> nutritionFacts = new HashMap<>();
        final String noData = "Keine Daten.";
        if (productJsonObject.has("nutriments")) {
            try {
                JSONObject nutrimentsJsonObject = productJsonObject.getJSONObject("nutriments");

                if(!nutrimentsJsonObject.has("energy-kcal_100g") && !nutrimentsJsonObject.getString("energy-kcal_100g").equals("0"))
                    nutritionFacts.put("energy-kcal_100g", nutrimentsJsonObject.getString("energy-kcal_100g") + "kcal");
                else
                    nutritionFacts.put("energy-kcal_100g", noData);

                if(!nutrimentsJsonObject.has("energy-kj_100g") && !nutrimentsJsonObject.getString("energy-kj_100g").equals("0"))
                    nutritionFacts.put("energy-kj_100g", nutrimentsJsonObject.getString("energy-kj_100g") + "kj");
                else
                    nutritionFacts.put("energy-kj_100g", noData);

                if(!nutrimentsJsonObject.has("fat_100g") && !nutrimentsJsonObject.getString("fat_100g").equals("0"))
                    nutritionFacts.put("fat_100g", nutrimentsJsonObject.getString("fat_100g") + "g");
                else
                    nutritionFacts.put("fat_100g", noData);

                if(!nutrimentsJsonObject.has("saturated-fat_100g") && !nutrimentsJsonObject.getString("saturated-fat_100g").equals("0"))
                    nutritionFacts.put("saturated-fat_100g", nutrimentsJsonObject.getString("saturated-fat_100g") + "g");
                else
                    nutritionFacts.put("saturated-fat_100g", noData);

                if(!nutrimentsJsonObject.has("carbohydrates_100g") && !nutrimentsJsonObject.getString("carbohydrates_100g").equals("0"))
                    nutritionFacts.put("carbohydrates_100g", nutrimentsJsonObject.getString("carbohydrates_100g") + "g");
                else
                    nutritionFacts.put("carbohydrates_100g", noData);

                if(!nutrimentsJsonObject.has("sugars_100g") && !nutrimentsJsonObject.getString("sugars_100g").equals("0"))
                    nutritionFacts.put("sugars_100g", nutrimentsJsonObject.getString("sugars_100g") + "g");
                else
                    nutritionFacts.put("sugars_100g", noData);

                if(!nutrimentsJsonObject.has("proteins_100g") && !nutrimentsJsonObject.getString("proteins_100g").equals("0"))
                    nutritionFacts.put("proteins_100g", nutrimentsJsonObject.getString("proteins_100g") + "g");
                else
                    nutritionFacts.put("proteins_100g", noData);

                if(!nutrimentsJsonObject.has("salt_100g") && !nutrimentsJsonObject.getString("salt_100g").equals("0"))
                    nutritionFacts.put("salt_100g", nutrimentsJsonObject.getString("salt_100g") + "g");
                else
                    nutritionFacts.put("salt_100g", noData);

                if(!nutrimentsJsonObject.has("sodium_100g") && !nutrimentsJsonObject.getString("sodium_100g").equals("0"))
                    nutritionFacts.put("sodium_100g", nutrimentsJsonObject.getString("sodium_100g") + "g");
                else
                    nutritionFacts.put("sodium_100g", noData);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return nutritionFacts;
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
