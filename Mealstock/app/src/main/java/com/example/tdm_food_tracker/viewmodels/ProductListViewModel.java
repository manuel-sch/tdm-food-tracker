package com.example.tdm_food_tracker.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tdm_food_tracker.database.ProductRepository;
import com.example.tdm_food_tracker.models.Product;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {


    private ProductRepository repository;

    private final LiveData<List<Product>> products;

    public ProductListViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        products = repository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public void insert(Product product) {
        repository.insert(product);

    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void update(Product product) {
        repository.update(product);

    }

}
