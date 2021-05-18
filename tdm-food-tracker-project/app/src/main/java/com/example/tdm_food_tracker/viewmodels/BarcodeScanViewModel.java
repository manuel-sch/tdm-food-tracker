package com.example.tdm_food_tracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarcodeScanViewModel extends ViewModel {

    private MutableLiveData<String> barcode;

    public LiveData<String> getBarcode() {
        if(barcode == null)
            barcode = new MutableLiveData<String>();
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.postValue(barcode);
    }
}
