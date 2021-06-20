package com.example.mealstock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealstock.R;
import com.example.mealstock.databinding.FragmentProductDetailViewBinding;
import com.example.mealstock.fragments.ProductDetailFragment;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

    }


}
