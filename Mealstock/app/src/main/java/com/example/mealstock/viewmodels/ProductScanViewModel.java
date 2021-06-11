package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.database.FireBaseRepository;
import com.example.mealstock.models.Product;

public class ProductScanViewModel extends AndroidViewModel {

    private MutableLiveData<String> barcode;
    private MutableLiveData<Product> product;
    private FireBaseRepository fireBaseRepository;

    public ProductScanViewModel(Application application) {
        super(application);
        fireBaseRepository = new FireBaseRepository();
    }

    public LiveData<String> getBarcode() {
        if(barcode == null)
            barcode = new MutableLiveData<String>();
        return barcode;
    }

    public LiveData<Product> getProduct() {
        if(product == null)
            product = new MutableLiveData<>();
        return product;
    }

    public void setProduct(Product product) {
        this.product.postValue(product);
    }


    public void setBarcode(String barcode) {
        this.barcode.postValue(barcode);
    }

    public void insertProduct(Product product){
        fireBaseRepository.insertProduct(product);

    }
}
