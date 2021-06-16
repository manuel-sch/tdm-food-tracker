package com.example.mealstock.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.fragments.ProductListFragment;
import com.example.mealstock.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListForStorageRecyclerViewAdapter  extends RecyclerView.Adapter<ProductListForStorageRecyclerViewAdapter.ProductItemViewHolder> {

    private Context context;
    private List<Product> products = new ArrayList<>();
    private final ProductItemClickListener clickListener;

    private int selectedItem;

    public ProductListForStorageRecyclerViewAdapter(ProductItemClickListener clickListener) {
        this.clickListener = clickListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_simple, parent, false);
        ProductItemViewHolder productViewHolder = new ProductItemViewHolder(v);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        Glide.with(context).load(products.get(position).getImageUrl()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.imageViewProductTitle);
        holder.textViewProductTitle.setText(products.get(position).getProductName());
        Log.d("BLAB", "onBindViewHolder: " + products.get(position).getUnit() );
        if(products.get(position).getQuantity() != 0)
            holder.textViewProductQuantity.setText((int)(products.get(position).getQuantity()) + " g/ml");
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

    public class ProductItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imageViewProductTitle;
        private TextView textViewProductTitle;
        private TextView textViewProductQuantity;
        private CardView cardView;

        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageViewProductTitle = itemView.findViewById(R.id.imageView_product);
            textViewProductTitle = itemView.findViewById(R.id.textView_productTitle);
            textViewProductQuantity = itemView.findViewById(R.id.textView_productQuantity);
            cardView = itemView.findViewById(R.id.cardView);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            int previousItem = selectedItem;
            selectedItem = position;
            notifyItemChanged(previousItem);
            notifyItemChanged(position);
            clickListener.onProductItemClick(position);
        }
    }
    public interface ProductItemClickListener {
        void onProductItemClick(int position);
    }
}
