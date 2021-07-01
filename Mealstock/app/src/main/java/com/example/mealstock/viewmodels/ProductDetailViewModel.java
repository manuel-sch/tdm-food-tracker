package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.models.Product;

public class ProductDetailViewModel extends AndroidViewModel {

    private FireBaseRepository fireBaseRepository;
    private MutableLiveData<Product> currentDetailProduct;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        currentDetailProduct = new MutableLiveData<>();
        fireBaseRepository = new FireBaseRepository();
    }

    public LiveData<Product> getProduct() {
        if(currentDetailProduct == null)
            currentDetailProduct = new MutableLiveData<>();
        return currentDetailProduct;
    }

    public void setProduct(Product product) {
        this.currentDetailProduct.postValue(product);
    }

}
