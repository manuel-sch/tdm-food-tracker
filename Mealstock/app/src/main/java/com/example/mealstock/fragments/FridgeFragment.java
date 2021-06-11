package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;
import com.example.mealstock.databinding.FragmentFridgeBinding;

public class FridgeFragment extends Fragment {

    private ImageView gefrierFach;
    private FragmentFridgeBinding fragmentFridgeBinding;

    private ViewPager2 viewPager;

    public FridgeFragment(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFridgeBinding = FragmentFridgeBinding.inflate(inflater, container, false);
        View view = fragmentFridgeBinding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gefrierFach = fragmentFridgeBinding.gefrierfach;

        gefrierFach.setOnClickListener(v -> {

            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, ProductListFragment.class, null).commit();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentFridgeBinding = null;
    }

    public ViewPager2 getAdapter() {
        return viewPager;
    }
}