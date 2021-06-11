package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;
import com.example.mealstock.databinding.CardNaehrwerteBinding;

public class CardNaehrwerteFragment extends Fragment {
    private final ViewPager2 viewPager;
    private CardNaehrwerteBinding cardNaehrwerteBinding;

    public CardNaehrwerteFragment(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.card_naehrwerte, container, false);
    }

    public ViewPager2 getAdapter() {
        return viewPager;
    }


}
