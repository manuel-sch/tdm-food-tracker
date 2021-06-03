package com.example.mealstock.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealstock.models.Product;

import java.util.List;

public class ProductRemoteSearchViewModel extends AndroidViewModel {

    private MutableLiveData<List<Product>> products;
    private MutableLiveData<Product> selectedProduct;

    public ProductRemoteSearchViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Product>> getProducts() {
        if(products == null)
            products = new MutableLiveData<>();
        return products;

    }

    public LiveData<Product> getSelectedProduct() {
        if(selectedProduct == null)
            selectedProduct = new MutableLiveData<>();
        return selectedProduct;

    }

    public void setProducts(List<Product> products) {
        this.products.postValue(products);
    }

    public void setSelectedProduct(Product product) {
        this.selectedProduct.postValue(product);
    }

    public void insertProduct(Product product) {
        // Produkt in Datenbank einfügen
    }
}
