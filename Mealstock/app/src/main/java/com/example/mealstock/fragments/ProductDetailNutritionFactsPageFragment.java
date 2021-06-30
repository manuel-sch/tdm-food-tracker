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

import com.example.mealstock.databinding.FragmentProductDetailPageNutritionFactsBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.viewmodels.ProductDetailViewModel;

import java.util.Map;
import java.util.Objects;

public class ProductDetailNutritionFactsPageFragment extends Fragment {

    private final String TAG = ProductDetailNutritionFactsPageFragment.class.getSimpleName();

    private FragmentProductDetailPageNutritionFactsBinding viewBinding;
    private ProductDetailViewModel viewModel;

    private TextView energyKjTextView;
    private TextView energyKcalTextView;
    private TextView fatTextView;
    private TextView saturatedFatTextView;
    private TextView carbohydratesTextView;
    private TextView sugarTextView;
    private TextView proteinTextView;
    private TextView saltTextView;
    private TextView sodiumTextView;
    private TextView nutritionScoreTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProductDetailPageNutritionFactsBinding.inflate(inflater, container, false);
        View view = viewBinding.getRoot();
        initializeViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModelObserving();
    }

    private void initializeViews() {
        energyKjTextView = viewBinding.textViewEnergyKjValue;
        energyKcalTextView = viewBinding.textViewEnergyKcalValue;
        fatTextView = viewBinding.textViewFatValue;
        saturatedFatTextView = viewBinding.textViewSaturatedFatValue;
        carbohydratesTextView = viewBinding.textViewCarbohydratesValue;
        sugarTextView = viewBinding.textViewSugarValue;
        proteinTextView = viewBinding.textViewProteinValue;
        saltTextView = viewBinding.textViewSaltValue;
        sodiumTextView = viewBinding.textViewSodiumValue;
        nutritionScoreTextView = viewBinding.textViewNutritionScoreValue;
    }

    private void setUpViewModelObserving() {
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        ProductDetailFragment detailFragment = (ProductDetailFragment) getParentFragmentManager().findFragmentByTag("ProductDetail");
        setCurrentViewsForProduct(Objects.requireNonNull(detailFragment).getCurrentProduct());
        viewModel.getProduct().observe(requireActivity(), product -> {
            setCurrentViewsForProduct(product);
        });
    }

    private void setCurrentViewsForProduct(Product product){
        Map<String, String> nutritionFacts = product.getNutritionFacts();
        Log.d(TAG, "setUpViewModelObserving: " + nutritionFacts);
        energyKjTextView.setText(nutritionFacts.get("energy-kj_100g"));
        energyKcalTextView.setText(nutritionFacts.get("energy-kcal_100g"));
        fatTextView.setText(nutritionFacts.get("fat_100g"));
        saturatedFatTextView.setText(nutritionFacts.get("saturated-fat_100g"));
        carbohydratesTextView.setText(nutritionFacts.get("carbohydrates_100g"));
        sugarTextView.setText(nutritionFacts.get("sugars_100g"));
        proteinTextView.setText(nutritionFacts.get("proteins_100g"));
        saltTextView.setText(nutritionFacts.get("salt_100g"));
        sodiumTextView.setText(nutritionFacts.get("sodium_100g"));
        nutritionScoreTextView.setText(product.getNutrientLevel());
    }


}
