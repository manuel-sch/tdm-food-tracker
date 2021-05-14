package com.example.tdm_food_tracker.ProduktForm;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm_food_tracker.R;


public class FormEdit extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formular_edit);
    }
    public void productPriceClicked(View v) {

       v.toString();

    }

    public void productCountClicked(View v) {
        tToast("productCountClicked");
    }

    public void productIngredientClicked(View v) {
        tToast("productIngredientClicked");
    }

    public void productUnitClicked(View v) {
        tToast("productUnitClicked");
    }

    public void productNameClicked(View v) {
        tToast("productNameClicked");
    }

    public void saveForm(View v) {

        FormModel form = new FormModel();

        EditText productName = (EditText) findViewById(R.id.productName);
        EditText productCount = (EditText) findViewById(R.id.productCount);
        EditText productIngredient = (EditText) findViewById(R.id.productIngredient);
        EditText productPrice = (EditText) findViewById(R.id.productPrice);
        EditText productUnit = (EditText) findViewById(R.id.productUnit);



        form.setProductName(productName.getText().toString());
        form.setProductCount(productCount.getText().toString());
        form.setProductIngredient(productIngredient.getText().toString());
        form.setProductPrice(productPrice.getText().toString());
        form.setProductUnit(productUnit.getText().toString());


        tToast( form.toString());




    }



    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

}
