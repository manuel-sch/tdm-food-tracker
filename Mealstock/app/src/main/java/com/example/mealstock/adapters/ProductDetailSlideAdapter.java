package com.example.mealstock.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mealstock.fragments.ProductDetailInfoPageFragment;
import com.example.mealstock.fragments.ProductDetailIngredientsPageFragment;
import com.example.mealstock.fragments.ProductDetailRecipesPageFragment;

public class ProductDetailSlideAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;
    public ProductDetailSlideAdapter(FragmentActivity fa){
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new ProductDetailInfoPageFragment();
            case 1:
                return new ProductDetailIngredientsPageFragment();
            case 2:
                return new ProductDetailRecipesPageFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
