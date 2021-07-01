package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mealstock.databinding.FragmentProductDetailPageRecipesBinding;

public class ProductDetailRecipesPageFragment extends Fragment {

    private FragmentProductDetailPageRecipesBinding viewBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProductDetailPageRecipesBinding.inflate(inflater, container, false);
        View view = viewBinding.getRoot();
        return view;
    }

}
