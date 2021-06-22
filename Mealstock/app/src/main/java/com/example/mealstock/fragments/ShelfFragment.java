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
import com.example.mealstock.databinding.FragmentShelfBinding;

public class ShelfFragment extends Fragment {

    private final ViewPager2 viewPager;
    private FragmentShelfBinding binding;
    private ImageView shelfImageView;


    public ShelfFragment(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShelfBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        shelfImageView = binding.imageViewShelf;

        shelfImageView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("storage", ProductConstants.SHELF);
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, ProductListFragment.class, bundle)
                    .addToBackStack("Shelf").commit();
        });

    }

    public ViewPager2 getAdapter() {
        return viewPager;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean handleBackPressWithHandledBoolean() {
        if (viewPager.getCurrentItem() == 0) {
            return false;
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return true;
        }
    }



}