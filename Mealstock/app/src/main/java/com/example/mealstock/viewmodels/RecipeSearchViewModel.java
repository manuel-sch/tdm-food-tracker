package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.models.Recipe;

import java.util.List;

public class RecipeSearchViewModel extends AndroidViewModel {

    private final String TAG = RecipeSearchViewModel.class.getSimpleName();

    private final FireBaseRepository fireBaseRepository;

    private MutableLiveData<List<Recipe>> currentRecipe;

    public RecipeSearchViewModel(@NonNull Application application) {
        super(application);
        currentRecipe = new MutableLiveData<>();
        fireBaseRepository = new FireBaseRepository();
    }

    public void insertRecipe(Recipe recipe){
        fireBaseRepository.insertRecipe(recipe);
    }

    public void deleteRecipe(Recipe recipe){
        fireBaseRepository.deleteRecipe(recipe.getName());
    }

    public LiveData<List<Recipe>> getCurrentRecipes() {
        return currentRecipe;
    }

    public void setCurrentRecipes(List<Recipe> currentRecipe) {
        this.currentRecipe.postValue(currentRecipe);
    }
}
