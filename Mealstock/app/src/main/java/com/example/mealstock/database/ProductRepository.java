package com.example.mealstock.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mealstock.models.Product;

import java.util.List;

public class ProductRepository {

    private LiveData<List<Product>> products;

    public ProductRepository(Application application) {
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public void insert(Product product) {
    }

    public void update(Product product) {
    }

    public void delete(Product product) {
    }

    public void deleteAll() {

    }





}
