package com.example.mealstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealstock.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeIngredientsRecyclerViewAdapter.RecipeItemViewHolder> {
    private final List<String> ingredients;


    public RecipeIngredientsRecyclerViewAdapter(){
        ingredients = new ArrayList<>();
    }

    public void updateIngredients(List<String> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_ingredient,parent, false);
        RecipeItemViewHolder recipeItemViewHolder = new RecipeItemViewHolder(v);
        return recipeItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        String currentIngredient = ingredients.get(position);
        holder.ingredientsTextView.setText(currentIngredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public class RecipeItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView ingredientsTextView;

        public RecipeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientsTextView = itemView.findViewById(R.id.textView_ingredient);
        }
    }

}


