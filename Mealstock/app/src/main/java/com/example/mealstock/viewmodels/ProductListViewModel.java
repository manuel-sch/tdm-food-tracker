package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.models.Product;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Product>> products;

    public ProductListViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Product>> getProducts() {
        if(products == null)
            products = new MutableLiveData<>();
        return products;

    }

    public void setProducts(List<Product> products) {
        this.products.postValue(products);
    }
}
