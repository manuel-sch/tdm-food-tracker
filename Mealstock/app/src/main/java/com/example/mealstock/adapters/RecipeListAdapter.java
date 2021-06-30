package com.example.mealstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealstock.R;
import com.example.mealstock.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeItemViewHolder> {

    private Context context;
    private List<Recipe> recipes = new ArrayList<>();
    private String[] test;

    public class RecipeItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewRecipe;
        private TextView textViewRecipeTitle;
        private TextView textViewRecipeTime;
        private CardView cardView;

        public RecipeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageView_recipe);
            textViewRecipeTitle = itemView.findViewById(R.id.recipeTitle);
            textViewRecipeTime = itemView.findViewById(R.id.recipeTime);
            cardView = itemView.findViewById(R.id.cardView_recipe);
        }
    }

    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_cardview,parent, false);
        RecipeItemViewHolder recipeItemViewHolder = new RecipeItemViewHolder(v);
        return recipeItemViewHolder;
    }

    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        //holder.textViewRecipeTitle.setText(recipes.get(position).getRecipeName());
        holder.textViewRecipeTitle.setText(test[position]);
    }

    public int getItemCount() {
        //return recipes.size();
        return test.length;
    }

/*    public RecipeListAdapter(List<Recipe> recipes){
        this.recipes = recipes;
    }
*/
    public RecipeListAdapter(String[] test){
        this.test = test;
    }


}


