package com.example.tdm_food_tracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tdm_food_tracker.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductRepository {

    private LiveData<List<Product>> products;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Product");


    public ProductRepository(Application application) {
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public void insert(Product product) {
        myRef.setValue(product);
    }

    public void update(Product product) {
    }

    public void delete(Product product) {
        myRef.removeValue();
    }

    public void deleteAll() {

    }





}
