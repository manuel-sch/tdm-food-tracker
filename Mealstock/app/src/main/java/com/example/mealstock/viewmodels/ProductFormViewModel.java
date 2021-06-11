package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.models.Product;

public class ProductFormViewModel extends AndroidViewModel {

    private MutableLiveData<Product> product;
    private final FireBaseRepository fireBaseRepository;

    public ProductFormViewModel(Application application) {
        super(application);
        fireBaseRepository = new FireBaseRepository();
    }

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
        fireBaseRepository.insertProduct(product);
    }
}
