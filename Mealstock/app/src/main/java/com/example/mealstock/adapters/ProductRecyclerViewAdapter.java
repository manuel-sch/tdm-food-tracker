package com.example.mealstock.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealstock.R;
import com.example.mealstock.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> products = new ArrayList<>();

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_simple, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(v);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Glide.with(context).load(products.get(position).getImageUrl()).centerCrop().placeholder(R.drawable.product_placeholder).into(holder.imageViewProductTitle);
        holder.textViewProductTitle.setText(products.get(position).getProductName());
        Log.d("BLAB", "onBindViewHolder: " + products.get(position).getUnit() );
        if(products.get(position).getUnit() != 0)
            holder.textViewProductUnit.setText(products.get(position).getUnit());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProductTitle;
        private TextView textViewProductTitle;
        private TextView textViewProductUnit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductTitle = itemView.findViewById(R.id.imageView_product);
            textViewProductTitle = itemView.findViewById(R.id.textView_productTitle);
            textViewProductUnit = itemView.findViewById(R.id.textView_productUnit);
        }
    }
}
