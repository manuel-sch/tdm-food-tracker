package com.example.mealstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.models.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeItemViewHolder> {

    private final Context context;
    private final List<Recipe> recipes;

    public RecipeListAdapter(Context context, List<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_cardview,parent, false);
        RecipeItemViewHolder recipeItemViewHolder = new RecipeItemViewHolder(v);
        return recipeItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        Glide.with(context).load(recipes.get(position).getImageUrl()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.imageViewRecipe);
        holder.textViewRecipeTitle.setText(recipes.get(position).getRecipeName());
        holder.textViewCookingTime.setText(recipes.get(position).getCookingTime());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class RecipeItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewRecipe;
        private final TextView textViewRecipeTitle;
        private final TextView textViewCookingTime;

        public RecipeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageView_recipe);
            textViewRecipeTitle = itemView.findViewById(R.id.recipeTitle);
            textViewCookingTime = itemView.findViewById(R.id.recipeTime);
        }
    }






}


