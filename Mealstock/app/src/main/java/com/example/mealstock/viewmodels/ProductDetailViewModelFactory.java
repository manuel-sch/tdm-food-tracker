package com.example.mealstock.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductDetailViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private Context context;


    public ProductDetailViewModelFactory(Application application, Context context) {
        this.application = application;
        this.context = context;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProductDetailViewModel(application, context);
    }
}
