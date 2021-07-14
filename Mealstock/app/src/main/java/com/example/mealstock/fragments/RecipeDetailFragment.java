package com.example.mealstock.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.adapters.RecipeIngredientsRecyclerViewAdapter;
import com.example.mealstock.databinding.FragmentRecipeDetailBinding;
import com.example.mealstock.models.Recipe;
import com.example.mealstock.viewmodels.RecipeDetailViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeDetailFragment extends Fragment {

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    private FragmentRecipeDetailBinding viewBinding;

    private Recipe currentRecipe;

    private RecipeDetailViewModel viewModel;

    private RecipeIngredientsRecyclerViewAdapter recyclerViewAdapter;

    private TextView nameTextView;
    private TextView cookingTimeTextView;
    private CircleImageView circleImageView;
    private RecyclerView ingredientsRecyclerView;
    private TextView instructionsTextView;
    private TextView dishTypeTextView;
    private TextView mealTypeTextView;
    private TextView quisineTypeTextView;
    private TextView quantityTextView;
    private TextView energyInKcalTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentRecipe = (Recipe) requireArguments().get("Recipe");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        View view = viewBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
        setUpRecyclerView();
        setUpViewModelObserving();
    }

    private void setUpViewModelObserving() {
        viewModel = new ViewModelProvider(this).get(RecipeDetailViewModel.class);
        viewModel.setCurrentRecipe(currentRecipe);
        Log.d(TAG, "setUpViewModelObserving: Detailrezept: " + currentRecipe);
        viewModel.getCurrentRecipe().observe(requireActivity(), currentRecipe -> {
            nameTextView.setText(currentRecipe.getName());
            Glide.with(this).load(currentRecipe.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.recipe_placeholder).into(circleImageView);
            
            cookingTimeTextView.setText(String.valueOf(currentRecipe.getTotalTimeInMinutes()));
            Log.d(TAG, "setUpViewModelObserving: " + currentRecipe.getIngredientsAsList());
            recyclerViewAdapter.updateIngredients(currentRecipe.getIngredientsAsList());
            if(!currentRecipe.getUrl().isEmpty())
                instructionsTextView.setText(currentRecipe.getUrl());
            if(!currentRecipe.getDishType().isEmpty())
                dishTypeTextView.setText(currentRecipe.getDishType());
            if(!currentRecipe.getMealType().isEmpty())
                mealTypeTextView.setText(currentRecipe.getMealType());
            if(!currentRecipe.getCuisineType().isEmpty())
                quisineTypeTextView.setText(currentRecipe.getCuisineType());
            if(!String.valueOf(currentRecipe.getQuantity()).isEmpty())
                quantityTextView.setText(String.valueOf(currentRecipe.getQuantity()));
            if(!String.valueOf(Math.round(currentRecipe.getEnergyInKcal())).isEmpty())
                energyInKcalTextView.setText(String.valueOf(Math.round(currentRecipe.getEnergyInKcal())));
        });
    }

    private void initializeViews() {
        nameTextView = viewBinding.textViewRecipeName;
        circleImageView = viewBinding.imageViewRecipe;
        cookingTimeTextView = viewBinding.textViewCookingTime;
        ingredientsRecyclerView = viewBinding.recyclerViewRecipeIngredients;
        instructionsTextView = viewBinding.textViewCookingInstructionsLink;
        dishTypeTextView = viewBinding.textViewDishType;
        mealTypeTextView = viewBinding.textViewMealType;
        quantityTextView = viewBinding.textViewQuantity;
        quisineTypeTextView = viewBinding.textViewCuisineType;
        energyInKcalTextView = viewBinding.textViewEnergyKcal;
    }

    private void setUpRecyclerView() {
        recyclerViewAdapter = new RecipeIngredientsRecyclerViewAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }
}