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

import com.example.mealstock.databinding.FragmentProductDetailPageInfoBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.viewmodels.ProductDetailViewModel;

import java.util.List;
import java.util.Objects;

public class ProductDetailInfoPageFragment extends Fragment {

    private final String TAG = ProductDetailInfoPageFragment.class.getSimpleName();

    private Product currentProduct;

    private FragmentProductDetailPageInfoBinding viewBinding;
    private ProductDetailViewModel viewModel;
    private TextView allergensDataTextView;
    private TextView ingredientsDataTextView;
    private TextView brandsDataTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProductDetailPageInfoBinding.inflate(inflater, container, false);
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
        allergensDataTextView = viewBinding.textViewAllergensData;
        ingredientsDataTextView = viewBinding.textViewIngredientsData;
        brandsDataTextView = viewBinding.textViewBrandsData;
    }

    private void setUpViewModelObserving() {
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        ProductDetailFragment detailFragment = (ProductDetailFragment) getParentFragmentManager().findFragmentByTag("ProductDetail");
        setCurrentViewsForProduct(Objects.requireNonNull(detailFragment).getCurrentProduct());
        Log.d(TAG, "setUpViewModelObserving: " + "Blub");
        viewModel.getProduct().observe(requireActivity(), product -> {
            setCurrentViewsForProduct(product);
        });
    }

    private void setCurrentViewsForProduct(Product product){
        List<String> allergens = product.getAllergensAsList();
        List<String> ingredients = product.getIngredientsAsList();
        String brands = product.getBrands();
        Log.d(TAG, "setUpViewModelObserving: " + allergens);
        if(!product.getAllergens().isEmpty()){
            allergensDataTextView.setText(allergens.get(0));;
            for(int i = 1; i < allergens.size(); i++){
                allergensDataTextView.setText(String.format("%s\n%s", allergensDataTextView.getText(), allergens.get(i)));
            }
        }
        Log.d(TAG, "setUpViewModelObserving: " + ingredients);
        if(!product.getIngredients().isEmpty()){
            ingredientsDataTextView.setText(ingredients.get(0));;
            for(int i = 1; i < ingredients.size(); i++){
                ingredientsDataTextView.setText(String.format("%s\n%s", ingredientsDataTextView.getText(), ingredients.get(i)));
            }
        }
        if(!brands.isEmpty()){
            brandsDataTextView.setText(brands);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }


}
