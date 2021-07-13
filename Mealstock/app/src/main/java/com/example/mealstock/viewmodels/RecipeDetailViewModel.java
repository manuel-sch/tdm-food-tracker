package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.models.Recipe;

public class RecipeDetailViewModel extends AndroidViewModel {

    private final String TAG = RecipeDetailViewModel.class.getSimpleName();

    private final FireBaseRepository fireBaseRepository;

    private MutableLiveData<Recipe> currentRecipe;

    public RecipeDetailViewModel(@NonNull Application application) {
        super(application);
        currentRecipe = new MutableLiveData<>();
        fireBaseRepository = new FireBaseRepository();
    }

    public LiveData<Recipe> getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe.postValue(currentRecipe);
    }
}
