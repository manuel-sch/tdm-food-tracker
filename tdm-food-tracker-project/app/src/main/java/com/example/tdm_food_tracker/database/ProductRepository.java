package com.example.tdm_food_tracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tdm_food_tracker.daos.ProductDao;
import com.example.tdm_food_tracker.models.ProductEntity;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<ProductEntity>> products;

    public ProductRepository(Application application) {
        MealStockDatabase db = MealStockDatabase.getDatabase(application);
        productDao = db.productDao();
        products = productDao.getAll();
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return products;
    }

    public void insert(ProductEntity product) {
        MealStockDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    public void update(ProductEntity product) {
        MealStockDatabase.databaseWriteExecutor.execute(() -> {
            productDao.update(product);
        });
    }

    public void delete(ProductEntity product) {
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
