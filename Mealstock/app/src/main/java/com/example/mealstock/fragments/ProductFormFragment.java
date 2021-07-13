package com.example.mealstock.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.mealstock.R;
import com.example.mealstock.databinding.FragmentProductFormBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.viewmodels.ProductFormViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProductFormFragment extends Fragment {

    private static final String TAG = ProductFormFragment.class.getSimpleName();

    public static final int CAMERA_REQUEST = 1;
    public static final int CAMERA_PERMISSION_REQUEST = 201;

    private final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);

    private ProductFormViewModel productViewModel;
    private FragmentProductFormBinding binding;

    private ActivityResultLauncher<Uri> takePictureResultLauncher;
    private Uri photoUri;


    private EditText productNameEditText;
    private EditText productGenericNameEditText;
    private EditText productQuanityEditText;
    private EditText productIngredientEditText;
    private EditText productPriceEditText;
    private EditText productUnitEditText;
    private EditText productBrandsEditText;
    private EditText productBoughtDateEditText;
    private EditText productExpiryDateEditText;
    private Spinner productStorageSpinner;
    private DatePickerDialog.OnDateSetListener boughtDatePickerDialog, expiryDatePickerDialog;
    private Button productImageTakeButton;
    private FloatingActionButton productSaveFloatingActionButton;
    CircleImageView productCircleImageView;

    private Product currentProduct = new Product();

    private ArrayAdapter<CharSequence> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePictureResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean saved) {
                        if(saved){
                            Log.d(TAG, "onActivityResult: " + "Bild gespeichert");
                            currentProduct.setImageUrl(photoUri.toString());
                            Log.d(TAG, "onActivityResult: " + currentProduct.getImageUrl());
                            productViewModel.setProduct(currentProduct);
                        }
                    }
                });
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
        productGenericNameEditText = binding.editTextGenericName;
        productQuanityEditText = binding.editTextProductQuantity;
        productIngredientEditText = binding.editTextProductIngredients;
        productPriceEditText = binding.editTextProductPrice;
        productUnitEditText = binding.editTextProductUnit;
        productBrandsEditText = binding.editTextProductBrands;
        productBoughtDateEditText = binding.editTextBoughtDate;
        productExpiryDateEditText = binding.editTextExpiryDate;
        productStorageSpinner = binding.spinnerStorage;
        productSaveFloatingActionButton = binding.fabProductSave;
        productImageTakeButton = binding.buttonTakeProductImage;
        productCircleImageView = binding.productImage;

        setUpSaveFloatingActionButton();
        setUpProductImageTakeButton();
    }

    private void setUpSaveFloatingActionButton() {
        productSaveFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Brandname des hinzugefügten Produktes: " + productBrandsEditText.getText().toString());
                currentProduct.setProductName(productNameEditText.getText().toString());
                currentProduct.setGenericName(productGenericNameEditText.getText().toString());
                currentProduct.setBrands(productBrandsEditText.getText().toString());
                currentProduct.setUnit(Integer.parseInt(productUnitEditText.getText().toString()));
                currentProduct.setPrice(Double.parseDouble(productPriceEditText.getText().toString()));
                currentProduct.setQuantity(Double.parseDouble(productQuanityEditText.getText().toString()));
                currentProduct.setIngredients(productIngredientEditText.getText().toString());
                Log.d(TAG, "onClick: Produkt: " + currentProduct + " wurde hinzugefügt.");
                productViewModel.insertProduct(currentProduct);
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void setUpProductImageTakeButton() {
        productImageTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takeImage();
                }
                else {
                    ActivityCompat.requestPermissions(requireActivity(), new
                            String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }
            }
        });
    }

    public void takeImage() {
        File photoFile =  null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }
        if(photoFile != null){
            photoUri = FileProvider.getUriForFile(requireActivity(),
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureResultLauncher.launch(photoUri);
        }

    }

    private File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
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
                productUnitEditText.setText(String.valueOf(product.getUnit()));
            if(product.getIngredients() != null)
                productIngredientEditText.setText(product.getIngredients());
            if(!product.getBrands().equals(""))
                productBrandsEditText.setText(String.valueOf(product.getBrands()));
            if(product.getPrice() != 0)
                productPriceEditText.setText(String.valueOf(product.getPrice()));
            if(!product.getBoughtDate().toString().equals(""))
                productBoughtDateEditText.setText(sdf.format(product.getBoughtDate()));
            if(!product.getExpiryDate().toString().equals(""))
                productExpiryDateEditText.setText(sdf.format(product.getExpiryDate()));
            productStorageSpinner.setSelection(adapter.getPosition(product.getStorage()));
            if(!product.getImageUrl().equals(""))
                Glide.with(this).load(product.getImageUrl()).into(productCircleImageView);
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

}
