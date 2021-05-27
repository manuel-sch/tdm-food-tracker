package com.example.tdm_food_tracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tdm_food_tracker.models.Product;

public class BarcodeScanViewModel extends ViewModel {

    private MutableLiveData<String> barcode;
    private MutableLiveData<Product> product;

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

    }
}
