package com.example.tdm_food_tracker.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tdm_food_tracker.database.ProductRepository;
import com.example.tdm_food_tracker.models.ProductEntity;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {


    private ProductRepository repository;

    private final LiveData<List<ProductEntity>> products;

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        products = repository.getAllProducts();
    }

    LiveData<List<ProductEntity>> getAllWords() {
        return products;
    }

    public void insert(ProductEntity product) {
        repository.insert(product);

    }

    public void delete(ProductEntity product) {
        repository.delete(product);
    }

    public void update(ProductEntity product) {
        repository.update(product);

    }

}
