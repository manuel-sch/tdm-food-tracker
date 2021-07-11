package com.example.mealstock.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealstock.R;
import com.example.mealstock.activities.MainActivity;
import com.example.mealstock.constants.AppInfoConstants;
import com.example.mealstock.fragments.ProductDetailFragment;
import com.example.mealstock.fragments.ProductRemoteSearchFragment;
import com.example.mealstock.fragments.ProductScanFragment;
import com.example.mealstock.models.Product;
import com.example.mealstock.models.Recipe;
import com.example.mealstock.utils.JsonHandler;
import com.example.mealstock.utils.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://developer.android.com/training/volley
public class NetworkDataTransmitterSingleton {

    private static final String TAG = NetworkDataTransmitterSingleton.class.getSimpleName();
    private static NetworkDataTransmitterSingleton instance = null;
    private static boolean recipeSearchedWithProductName = false;
    private static Context queueContext;
    private final ImageLoader imageLoader;
    private RequestQueue queue;
    private static MainActivity mainActivity;


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
        mainActivity = (MainActivity) context;

    }

    public static synchronized NetworkDataTransmitterSingleton getInstance(Context queueContext) {
        if (instance == null)
            instance = new NetworkDataTransmitterSingleton(queueContext);
        return instance;
    }

    public void cancelAllRequestsForContext(Context context) {
        if (instance != null)
            queue.cancelAll(context.getClass().getSimpleName());
    }

    public void requestJsonObjectResponseForJsonRequestWithContext(JsonRequest jsonReq) {

        mainActivity.setProgressBarVisibilityWithBool(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(jsonReq.getHttpMethod(), jsonReq.getUrl(), jsonReq.getJsonObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (jsonReq.getRequestMethod() == RequestMethod.BARCODE_SEARCH) {
                        Log.d(TAG, "onResponse: " + response.toString(1));
                        handleBarcodeSearchResponse(response);
                    } else if (jsonReq.getRequestMethod() == RequestMethod.PRODUCT_NAME) {
                        Log.d(TAG, "onResponse: " + response.getJSONArray("products").getJSONObject(0).toString(1));
                        handleProductNameSearchResponse(response);

                    } else if (jsonReq.getRequestMethod() == RequestMethod.RECIPE_SEARCH) {
                        //Log.d(TAG, "onResponse: " + response.toString(1));
                        handleRecipeSearchResponse(response);

                    } else {
                        String errorMessage = "Angegebene Requestmethode ist ungültig!";
                        Log.e(TAG, "onResponse: " + errorMessage);
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
                // Log.d(TAG, "getHeaders: " + AppInfoConstants.getAppName() + " " + AppInfoConstants.getAppVersion());
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
            queue = Volley.newRequestQueue(queueContext.getApplicationContext());
        }
        return queue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private void handleBarcodeSearchResponse(JSONObject response) {
        try {
            if (response.getString("status_verbose").equals("product found")) {

                Fragment navHostFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
                Log.d(TAG, "handleBarcodeSearchResponse: " + navHostFragment.getChildFragmentManager().getFragments());
                ProductScanFragment scanFragment = (ProductScanFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                if (scanFragment != null) {
                    Product newProduct = JsonHandler.parseJsonObjectWithSingleProductToProduct(response);
                    scanFragment.setBarcodeProduct(newProduct);
                    scanFragment.showProductAddDialog();
                }
            } else {
                String errorMessage = "Kein Produkt zum Code gefunden!";
                Log.e(TAG, "handleBarcodeSearchResponse: " + errorMessage);
                mainActivity.showShortSnackBarWithText(errorMessage);
            }
            mainActivity.setProgressBarVisibilityWithBool(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handleProductNameSearchResponse(JSONObject response) {
        try {
            if (response.getJSONArray("products").length() != 0) {
                Fragment navHostFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
                ProductRemoteSearchFragment searchFragment = (ProductRemoteSearchFragment) navHostFragment.getChildFragmentManager().findFragmentByTag("ProductRemoteSearch");
                if (searchFragment != null) {
                    List<Product> foundProducts = JsonHandler.parseJsonArrayWithMultipleProductsToProductList(response);
                    searchFragment.setCurrentProducts(foundProducts);
                }
                mainActivity.setProgressBarVisibilityWithBool(false);
            } else {
                String errorMessage = "Keine Produkte zum Namen gefunden!";
                Log.e(TAG, "handleBarcodeSearchResponse: " + errorMessage);
                mainActivity.showShortSnackBarWithText(errorMessage);
                mainActivity.setProgressBarVisibilityWithBool(false);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handleRecipeSearchResponse(JSONObject response) {
        try {
            Fragment navHostFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
            //Log.d(TAG, "handleRecipeSearchResponse: " + navHostFragment.getParentFragmentManager().findFragmentByTag("ProductDetail"));
            ProductDetailFragment detailFragment = (ProductDetailFragment) navHostFragment.getParentFragmentManager().findFragmentByTag("ProductDetail");
            if (detailFragment != null) {
                Log.d(TAG, "handleRecipeSearchResponse: " + recipeSearchedWithProductName);
                if (response.getJSONArray("hits").length() != 0) {
                    setRecipesInDetailFragmentViewmodel(response, detailFragment);
                    detailFragment.setRecipesFoundInViewModel(true);
                } else if (!recipeSearchedWithProductName) {
                    searchRecipesWithProductName(detailFragment);
                }
                else{
                    Log.d(TAG, "handleRecipeSearchResponse: " + "nichts jefunden");
                    detailFragment.setRecipesFoundInViewModel(false);
                }

            }
            mainActivity.setProgressBarVisibilityWithBool(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setRecipesInDetailFragmentViewmodel(JSONObject response, ProductDetailFragment detailFragment){
        try {
            recipeSearchedWithProductName = false;
            //Log.d(TAG, "onResponse: " + response.getJSONArray("hits").getJSONObject(0).toString(1));
            List<Recipe> foundRecipes = null;
            foundRecipes = JsonHandler.parseJsonArrayWithMultipleRecipesToRecipeList(response);
            Log.d(TAG, "setRecipesInDetailFragmentViewmodel: " + "Es wurden Rezepte gefunden!");
            //Log.d(TAG, "setRecipesInDetailFragmentViewmodel: " + foundRecipes);
            detailFragment.setRecipes(foundRecipes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void searchRecipesWithProductName(ProductDetailFragment detailFragment){
        recipeSearchedWithProductName = true;
        detailFragment.setProductInformationToSearchForInRecipesWithProductName();
        Log.d(TAG, "searchRecipesWithProductName: " + "No recipes found, searching with Product Name now.");
    }


}
