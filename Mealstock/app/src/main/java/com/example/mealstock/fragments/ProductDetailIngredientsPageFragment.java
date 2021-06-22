package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mealstock.databinding.FragmentProductDetailPageIngredientsBinding;

public class ProductDetailIngredientsPageFragment extends Fragment {
    private FragmentProductDetailPageIngredientsBinding viewBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProductDetailPageIngredientsBinding.inflate(inflater, container, false);
        View view = viewBinding.getRoot();
        return view;
    }


}
