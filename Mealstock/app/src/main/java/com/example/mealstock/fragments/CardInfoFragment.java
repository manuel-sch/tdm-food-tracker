package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.databinding.CardInfoBinding;

public class CardInfoFragment extends Fragment {



    private final ViewPager2 viewPager;
    private CardInfoBinding cardInfoBinding;

    public CardInfoFragment(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cardInfoBinding = CardInfoBinding.inflate(inflater, container, false);
        View view = cardInfoBinding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cardInfoBinding = null;
    }

    public ViewPager2 getAdapter() {
        return viewPager;
    }


}
