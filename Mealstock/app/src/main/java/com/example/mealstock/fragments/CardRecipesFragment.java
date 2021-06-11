package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;

public class CardRecipesFragment extends Fragment {
    private final ViewPager2 viewPager;

    public CardRecipesFragment(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.card_recipes, container, false);
    }

    public ViewPager2 getAdapter() {
        return viewPager;
    }
}
