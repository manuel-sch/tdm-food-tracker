package com.example.mealstock.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.mealstock.constants.UrlRequestConstants;
import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.models.Product;
import com.example.mealstock.models.Recipe;
import com.example.mealstock.network.JsonRequest;
import com.example.mealstock.network.NetworkDataTransmitterSingleton;
import com.example.mealstock.utils.RequestMethod;
import com.example.mealstock.utils.ToEnglishTranslator;

import java.util.List;

public class ProductDetailViewModel extends AndroidViewModel {

    private final FireBaseRepository fireBaseRepository;
    private final NetworkDataTransmitterSingleton networkDataTransmitterSingleton;

    private ToEnglishTranslator toEnglishTranslator;

    private final Context context;

    private MutableLiveData<Product> currentDetailProduct;
    private MutableLiveData<List<Recipe>> recipesForCurrentDetailProduct;

    public ProductDetailViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
        currentDetailProduct = new MutableLiveData<>();
        fireBaseRepository = new FireBaseRepository();
        networkDataTransmitterSingleton = NetworkDataTransmitterSingleton.getInstance(application.getApplicationContext());
    }


    public LiveData<Product> getProduct() {
        if(currentDetailProduct == null)
            currentDetailProduct = new MutableLiveData<>();
        return currentDetailProduct;
    }

    public List<Recipe> getCurrentRecipes() {
        if(recipesForCurrentDetailProduct == null)
            recipesForCurrentDetailProduct = new MutableLiveData<>();
        return recipesForCurrentDetailProduct.getValue();
    }

    public LiveData<List<Recipe>> getRecipes() {
        if(recipesForCurrentDetailProduct == null)
            recipesForCurrentDetailProduct = new MutableLiveData<>();
        return recipesForCurrentDetailProduct;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipesForCurrentDetailProduct.postValue(recipes);
    }

    public void setProduct(Product product) {
        this.currentDetailProduct.postValue(product);
        setUpTranslatorAndSetProductInformationForRecipeSearch(product);
    }

    private void setUpTranslatorAndSetProductInformationForRecipeSearch(Product product){
        if(toEnglishTranslator != null)
            toEnglishTranslator.close();
        toEnglishTranslator = new ToEnglishTranslator(this, product);
        toEnglishTranslator.setTranslatedToEnglishProductInformationForRecipeSearch();
    }

    public void setProductInformationToSearchForInRecipe(String information){
        if(!information.isEmpty()){
            setRecipesByFetchingFromServer(information);
        }
    }

    private void setRecipesByFetchingFromServer(String information){
        MutableLiveData<List<Recipe>> fetchedRecipes = new MutableLiveData<>();
        String combinedUrl = UrlRequestConstants.EDAMAM_RECIPE_SEARCH + "nutella" + UrlRequestConstants.EDAMAM_RECIPE_APP_ID_APP_KEY;
        //String combinedUrl = UrlRequestConstants.EDAMAM_RECIPE_SEARCH + productInformationToSearchForInRecipe + UrlRequestConstants.EDAMAM_RECIPE_APP_ID_APP_KEY;
        JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.RECIPE_SEARCH, null);
        networkDataTransmitterSingleton.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, context);
        recipesForCurrentDetailProduct = fetchedRecipes;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        toEnglishTranslator.close();
    }
}
