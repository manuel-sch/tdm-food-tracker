package com.example.mealstock.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealstock.R;
import com.example.mealstock.fragments.ProductScanFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(this.getApplicationContext());
        progressBar = findViewById(R.id.progressBar);
        supportFragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) supportFragmentManager.findFragmentById(R.id.navHostFragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);


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
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == REQUEST_CAMERA_PERMISSION){
                Fragment fragment = supportFragmentManager.findFragmentById(R.id.navHostFragment).getChildFragmentManager().getFragments().get(0);
                if(fragment != null && fragment instanceof ProductScanFragment){
                    ProductScanFragment productScanFragment = (ProductScanFragment) fragment;
                    productScanFragment.startCameraSource();
                }

            }
        }

    }


}

