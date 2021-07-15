package com.example.mealstock.activities;

import static com.example.mealstock.fragments.ProductFormFragment.CAMERA_PERMISSION_REQUEST;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mealstock.R;
import com.example.mealstock.fragments.HomeFragment;
import com.example.mealstock.fragments.ProductFormFragment;
import com.example.mealstock.fragments.ProductScanFragment;
import com.example.mealstock.fragments.RecipeSearchFragment;
import com.example.mealstock.models.Product;
import com.example.mealstock.network.NetworkDataTransmitterSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000;
    private final String TAG = MainActivity.class.getSimpleName();

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
        supportFragmentManager = getSupportFragmentManager();

        createNotificationChannel();


    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.searchFrag:
                            openFragment(new RecipeSearchFragment(), "SearchFrag");
                            item.setChecked(true);
                            return true;
                        case R.id.homeFrag:
                            openFragment(HomeFragment.newInstance("", ""), "HomeFrag");
                            item.setChecked(true);
                            return true;
                        case R.id.scanFrag:
                            openFragment(ProductScanFragment.newInstance("", ""), "ScanFrag");
                            item.setChecked(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutMenu){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: 1 " + supportFragmentManager.getFragments() );
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onRequestPermissionsResult: 2 " + supportFragmentManager.getFragments() );
            Log.d(TAG, "onRequestPermissionsResult: requestcode: " + requestCode);
            if(requestCode == ProductScanFragment.REQUEST_CAMERA_SCAN_PERMISSION){
                Log.d(TAG, "onRequestPermissionsResult: 3 " + supportFragmentManager.getFragments() );
                Fragment fragment = supportFragmentManager.findFragmentByTag("ScanFrag");
                if(fragment != null && fragment instanceof ProductScanFragment){
                    ProductScanFragment productScanFragment = (ProductScanFragment) fragment;
                    productScanFragment.startCameraSourceBasedOnRequiredPermission(true);
                }

            }
            else if(requestCode == CAMERA_PERMISSION_REQUEST){
                Fragment fragment = supportFragmentManager.findFragmentByTag("ProductForm");
                if(fragment != null && fragment instanceof ProductFormFragment){
                    ProductFormFragment productScanFragment = (ProductFormFragment) fragment;
                    productScanFragment.takeImage();
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

