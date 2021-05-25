package com.example.tdm_food_tracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tdm_food_tracker.models.Product;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<Product> product;

    public LiveData<Product> getProduct() {
        if (product == null) {
            product = new MutableLiveData<>();
        }
        return product;
    }

    public void setProduct(Product product) {
        this.product.postValue(product);
    }

    public void insertProduct(Product product){

    }
}
