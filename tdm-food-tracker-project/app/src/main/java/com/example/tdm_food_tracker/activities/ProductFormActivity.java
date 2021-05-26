package com.example.tdm_food_tracker.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tdm_food_tracker.R;
import com.example.tdm_food_tracker.databinding.ActivityProductFormBinding;
import com.example.tdm_food_tracker.models.Product;
import com.example.tdm_food_tracker.viewmodels.ProductViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ProductFormActivity extends AppCompatActivity {

    private static final String TAG = ProductFormActivity.class.getSimpleName();
    private String EXTRA_REPLY;
    private final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);

    private ProductViewModel productViewModel;
    private ActivityProductFormBinding activityBinding;


    private EditText productNameEditText;
    private EditText productQuanityEditText;
    private EditText productIngredientEditText;
    private EditText productPriceEditText;
    private EditText productUnitEditText;
    private EditText productBoughtDateEditText;
    private EditText productExpiryDateEditText;
    private Spinner productStorageSpinner;
    private DatePickerDialog.OnDateSetListener boughtDatePickerDialog, expiryDatePickerDialog;

    private Product currentProduct = new Product();

    private ArrayAdapter<CharSequence> adapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityProductFormBinding.inflate(getLayoutInflater());
        View viewRoot = activityBinding.getRoot();
        setContentView(viewRoot);
        EXTRA_REPLY = this.getApplicationContext() + ".REPLY";
        Log.d(TAG, "onCreate: " + "Test");
        initializeViews();
        setUpViews();
        setUpViewObserving();
    }

    void initializeViews() {
        productNameEditText = activityBinding.editTextProductName;
        productQuanityEditText = activityBinding.editTextProductQuantity;
        productIngredientEditText = activityBinding.editTextProductIngredients;
        productPriceEditText = activityBinding.editTextProductPrice;
        productUnitEditText = activityBinding.editTextProductUnit;
        productBoughtDateEditText = activityBinding.editTextBoughtDate;
        productExpiryDateEditText = activityBinding.editTextExpiryDate;
        productStorageSpinner = activityBinding.spinnerStorage;
    }

    void setUpViews() {
        setUpStorageSpinner();
        setUpDatePicker(productBoughtDateEditText, boughtDatePickerDialog);
        setUpDatePicker(productExpiryDateEditText, expiryDatePickerDialog);
    }


    void setUpViewObserving() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProduct().observe(this, product -> {
            currentProduct = product;
            if(product.getProductName() != null)
                productNameEditText.setText(product.getProductName());
            if(product.getQuantity() != 0)
                productQuanityEditText.setText(String.valueOf(product.getQuantity()));
            if(product.getUnit() != 0)
                productQuanityEditText.setText(String.valueOf(product.getUnit()));
            if(product.getIngredients() != null)
                productIngredientEditText.setText(product.getIngredients());
            if(product.getPrice() != 0)
                productPriceEditText.setText(String.valueOf(product.getPrice()));
            if(!product.getBoughtDate().toString().equals(""))
                productBoughtDateEditText.setText(sdf.format(product.getBoughtDate()));
            if(!product.getExpiryDate().toString().equals(""))
                productExpiryDateEditText.setText(sdf.format(product.getExpiryDate()));
            productStorageSpinner.setSelection(adapter.getPosition(product.getStorage()));
        });
    }

    void setUpStorageSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.product_storage, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        productStorageSpinner.setAdapter(adapter);

        productStorageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentProduct.setStorage(productStorageSpinner.getItemAtPosition(position).toString());
                productViewModel.setProduct(currentProduct);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }



    void setUpDatePicker(EditText datePickerEditText, DatePickerDialog.OnDateSetListener datePickerDialog) {
        datePickerEditText.setInputType(InputType.TYPE_NULL);
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setProductDateEditTextFromCurrentCalendar(datePickerEditText);
            }
        };
        DatePickerDialog.OnDateSetListener finalDatePickerDialog = datePickerDialog;
        datePickerEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProductFormActivity.this, finalDatePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setProductDateEditTextFromCurrentCalendar(EditText datePickerEditText) {
        if(datePickerEditText.getId() == R.id.editText_boughtDate)
            currentProduct.setBoughtDate(myCalendar.getTime());
        else if(datePickerEditText.getId() == R.id.editText_expiryDate)
            currentProduct.setExpiryDate(myCalendar.getTime());
        productViewModel.setProduct(currentProduct);
        //datePickerEditText.setText(sdf.format(myCalendar.getTime()));
    }


    public void handleSaveForm(View v) {
        productViewModel.insertProduct(currentProduct);

    }

    private void showToastMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}
