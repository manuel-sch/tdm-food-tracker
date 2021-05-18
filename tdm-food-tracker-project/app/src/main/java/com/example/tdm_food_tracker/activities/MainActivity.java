package com.example.tdm_food_tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tdm_food_tracker.constants.AppInfoConstants;
import com.example.tdm_food_tracker.databinding.ActivityMainBinding;
import com.example.tdm_food_tracker.models.Product;
import com.example.tdm_food_tracker.utils.RequestTester;
import com.example.tdm_food_tracker.viewmodels.ProductListViewModel;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_PRODUCT_ACTIVITY_REQUEST_CODE = 1;
    private final String TAG = MainActivity.class.getSimpleName();

    private RequestTester reqTester;
    // ViewBinding ermöglicht direktes zugreifen auf Views aus Layout über Objekt mit .viewName
    private ActivityMainBinding activityMainBinding;
    private ProductListViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = activityMainBinding.getRoot();
        setContentView(viewRoot);
        AppInfoConstants utilConstants = new AppInfoConstants(this);
        reqTester = new RequestTester(this);
        productViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);


        productViewModel.getAllProducts().observe(this, words -> {
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case NEW_PRODUCT_ACTIVITY_REQUEST_CODE:
                    Product newProduct = (Product) data.getSerializableExtra(FormEditActivity.EXTRA_REPLY);
                    productViewModel.insert(newProduct);
                    break;
            }
        }
         else {
            Toast.makeText(
                    getApplicationContext(),
                    "onActivityResult not ok result.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void testQueue(View view) {
        reqTester.testBarcodeSearchQueueOpenFoodFacts();
        //reqTester.testProductNameSearchQueueOpenFoodFacts();
    }


    public void buttonToFormClick(View view) {
        Log.d(TAG, "buttonToFormClick: Form Activity should be started now.");
        Intent intent = new Intent(this, FormEditActivity.class);
        startActivityForResult(intent, NEW_PRODUCT_ACTIVITY_REQUEST_CODE);
    }

}