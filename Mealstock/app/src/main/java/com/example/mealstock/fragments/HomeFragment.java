package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;
import com.example.mealstock.adapters.ScreenSlidePagerAdapter;
import com.example.mealstock.databinding.FragmentHomeBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class HomeFragment extends Fragment {


    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private FragmentHomeBinding fragmentHomeBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = fragmentHomeBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = fragmentHomeBinding.pager;
        pagerAdapter = new ScreenSlidePagerAdapter(requireActivity(), viewPager);
        viewPager.setAdapter(pagerAdapter);

        SpringDotsIndicator dotsIndicator = fragmentHomeBinding.dotsIndicator;
        dotsIndicator.setViewPager2(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;

    }


}