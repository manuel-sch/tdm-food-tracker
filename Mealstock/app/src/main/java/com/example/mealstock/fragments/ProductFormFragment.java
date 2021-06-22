package com.example.mealstock.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealstock.R;
import com.example.mealstock.databinding.FragmentProductFormBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.viewmodels.ProductFormViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ProductFormFragment extends Fragment {

    private static final String TAG = ProductFormFragment.class.getSimpleName();
    private final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);

    private ProductFormViewModel productViewModel;
    private FragmentProductFormBinding binding;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductFormBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onCreate: " + "Test");
        initializeViews();
        setUpViews();
        setUpViewObserving();
    }


    void initializeViews() {
        productNameEditText = binding.editTextProductName;
        productQuanityEditText = binding.editTextProductQuantity;
        productIngredientEditText = binding.editTextProductIngredients;
        productPriceEditText = binding.editTextProductPrice;
        productUnitEditText = binding.editTextProductUnit;
        productBoughtDateEditText = binding.editTextBoughtDate;
        productExpiryDateEditText = binding.editTextExpiryDate;
        productStorageSpinner = binding.spinnerStorage;
    }

    void setUpViews() {
        setUpStorageSpinner();
        setUpDatePicker(productBoughtDateEditText, boughtDatePickerDialog);
        setUpDatePicker(productExpiryDateEditText, expiryDatePickerDialog);
    }


    void setUpViewObserving() {
        productViewModel = new ViewModelProvider(this).get(ProductFormViewModel.class);
        productViewModel.getProduct().observe(requireActivity(), product -> {
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
        adapter = ArrayAdapter.createFromResource(requireActivity(),
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
                new DatePickerDialog(requireActivity(), finalDatePickerDialog, myCalendar
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
        Context context = requireActivity().getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}
