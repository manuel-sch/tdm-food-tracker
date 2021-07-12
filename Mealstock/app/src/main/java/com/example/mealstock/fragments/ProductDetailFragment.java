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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.adapters.ProductDetailSlideAdapter;
import com.example.mealstock.databinding.FragmentProductDetailBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.models.Recipe;
import com.example.mealstock.viewmodels.ProductDetailViewModel;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailFragment extends Fragment {

    private final static String TAG = ProductDetailFragment.class.getSimpleName();

    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ProductDetailViewModel viewModel;

    private FragmentStateAdapter productDetailSlideAdapter;

    private Product currentProduct;
    private ViewPager2 detailViewPager;

    public List<Recipe> currentRecipes;

    private TextView productNameTextView;
    private TextView productExpiryDateTextView;
    private CircleImageView circleImageView;
    private SpringDotsIndicator dotsIndicator;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentProduct = (Product) requireArguments().get("Product");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProductDetailBinding = FragmentProductDetailBinding.inflate(inflater, container, false);
        View view = fragmentProductDetailBinding.getRoot();
        initializeViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Derzeitiges Produkt - " + currentProduct);
        setUpViewPager();
        setUpDotsIndicator();
        setUpViewModelObserving();
    }

    private void initializeViews() {
        productNameTextView = fragmentProductDetailBinding.titleProduct;
        productExpiryDateTextView = fragmentProductDetailBinding.expireDate;
        detailViewPager = fragmentProductDetailBinding.cardViewpager;
        circleImageView = fragmentProductDetailBinding.productImage;
        dotsIndicator = fragmentProductDetailBinding.dotsIndicatorCard;
    }

    private void setUpViewPager() {
        productDetailSlideAdapter = new ProductDetailSlideAdapter(requireActivity());
        detailViewPager.setAdapter(productDetailSlideAdapter);
    }

    private void setUpDotsIndicator() {
        dotsIndicator.setViewPager2(detailViewPager);
    }

    private void setUpViewModelObserving() {
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        viewModel.setProduct(currentProduct);
        viewModel.getProduct().observe(requireActivity(), product -> {
            productNameTextView.setText(currentProduct.getProductName());
            productExpiryDateTextView.setText(sdf.format(product.getExpiryDate()));
            Glide.with(this).load(currentProduct.getImageUrl()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(circleImageView);
        });

    }

    public Product getCurrentProduct(){
        return currentProduct;
    }

    public void setProductInformationToSearchForInRecipesWithProductName(){
        viewModel.setProductInformationToSearchForInRecipe(currentProduct.getProductName());
    }

    public void setRecipes(List<Recipe> recipes){
        viewModel.setRecipes(recipes);
        currentRecipes = recipes;
    }

    public void setRecipesFoundInViewModel(boolean found){
        viewModel.setRecipesFound(found);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentProductDetailBinding = null;

    }
}