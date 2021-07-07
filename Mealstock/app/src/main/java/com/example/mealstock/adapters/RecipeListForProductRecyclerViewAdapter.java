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

import java.util.ArrayList;
import java.util.List;

public class RecipeListForProductRecyclerViewAdapter extends RecyclerView.Adapter<RecipeListForProductRecyclerViewAdapter.RecipeItemViewHolder> {

    private Context context;
    private List<Recipe> recipes;
    private final RecipeItemClickListener clickListener;

    public RecipeListForProductRecyclerViewAdapter(RecipeItemClickListener clickListener) {
        recipes = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_recipe_list, parent, false);
        return new RecipeItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        Glide.with(context).load(recipes.get(position).getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.imageViewProductTitle);
        holder.recipeNameTextView.setText(recipes.get(position).getName());
        holder.recipeTotalTimeTextView.setText(String.valueOf(recipes.get(position).getTotalTimeInMinutes()));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipes(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public class RecipeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imageViewProductTitle;
        private final TextView recipeNameTextView;
        private final TextView recipeTotalTimeTextView;


        public RecipeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageViewProductTitle = itemView.findViewById(R.id.imageView_recipe);
            recipeNameTextView = itemView.findViewById(R.id.textView_recipe_name);
            recipeTotalTimeTextView = itemView.findViewById(R.id.textView_recipe_total_time);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onRecipeItemClick(recipes.get(position));
        }
    }

    public interface RecipeItemClickListener {
        void onRecipeItemClick(Recipe clickedProduct);
    }
}
