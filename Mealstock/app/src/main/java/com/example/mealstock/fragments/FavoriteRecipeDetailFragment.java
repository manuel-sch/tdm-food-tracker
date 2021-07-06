package com.example.mealstock.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mealstock.R;
import com.example.mealstock.databinding.FragmentFavoriteRecipeDetailBinding;
import com.example.mealstock.databinding.FragmentProductDetailViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteRecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteRecipeDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    private FragmentFavoriteRecipeDetailBinding fragmentFavoriteRecipeDetailBinding;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    public FavoriteRecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FavoriteRecipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteRecipeDetailFragment newInstance(String param1, String param2, String param3) {
        FavoriteRecipeDetailFragment fragment = new FavoriteRecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentFavoriteRecipeDetailBinding = FragmentFavoriteRecipeDetailBinding.inflate(inflater, container, false);
        View view = fragmentFavoriteRecipeDetailBinding.getRoot();

        TextView recipeTitle = view.findViewById(R.id.recipe_name);
        TextView cookingTime = view.findViewById(R.id.time_title);

        recipeTitle.setText(mParam1);
        cookingTime.setText(mParam2);


        return view;
    }
    public void onDestroyView() {
        super.onDestroyView();
        fragmentFavoriteRecipeDetailBinding = null;

    }
}