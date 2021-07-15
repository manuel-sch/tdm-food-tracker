package com.example.mealstock.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class FavoriteRecipeListAdapter extends RecyclerView.Adapter<FavoriteRecipeListAdapter.RecipeItemViewHolder> implements Filterable {

    private Context context;
    private final List<Recipe> recipes;
    private RecipeClickListener clickListener;

    private Filter searched_filter;


    public FavoriteRecipeListAdapter(RecipeClickListener clickListener){
        this.recipes = new ArrayList<>();
        this.clickListener = clickListener;
        setUpFilter();
    }

    private void setUpFilter() {
        searched_filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Recipe> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(recipes);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Recipe item : recipes) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                recipes.clear();
                recipes.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void updateRecipes(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_favorite_recipe_list, viewGroup, false);
        return new RecipeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        Glide.with(context).load(recipes.get(position).getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.imageViewRecipe);
        holder.textViewRecipeTitle.setText(recipes.get(position).getName());
        if(recipes.get(position).getTotalTimeInMinutes()!=0){
            holder.textViewCookingTime.setText(String.valueOf(recipes.get(position).getTotalTimeInMinutes()));
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public Filter getFilter() {
        return searched_filter;
    }

    public class RecipeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageViewRecipe;
        private final ImageView favStarImageView;
        private final TextView textViewRecipeTitle;
        private final TextView textViewCookingTime;

        public RecipeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageViewRecipe = itemView.findViewById(R.id.imageView_recipe);
            favStarImageView = itemView.findViewById(R.id.favstar);
            textViewRecipeTitle = itemView.findViewById(R.id.recipeTitle);
            textViewCookingTime = itemView.findViewById(R.id.recipeTime);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick: blub");
            int position = getAdapterPosition();
            clickListener.onRecipeClick(recipes.get(position));
        }
    }

    public interface RecipeClickListener {
        void onRecipeClick(Recipe clickedRecipe);
    }
}


