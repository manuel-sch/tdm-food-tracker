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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ProductFormActivity extends AppCompatActivity {

    private static final String TAG = ProductFormActivity.class.getSimpleName();
    private String EXTRA_REPLY;
    private final Calendar myCalendar = Calendar.getInstance();

    private ProductViewModel productViewModel;
    private ActivityProductFormBinding activityBinding;


    private EditText productNameEditText;
    private EditText productQuanityEditText;
    private EditText productIngredientEditText;
    private EditText productPriceEditText;
    private EditText productUnitEditText;
    private EditText productDateEditText;
    private Spinner productStorageSpinner;
    private DatePickerDialog.OnDateSetListener datePickerDialog;

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
        productNameEditText = activityBinding.productName;
        productQuanityEditText = activityBinding.productQuantity;
        productIngredientEditText = activityBinding.productIngredient;
        productPriceEditText = activityBinding.productPrice;
        productUnitEditText = activityBinding.productUnit;
        productDateEditText = activityBinding.editTextDate;
        productStorageSpinner = activityBinding.spinnerStorage;
    }

    void setUpViews() {
        setUpStorageSpinner();
        setUpDatePicker();
    }


    void setUpViewObserving() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProduct().observe(this, product -> {
            currentProduct = product;
            productNameEditText.setText(product.getProductName());
            productQuanityEditText.setText(String.valueOf(product.getQuantity()));
            productIngredientEditText.setText(product.getIngredients());
            productPriceEditText.setText((int) product.getPrice());
            productUnitEditText.setText(product.getUnit());
            productDateEditText.setText(product.getExpiryDate().toString());
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



    void setUpDatePicker() {
        productDateEditText.setInputType(InputType.TYPE_NULL);
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setProductDateEditTextFromCurrentCalendar();
            }
        };
        productDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProductFormActivity.this, datePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setProductDateEditTextFromCurrentCalendar() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        productDateEditText.setText(sdf.format(myCalendar.getTime()));
    }


    Date buildDateFromEditText() {
        String dayMonthYear = productDateEditText.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dayMonthYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
