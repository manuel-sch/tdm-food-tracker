package com.example.mealstock.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.mealstock.R;
import com.example.mealstock.adapters.ProductRemoteSearchRecyclerViewAdapter;
import com.example.mealstock.constants.UrlRequestConstants;
import com.example.mealstock.databinding.FragmentSearchRemoteBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.network.JsonRequest;
import com.example.mealstock.network.NetworkDataTransmitterSingleton;
import com.example.mealstock.utils.RequestMethod;
import com.example.mealstock.viewmodels.ProductRemoteSearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProductRemoteSearchFragment extends Fragment implements ProductRemoteSearchRecyclerViewAdapter.ProductItemClickListener, View.OnClickListener {

    // Constants
    private static final String TAG = ProductRemoteSearchFragment.class.getSimpleName();

    // Main Variables
    private List<Product> currentProducts;
    private Product selectedProduct;

    // Datepicker/Calendar
    private final Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);
    private DatePickerDialog.OnDateSetListener boughtDatePickerDialog, expiryDatePickerDialog;

    // Views
    private ProgressBar progressBar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;


    // Dialog
    private TextView productTitleTextViewOnDialog;
    private AlertDialog productAddDialog;
    private View productAddDialogView;
    private ImageView productImageOnDialog;
    private EditText productBoughtDateEditTextOnDialog, productExpiryDateEditTextInDialog;
    private Spinner storageSpinerOnDialog;
    private NumberPicker productUnitNumberPicker;

    // Utils
    private FragmentSearchRemoteBinding binding;
    private NetworkDataTransmitterSingleton dataTransmitter;
    private FragmentManager parentFragmentManager;
    private ProductRemoteSearchViewModel productListViewModel;
    private ProductRemoteSearchRecyclerViewAdapter recyclerViewAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public static ProductRemoteSearchFragment newInstance(String param1, String param2) {
        ProductRemoteSearchFragment fragment = new ProductRemoteSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchRemoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        initializeUtils();
        initializeViews();
        setUpProductAddDialog();
        initializeViewsFromDialog();
        setUpStorageSpinner();
        setUpProductUnitPicker();
        setUpSearchBar();
        setUpViewObserving();
        setUpDatePicker(productBoughtDateEditTextOnDialog, boughtDatePickerDialog);
        setUpDatePicker(productExpiryDateEditTextInDialog, expiryDatePickerDialog);
        setUpStorageSpinnerAdapter();
    }

    private void initializeUtils() {
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(requireActivity());
        parentFragmentManager = getParentFragmentManager();
    }

    private void initializeViews() {
        progressBar = binding.progressBar;
        searchView = binding.searchViewRemoteProduct;
        recyclerView = binding.recyclerViewRemoteProducts;
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(llm);
        recyclerViewAdapter = new ProductRemoteSearchRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        floatingActionButton = binding.fabProductSave;
        floatingActionButton.setOnClickListener(this);
    }

    private void setUpSearchBar() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            String combinedUrl;
            JsonRequest jsonRequest;

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                combinedUrl = UrlRequestConstants.OPENFOODFACTS_SEARCH_PRODUCT_WTIH_PRODUCT_NAME + query.replace(" ", "+");
                jsonRequest = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.PRODUCT_NAME, null);
                dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonRequest);
                setSearchViewActivationWithBool(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setUpViewObserving() {
        productListViewModel = new ViewModelProvider(this).get(ProductRemoteSearchViewModel.class);
        productListViewModel.getProducts().observe(requireActivity(), products -> {
            currentProducts = products;
            recyclerViewAdapter.updateProducts(products);
        });
        productListViewModel.getSelectedProduct().observe(requireActivity(), product -> {
            selectedProduct = product;
            if(productAddDialog.isShowing()) {
                productTitleTextViewOnDialog.setText(product.getProductName());
                Glide.with(this).load(product.getImageUrl()).centerCrop().placeholder(R.drawable.product_placeholder).into(productImageOnDialog);
                if (!product.getBoughtDate().toString().equals(""))
                    productBoughtDateEditTextOnDialog.setText(sdf.format(product.getBoughtDate()));
                if (!product.getExpiryDate().toString().equals(""))
                    productExpiryDateEditTextInDialog.setText(sdf.format(product.getExpiryDate()));
            }

        });
    }

    private void setUpProductAddDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();
        productAddDialogView = inflater.inflate(R.layout.dialog_product_add, null);

        builder.setView(productAddDialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Log.d(TAG, "onClick: Gespeichertes Produkt: " + selectedProduct);
                        selectedProduct.setStorage(storageSpinerOnDialog.getSelectedItem().toString());
                        productListViewModel.insertProduct(selectedProduct);
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        productAddDialog = builder.create();
        productAddDialog.getWindow().getAttributes().windowAnimations = R.style.animation_fade_in_fade_out;
    }

    private void initializeViewsFromDialog(){
        productTitleTextViewOnDialog = productAddDialogView.findViewById(R.id.textView_recipe_name);
        storageSpinerOnDialog = productAddDialogView.findViewById(R.id.spinner_productStorage);
        productBoughtDateEditTextOnDialog = productAddDialogView.findViewById(R.id.editText_boughtDate);
        productExpiryDateEditTextInDialog = productAddDialogView.findViewById(R.id.editText_expiryDate);
        productImageOnDialog = productAddDialogView.findViewById(R.id.imageView_recipe);
        productUnitNumberPicker = productAddDialogView.findViewById(R.id.numberPicker_productUnit);

    }

    void setUpStorageSpinner(){
        storageSpinerOnDialog.setAdapter(setUpStorageSpinnerAdapter());
        setUpStorageSpinnerClickListener();
    }

    private void setUpProductUnitPicker() {
        productUnitNumberPicker.setMinValue(1);
        productUnitNumberPicker.setMaxValue(250);
        productUnitNumberPicker.setWrapSelectorWheel(false);
        productUnitNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedProduct.setUnit(newVal);
                productListViewModel.setSelectedProduct(selectedProduct);
            }
        });
    }

    ArrayAdapter<CharSequence> setUpStorageSpinnerAdapter() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.product_storage, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setUpStorageSpinnerClickListener();
        return adapter;
    }

    void setUpStorageSpinnerClickListener(){
        storageSpinerOnDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStorage = parent.getItemAtPosition(position).toString();
                selectedProduct.setStorage(selectedStorage);
                productListViewModel.setSelectedProduct(selectedProduct);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                setProductDateFromCurrentCalendar(datePickerEditText);
            }
        };
        DatePickerDialog.OnDateSetListener finalDatePickerDialog = datePickerDialog;
        datePickerEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), finalDatePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setProductDateFromCurrentCalendar(EditText datePickerEditText) {
        if(datePickerEditText.getId() == R.id.editText_boughtDate)
            selectedProduct.setBoughtDate(myCalendar.getTime());
        else if(datePickerEditText.getId() == R.id.editText_expiryDate)
            selectedProduct.setExpiryDate(myCalendar.getTime());
        productListViewModel.setSelectedProduct(selectedProduct);
    }

    public void setCurrentProducts(List<Product> products){
        productListViewModel.setProducts(products);
    }

    public void setSearchViewActivationWithBool(boolean activateSearchView){
            searchView.setSubmitButtonEnabled(activateSearchView);
    }

    private void handleProductSaveFab() {
        setDialogViews();
        productAddDialog.show();
    }

    private void setDialogViews(){
        productTitleTextViewOnDialog.setText(selectedProduct.getProductName());
        Glide.with(this).load(selectedProduct.getImageUrl()).centerCrop().placeholder(R.drawable.product_placeholder).into(productImageOnDialog);
        if(!selectedProduct.getBoughtDate().toString().equals(""))
            productBoughtDateEditTextOnDialog.setText(sdf.format(selectedProduct.getBoughtDate()));
        if(!selectedProduct.getExpiryDate().toString().equals(""))
            productExpiryDateEditTextInDialog.setText(sdf.format(selectedProduct.getExpiryDate()));
    }

    public void setProgressBarVisibilityWithBool(boolean showProgressbar) {
        Log.d(TAG, "setProgressBarVisibilityWithBool: " + showProgressbar);
        if (showProgressbar)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab_product_save:
                handleProductSaveFab();
                break;
        }
    }

    @Override
    public void onProductItemClick(int position) {
        floatingActionButton.setEnabled(true);
        productListViewModel.setSelectedProduct(currentProducts.get(position));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}