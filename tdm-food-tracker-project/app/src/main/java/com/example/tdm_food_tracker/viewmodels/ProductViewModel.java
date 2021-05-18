package com.example.tdm_food_tracker.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tdm_food_tracker.database.ProductRepository;
import com.example.tdm_food_tracker.models.Product;

public class ProductViewModel extends AndroidViewModel {


    private ProductRepository repository;

    private MutableLiveData<Product> product;

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    public LiveData<Product> getProduct() {
        if (product == null) {
            product = new MutableLiveData<>();
        }
        return product;
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
