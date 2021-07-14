package com.example.mealstock.adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealstock.R;
import com.example.mealstock.activities.MainActivity;
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

    private static final String PRIMARY_CHANNEL_ID = "expired_food_notification_channel";

    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.mealstock.ACTION_UPDATE_NOTIFICATION";

    private View v;

    public ProductListForSoonExpiringProductsRecyclerViewAdapter(ProductItemClickListener clickListener) {
        products = new ArrayList<>();
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        v = LayoutInflater.from(context).inflate(R.layout.item_soon_expiring_product, parent, false);
        mNotifyManager = (NotificationManager)
                v.getContext().getSystemService(v.getContext().NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
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
        //Log.d("TAG1", "setExpiryDateTextViewColorBasedOnLeftTime: " + currentDate);

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date pastSevenDaysDate = calendar.getTime();
        //Log.d("TAG2", "setExpiryDateTextViewColorBasedOnLeftTime: " + pastSevenDaysDate);

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date pastFourteenDaysDate = calendar.getTime();
        //Log.d("TAG3", "setExpiryDateTextViewColorBasedOnLeftTime: " + pastFourteenDaysDate);

        calendar.add(Calendar.DAY_OF_YEAR, 16);
        Date pastOneMonthDate = calendar.getTime();
        //Log.d("TAG4", "setExpiryDateTextViewColorBasedOnLeftTime: " + pastOneMonthDate);

        Date expiryDate = currentProduct.getExpiryDate();
        //Log.d("TAG5", "setExpiryDateTextViewColorBasedOnLeftTime: " + expiryDate);


        Log.d("TAG6", "setExpiryDateTextViewColorBasedOnLeftTime: " + !currentProduct.isAlreadyNotificated());
        
        if(expiryDate.before(pastSevenDaysDate)){
            expiryDateTextView.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
            if(!currentProduct.isAlreadyNotificated()){
                sendNotification(currentProduct);
            }
        }
        else if(expiryDate.before(pastFourteenDaysDate)){
            expiryDateTextView.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }
        else if(expiryDate.before(pastOneMonthDate)){
            expiryDateTextView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        }
    }

    /**
     * OnClick method for the "Notify Me!" button.
     * Creates and delivers a simple notification.
     */
    public void sendNotification(Product currentProduct) {

        currentProduct.setAlreadyNotificated(true);
        // Sets up the pending intent to update the notification.
        // Corresponds to a press of the Update Me! button.
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this.context,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        // Build the notification with all of the parameters using helper
        // method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        String s =  currentProduct.getProductName() ;
        // Add the action button using the pending intent.
        notifyBuilder.addAction(R.mipmap.component_foreground,
                s, updatePendingIntent);

        // Deliver the notification.
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }
    private NotificationCompat.Builder getNotificationBuilder() {

        // Set up the pending intent that is delivered when the notification
        // is clicked.
        Intent notificationIntent = new Intent(v.getContext(), MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (v.getContext(), NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification with all of the parameters.
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(v.getContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle("Oh no. Dein Produkt läuft in weniger als sieben tagen ab :(")
                .setSmallIcon(R.mipmap.component_foreground)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
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
            productImageView = itemView.findViewById(R.id.imageView_recipe);
            expiryDateTextView = itemView.findViewById(R.id.textView_recipe_name);

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
