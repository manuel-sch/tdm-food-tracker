package com.example.mealstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealstock.R;
import com.example.mealstock.adapters.RecipeListAdapter;

public class RecipeSearchFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);


        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recipeListView);
        rv.setHasFixedSize(true);
        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(new String[]{"Gebratener Reis", "Spagetthi", "Steak", "Sushi", "Nudelsuppe", "Rinderbraten"});
        rv.setAdapter(recipeListAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);


        Button button = (Button) view.findViewById(R.id.recipeButton);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, FavoriteRecipeDetailFragment.class, null).addToBackStack("FavoriteRecipe").commit();
            }
        });
        return view;
    }
}