package com.example.mealstock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealstock.R;
import com.example.mealstock.models.Product;
import com.example.mealstock.viewmodels.ProductListViewModel;

public class ProductInputActivity extends AppCompatActivity {

    public static final int NEW_PRODUCT_ACTIVITY_REQUEST_CODE = 1;
    public String EXTRA_REPLY;

    private final String TAG = ProductInputActivity.class.getSimpleName();
    private ProductListViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_input);
        EXTRA_REPLY = this.getApplicationContext() + ".REPLY";
        productViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
    }

    public void barcodeActivityButton(View view) {
        Intent intent = new Intent(this, BarcodeScanActivity.class);
        startActivity(intent);
    }

    public void buttonToFormClick(View view) {
        Log.d(TAG, "buttonToFormClick: Form Activity should be started now.");
        Intent intent = new Intent(this, ProductFormActivity.class);
        startActivityForResult(intent, NEW_PRODUCT_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NEW_PRODUCT_ACTIVITY_REQUEST_CODE:
                    Product newProduct = (Product) data.getSerializableExtra(EXTRA_REPLY);
                    //productViewModel.insert(newProduct);
                    break;
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "onActivityResult not ok result.",
                    Toast.LENGTH_LONG).show();
        }
    }
}