package com.example.mealstock.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.fragments.CardInfoFragment;
import com.example.mealstock.fragments.CardNaehrwerteFragment;
import com.example.mealstock.fragments.CardRecipesFragment;

public class CardSlideAdapter extends FragmentStateAdapter {
    private ViewPager2 viewPager;
    private static final int NUM_PAGES = 3;
    public CardSlideAdapter(FragmentActivity fa, ViewPager2 viewPager){
        super(fa);
        this.viewPager = viewPager;
    }

    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new CardInfoFragment(viewPager);
            case 1:
                return new CardNaehrwerteFragment(viewPager);
            case 2:
                return new CardRecipesFragment(viewPager);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
