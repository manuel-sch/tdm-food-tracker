package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;
import com.example.mealstock.constants.ProductConstants;
import com.example.mealstock.databinding.FragmentHomePageFridgeFreezerBinding;

public class FridgeFreezerFragment extends Fragment {

    private ImageView freezer, fridge;
    private FragmentHomePageFridgeFreezerBinding viewBinding;

    private ViewPager2 viewPager;

    public FridgeFreezerFragment(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentHomePageFridgeFreezerBinding.inflate(inflater, container, false);
        View view = viewBinding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        freezer = viewBinding.gefrierfach;
        fridge = viewBinding.kuehl;
        freezer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("storage", ProductConstants.FREEZER);

            getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.navHostFragment, ProductListFragment.class, bundle)
                            .addToBackStack("Freezer").commit();
        });

        fridge.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("storage", ProductConstants.FRIDGE);

            getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.navHostFragment, ProductListFragment.class, bundle)
                    .addToBackStack("Fridge").commit();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    public ViewPager2 getAdapter() {
        return viewPager;
    }
}