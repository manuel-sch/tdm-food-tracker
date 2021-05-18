package com.example.tdm_food_tracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tdm_food_tracker.daos.ProductDao;
import com.example.tdm_food_tracker.models.Product;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> products;

    public ProductRepository(Application application) {
        MealStockDatabase db = MealStockDatabase.getDatabase(application);
        productDao = db.productDao();
        products = productDao.getAll();
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public void insert(Product product) {
        MealStockDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    public void update(Product product) {
        MealStockDatabase.databaseWriteExecutor.execute(() -> {
            productDao.update(product);
        });
    }

    public void delete(Product product) {
        MealStockDatabase.databaseWriteExecutor.execute(() -> {
            productDao.delete(product);
        });
    }

    public void deleteAll() {
        MealStockDatabase.databaseWriteExecutor.execute(() -> {
            productDao.deleteAll();
        });
    }





}
