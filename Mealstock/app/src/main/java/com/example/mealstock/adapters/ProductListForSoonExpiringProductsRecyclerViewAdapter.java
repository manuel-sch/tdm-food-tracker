package com.example.mealstock.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        Product currentProduct = products.get(position);
        Glide.with(context).load(currentProduct.getImageUrl()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.product_placeholder).into(holder.productImageView);
        holder.expiryDateTextView.setText(sdf.format(currentProduct.getExpiryDate()));
        setExpiryDateTextViewColorBasedOnLeftTime(holder.expiryDateTextView, currentProduct);
    }

    private void setExpiryDateTextViewColorBasedOnLeftTime(TextView expiryDateTextView, Product currentProduct){
        Calendar calendar = Calendar.getInstance();


        Date currentDate = calendar.getTime();
        Log.d("TAG1", "setExpiryDateTextViewColorBasedOnLeftTime: " + currentDate);

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date pastSevenDaysDate = calendar.getTime();
        Log.d("TAG2", "setExpiryDateTextViewColorBasedOnLeftTime: " + pastSevenDaysDate);

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date pastFourteenDaysDate = calendar.getTime();
        Log.d("TAG3", "setExpiryDateTextViewColorBasedOnLeftTime: " + pastFourteenDaysDate);

        calendar.add(Calendar.DAY_OF_YEAR, 16);
        Date pastOneMonthDate = calendar.getTime();
        Log.d("TAG4", "setExpiryDateTextViewColorBasedOnLeftTime: " + pastOneMonthDate);

        Date expiryDate = currentProduct.getExpiryDate();
        Log.d("TAG5", "setExpiryDateTextViewColorBasedOnLeftTime: " + expiryDate);

        if(expiryDate.before(pastSevenDaysDate)){
            expiryDateTextView.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
        }
        else if(expiryDate.before(pastFourteenDaysDate)){
            expiryDateTextView.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }
        else if(expiryDate.before(pastOneMonthDate)){
            expiryDateTextView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        }
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
