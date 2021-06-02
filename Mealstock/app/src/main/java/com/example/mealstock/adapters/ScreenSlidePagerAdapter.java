package com.example.mealstock.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.fragments.FridgeFragment;
import com.example.mealstock.fragments.ShelfFragment;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    private ViewPager2 viewPager;
    private static final int NUM_PAGES = 2;
    public ScreenSlidePagerAdapter(FragmentActivity fa, ViewPager2 viewPager) {
        super(fa);
        this.viewPager = viewPager;
    }
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new FridgeFragment(viewPager);
            case 1:
                return new ShelfFragment(viewPager);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }


}