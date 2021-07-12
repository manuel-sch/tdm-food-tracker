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

import com.example.mealstock.adapters.RecipeListForProductRecyclerViewAdapter;
import com.example.mealstock.databinding.FragmentProductDetailPageRecipesBinding;
import com.example.mealstock.models.Recipe;
import com.example.mealstock.viewmodels.ProductDetailViewModel;

import java.util.List;

public class ProductDetailRecipesPageFragment extends Fragment implements RecipeListForProductRecyclerViewAdapter.RecipeItemClickListener {

    private final String TAG = ProductDetailRecipesPageFragment.class.getSimpleName();

    private TextView noRecipesFoundTextView;

    private RecyclerView recyclerView;
    private RecipeListForProductRecyclerViewAdapter recyclerViewAdapter;

    private List<Recipe> currentRecipes;

    private FragmentProductDetailPageRecipesBinding viewBinding;
    private ProductDetailViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProductDetailPageRecipesBinding.inflate(inflater, container, false);
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

    private void initializeViews() {
        recyclerView = viewBinding.recyclerView;
        noRecipesFoundTextView = viewBinding.textViewRecipeListEmpty;
    }

    private void setUpRecyclerView() {
        recyclerViewAdapter = new RecipeListForProductRecyclerViewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setUpViewModelObserving() {

        ProductDetailFragment detailFragment = (ProductDetailFragment) getParentFragmentManager().findFragmentByTag("ProductDetail");
        assert detailFragment != null;
        viewModel = new ViewModelProvider(detailFragment).get(ProductDetailViewModel.class);

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if(currentRecipes != null)
                currentRecipes.clear();
            currentRecipes = recipes;
            recyclerViewAdapter.updateRecipes(recipes);
        });

        viewModel.getRecipesFound().observe(getViewLifecycleOwner(), found -> {
            Log.d(TAG, "setUpViewModelObserving: " + found);
            if(found)
                noRecipesFoundTextView.setVisibility(View.GONE);
            else
                noRecipesFoundTextView.setVisibility(View.VISIBLE);

        });

    }

    @Override
    public void onRecipeItemClick(Recipe clickedProduct) {
        Log.d(TAG, "onRecipeItemClick: " + "pressed on recipe");
    }
}
