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
import com.example.mealstock.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListForSoonExpiringProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductListForSoonExpiringProductsRecyclerViewAdapter.ProductItemViewHolder> {

    private Context context;
    private final List<Product> products;
    private final ProductItemClickListener clickListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);

    public ProductListForSoonExpiringProductsRecyclerViewAdapter(ProductItemClickListener clickListener) {
        products = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_soon_expiring_product, parent, false);
        return new ProductItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        Glide.with(context).load(products.get(position).getImageUrl()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.productImageView);
        holder.expiryDateTextView.setText(sdf.format(products.get(position).getExpiryDate()));
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

        private final ImageView productImageView;
        private final TextView expiryDateTextView;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            productImageView = itemView.findViewById(R.id.imageView_product);
            expiryDateTextView = itemView.findViewById(R.id.textView_product_expiry_date);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onProductItemClick(products.get(position));
        }
    }

    public interface ProductItemClickListener {
        void onProductItemClick(Product clickedProduct);
    }
}
