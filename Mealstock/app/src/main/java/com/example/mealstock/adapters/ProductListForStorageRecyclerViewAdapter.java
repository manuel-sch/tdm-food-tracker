package com.example.mealstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListForStorageRecyclerViewAdapter  extends RecyclerView.Adapter<ProductListForStorageRecyclerViewAdapter.ProductItemViewHolder> implements Filterable {

    private Context context;
    private final ProductItemLongClickListener longClinkListener;
    private List<Product> products;
    private final ProductItemClickListener clickListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);

    public ProductListForStorageRecyclerViewAdapter(ProductItemClickListener clickListener, ProductItemLongClickListener swipeListener) {
        products = new ArrayList<>();
        this.clickListener = clickListener;
        this.longClinkListener = swipeListener;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_list, parent, false);
        return new ProductItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        Glide.with(context).load(products.get(position).getImageUrl()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.imageViewProductTitle);
        holder.textViewProductName.setText(products.get(position).getProductName());
        holder.textViewProductBoughtDate.setText(sdf.format(products.get(position).getBoughtDate()));
        holder.textViewProductExpiryDate.setText(sdf.format(products.get(position).getExpiryDate()));
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

    @Override
    public Filter getFilter() {
        return Searched_Filter;
    }
    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(products);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product item : products) {
                    if (item.getProductName().toLowerCase().contains(filterPattern)) {
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
            products.clear();
            products.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class ProductItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private final ImageView imageViewProductTitle;
        private final TextView textViewProductName;
        private final TextView textViewProductBoughtDate;
        private final TextView textViewProductExpiryDate;
        private final CardView cardView;


        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageViewProductTitle = itemView.findViewById(R.id.imageView_product);
            textViewProductName = itemView.findViewById(R.id.textView_product_expiry_date);
            textViewProductBoughtDate = itemView.findViewById(R.id.textView_productBoughDate_value);
            textViewProductExpiryDate = itemView.findViewById(R.id.textView_productExpiryDate_value);
            cardView = itemView.findViewById(R.id.cardView);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onProductItemClick(products.get(position));
        }


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            longClinkListener.onProductItemLongClick(position);
            return true;
        }
    }

    public interface ProductItemLongClickListener {
        void onProductItemLongClick(int position);
    }

    public interface ProductItemClickListener {
        void onProductItemClick(Product clickedProduct);
    }
}
