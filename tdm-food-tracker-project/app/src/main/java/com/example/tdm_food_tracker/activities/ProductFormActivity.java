package com.example.tdm_food_tracker.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tdm_food_tracker.R;
import com.example.tdm_food_tracker.constants.AppInfoConstants;
import com.example.tdm_food_tracker.databinding.ActivityProductFormBinding;
import com.example.tdm_food_tracker.models.Product;
import com.example.tdm_food_tracker.viewmodels.ProductViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ProductFormActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = AppInfoConstants.getAppPackageName() + ".REPLY";

    private ActivityProductFormBinding activityBinding;
    ProductViewModel productViewModel;

    private EditText productNameEditText;
    private EditText productQuanityEditText;
    private EditText productIngredientEditText;
    private EditText productPriceEditText;
    private EditText productUnitEditText;
    private EditText productDateEditText;
    private Spinner productStorageSpinner;

    private DatePickerDialog datePickerDialog;

    private ArrayAdapter<CharSequence> adapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityProductFormBinding.inflate(getLayoutInflater());
        View viewRoot = activityBinding.getRoot();
        setContentView(viewRoot);
        initializeViews();
        setUpViews();
        setUpViewObserving();
    }

    void initializeViews(){
        productNameEditText = activityBinding.productName;
        productQuanityEditText = activityBinding.productQuantity;
        productIngredientEditText = activityBinding.productIngredient;
        productPriceEditText = activityBinding.productPrice;
        productUnitEditText = activityBinding.productUnit;
        productDateEditText = activityBinding.editTextDate;
        productStorageSpinner = activityBinding.spinnerStorage;
    }

    void setUpViews(){
        setUpStorageSpinner();
        setUpDatePicker();
    }

    void setUpViewObserving(){
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProduct().observe(this, product -> {
            productNameEditText.setText(product.getProductName());
            productQuanityEditText.setText(product.getQuantity());
            productIngredientEditText.setText(product.getIngredients());
            productPriceEditText.setText((int) product.getPrice());
            productUnitEditText.setText(product.getUnit());
            productDateEditText.setText(product.getExpiryDate().toString());
            productStorageSpinner.setSelection(adapter.getPosition(product.getStorage()));
        });
    }

    void setUpStorageSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.product_storage, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        productStorageSpinner.setAdapter(adapter);
    }

    void setUpDatePicker(){
        productDateEditText.setInputType(InputType.TYPE_NULL);
        productDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(ProductFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        productDateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
            }
        });
    }


    public void saveForm(View v) {
        Intent replyIntent = new Intent();
        Product newProduct = setUpProductEntityAndGetIt();

        if (TextUtils.isEmpty(productNameEditText.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            replyIntent.putExtra(EXTRA_REPLY, newProduct);
            setResult(RESULT_OK, replyIntent);
        }
        finish();

    }

    Product setUpProductEntityAndGetIt(){

        Product newProduct = new Product();

        newProduct.setProductName(productNameEditText.getText().toString());
        newProduct.setQuantity(productQuanityEditText.getText().toString());
        newProduct.setIngredients(productIngredientEditText.getText().toString());
        newProduct.setPrice(Double.parseDouble(productPriceEditText.getText().toString()));
        newProduct.setUnit(Integer.parseInt(productUnitEditText.getText().toString()));
        newProduct.setStorage(productStorageSpinner.getItemAtPosition(productStorageSpinner.getSelectedItemPosition()).toString());
        newProduct.setExpiryDate(buildDateFromEditText());


        return newProduct;
    }

    Date buildDateFromEditText(){
        String dayMonthYear = productDateEditText.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dayMonthYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void showToastMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}
