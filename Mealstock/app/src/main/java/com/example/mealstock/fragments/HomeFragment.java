package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;
import com.example.mealstock.adapters.ProductListForSoonExpiringProductsRecyclerViewAdapter;
import com.example.mealstock.adapters.ScreenSlidePagerAdapter;
import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.databinding.FragmentHomeBinding;
import com.example.mealstock.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements ProductListForSoonExpiringProductsRecyclerViewAdapter.ProductItemClickListener {

    private List<Product> currentProducts;

    private ViewPager2 viewPager;
    private RecyclerView soonExpiringProductsRecyclerView;
    private ProductListForSoonExpiringProductsRecyclerViewAdapter recyclerViewAdapter;

    private FragmentStateAdapter pagerAdapter;
    private FragmentHomeBinding fragmentHomeBinding;

    private FireBaseRepository fireBaseRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentProducts = new ArrayList<>();
        fireBaseRepository = new FireBaseRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = fragmentHomeBinding.getRoot();
        recyclerViewAdapter = new ProductListForSoonExpiringProductsRecyclerViewAdapter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initalizeViews();
        setUpViewPager();
        setUpRecyclerView();
        setUpFireBase();
    }

    private void initalizeViews() {
        viewPager = fragmentHomeBinding.pager;
        soonExpiringProductsRecyclerView = fragmentHomeBinding.recyclerViewSoonExpiringProducts;
    }

    private void setUpViewPager() {
        pagerAdapter = new ScreenSlidePagerAdapter(requireActivity(), viewPager);
        viewPager.setAdapter(pagerAdapter);

        SpringDotsIndicator dotsIndicator = fragmentHomeBinding.dotsIndicator;
        dotsIndicator.setViewPager2(viewPager);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        soonExpiringProductsRecyclerView.setLayoutManager(layoutManager);
        soonExpiringProductsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setUpFireBase() {
        fireBaseRepository.getProductReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentProducts.clear();
                for (DataSnapshot storageSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot productSnapshot : storageSnapshot.getChildren()) {
                        currentProducts.add(productSnapshot.getValue(Product.class));
                    }
                }
                Collections.sort(currentProducts);
                recyclerViewAdapter.updateProducts(currentProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(null, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProductItemClick(Product clickedProduct) {
        Bundle productDetailBundle = new Bundle();
        productDetailBundle.putSerializable("Product", clickedProduct);
        requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.navHostFragment,
                ProductDetailFragment.class, productDetailBundle, "ProductDetail").addToBackStack("ProductDetail").commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;

    }

}