package com.example.mealstock.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mealstock.R;
import com.example.mealstock.fragments.CalendarFragment;
import com.example.mealstock.fragments.HomeFragment;
import com.example.mealstock.fragments.ProductRemoteSearchFragment;
import com.example.mealstock.fragments.ProductScanFragment;
import com.example.mealstock.fragments.SettingsFragment;
import com.example.mealstock.models.Product;
import com.example.mealstock.network.NetworkDataTransmitterSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final int TIME_INTERVAL = 2000;
    private final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private NetworkDataTransmitterSingleton dataTransmitter;
    private long mBackPressed;
    private FragmentManager supportFragmentManager;
    BottomNavigationView bottomNavigation;

    private static final String PRIMARY_CHANNEL_ID = "expired_food_notification_channel";

    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.mealstock.ACTION_UPDATE_NOTIFICATION";

    private NotificationReceiver mReceiver = new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance("", ""), "HomeFrag");
        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(MainActivity.this);
        progressBar = findViewById(R.id.progressBar);
        supportFragmentManager = getSupportFragmentManager();

        createNotificationChannel();


    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.calenderFrag:
                            openFragment(CalendarFragment.newInstance("", ""), "CalendarFrag");
                            return true;
                        case R.id.searchFrag:
                            openFragment(ProductRemoteSearchFragment.newInstance("", ""), "SearchFrag");
                            return true;
                        case R.id.homeFrag:
                            openFragment(HomeFragment.newInstance("", ""), "HomeFrag");
                            return true;
                        case R.id.scanFrag:
                            openFragment(ProductScanFragment.newInstance("", ""), "ScanFrag");
                            return true;
                        case R.id.settingFrag:
                            openFragment(SettingsFragment.newInstance("", ""), "SettingsFrag");
                            return true;
                    }
                    return false;
                }
            };

    /**
     * Unregisters the receiver when the app is being destroyed.
     */
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
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
    }
    public void sendNotification(Product currentProduct) {

        // Sets up the pending intent to update the notification.
        // Corresponds to a press of the Update Me! button.
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        // Build the notification with all of the parameters using helper
        // method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        String s = "Hey hey :D wollte kurz daran erinnern das dein Produkt :" + currentProduct.getProductName() +"in sieben tagen abläuft. Gucke dir in der app ein paar rezepte an um es zu verwerten ;)";
        // Add the action button using the pending intent.
        notifyBuilder.addAction(R.mipmap.component_foreground,
                s, updatePendingIntent);

        // Deliver the notification.
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }
    private NotificationCompat.Builder getNotificationBuilder(){NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("Oh no. Bald läuft dein Produk ab :(")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.mipmap.component_foreground);
        return notifyBuilder;
    };




    public void openFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.navHostFragment,
                fragment, tag).commit();
       
    }

    public void showShortSnackBarWithText(String text) {
        Snackbar.make(findViewById(R.id.main_layout), text, Snackbar.LENGTH_SHORT).show();
    }

    public void setProgressBarVisibilityWithBool(boolean showProgressbar) {
        if (showProgressbar)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {

        if (supportFragmentManager.getBackStackEntryCount() > 0) {
            super.onBackPressed();

        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return;
            } else {
                Toast.makeText(getBaseContext(), "Click two times to close the App", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == REQUEST_CAMERA_PERMISSION){
                Log.d(TAG, "onRequestPermissionsResult: " + supportFragmentManager.getFragments() );
                Fragment fragment = supportFragmentManager.findFragmentByTag("ScanFrag");
                if(fragment != null && fragment instanceof ProductScanFragment){
                    ProductScanFragment productScanFragment = (ProductScanFragment) fragment;
                    productScanFragment.startCameraSourceBasedOnRequiredPermission(true);
                }

            }
        }

    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        /**
         * Receives the incoming broadcasts and responds accordingly.
         *
         * @param context Context of the app when the broadcast is received.
         * @param intent The broadcast intent containing the action.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the notification.
           // updateNotification();
        }
    }

}

