package com.example.mealstock.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealstock.R;
import com.example.mealstock.adapters.FavoriteRecipeListAdapter;
import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.databinding.FragmentRecipeSearchBinding;
import com.example.mealstock.models.Recipe;
import com.example.mealstock.viewmodels.RecipeSearchViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchFragment extends Fragment implements FavoriteRecipeListAdapter.RecipeClickListener {

    private static final String TAG = RecipeSearchFragment.class.getSimpleName();

    private List<Recipe> currentRecipes;

    private FavoriteRecipeListAdapter recipeListAdapter;

    private FragmentRecipeSearchBinding viewBinding;

    private FireBaseRepository fireBaseRepository;

    private RecipeSearchViewModel viewModel;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentRecipes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentRecipeSearchBinding.inflate(inflater, container, false);
        View view = viewBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireBaseRepository = new FireBaseRepository();

        initializeViews();
        setUpRecyclerView();
        setUpViewObserving();
        setUpFireBase();
        setUpSearchBar();

    }

    private void initializeViews() {
        recyclerView = viewBinding.recipeListView;
        searchView = viewBinding.searchBar;
        progressBar = viewBinding.progressBar;
    }


    private void setUpRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recipeListAdapter = new FavoriteRecipeListAdapter(this);
        recyclerView.setAdapter(recipeListAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(llm);
    }

    private void setUpViewObserving() {
        viewModel = new ViewModelProvider(this).get(RecipeSearchViewModel.class);
        viewModel.getCurrentRecipes().observe(requireActivity(), recipes-> {
            Log.d(TAG, "setUpViewObserving: " + recipes);
            currentRecipes = recipes;
            recipeListAdapter.updateRecipes(currentRecipes);
        });
    }

    private void setUpFireBase() {

        fireBaseRepository.getRecipeReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentRecipes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    currentRecipes.add(dataSnapshot.getValue(Recipe.class));
                }
                viewModel.setCurrentRecipes(currentRecipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(null, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSearchBar() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    recipeListAdapter.getFilter().filter(newText);
                } else {
                    setUpFireBase();
                }
                return false;
            }
        });
    }

    public void setProgressBarVisibilityWithBool(boolean showProgressbar) {
        Log.d(TAG, "setProgressBarVisibilityWithBool: " + showProgressbar);
        if (showProgressbar)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onRecipeClick(Recipe clickedRecipe) {
        Log.d(TAG, "onRecipeClick: ");
        Bundle recipeDetailBundle = new Bundle();
        recipeDetailBundle.putSerializable("Recipe", clickedRecipe);
        requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.navHostFragment,
                RecipeDetailFragment.class, recipeDetailBundle, "RecipeDetailFrag").addToBackStack("RecipeDetailFrag").commit();
    }

    @Override
    public void onUnfavoriteClick(Recipe clickedRecipe) {
        viewModel.deleteRecipe(clickedRecipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }


}