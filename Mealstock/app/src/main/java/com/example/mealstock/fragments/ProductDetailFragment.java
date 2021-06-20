package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mealstock.R;
import com.example.mealstock.adapters.CardSlideAdapter;
import com.example.mealstock.adapters.ScreenSlidePagerAdapter;
import com.example.mealstock.databinding.CardInfoBinding;
import com.example.mealstock.databinding.FragmentHomeBinding;
import com.example.mealstock.databinding.FragmentProductDetailViewBinding;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class ProductDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private ViewPager2 vpDetail;
    private FragmentProductDetailViewBinding fragmentProductDetailViewBinding;
    private FragmentStateAdapter pAdaptCard;


    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String param1) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProductDetailViewBinding = FragmentProductDetailViewBinding.inflate(inflater, container, false);
        View view = fragmentProductDetailViewBinding.getRoot();

        TextView titleTV = view.findViewById(R.id.titleProduct);
        titleTV.setText(mParam1);



        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpDetail = fragmentProductDetailViewBinding.cardViewpager;
        pAdaptCard = new CardSlideAdapter(requireActivity(), vpDetail);
        vpDetail.setAdapter(pAdaptCard);

        SpringDotsIndicator dotsIndicator = fragmentProductDetailViewBinding.dotsIndicatorCard;
        dotsIndicator.setViewPager2(vpDetail);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentProductDetailViewBinding = null;

    }


}