package com.example.mealstock.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.mealstock.R;
import com.example.mealstock.activities.MainActivity;
import com.example.mealstock.constants.UrlRequestConstants;
import com.example.mealstock.databinding.FragmentScanBinding;
import com.example.mealstock.models.Product;
import com.example.mealstock.network.JsonRequest;
import com.example.mealstock.network.NetworkDataTransmitterSingleton;
import com.example.mealstock.utils.RequestMethod;
import com.example.mealstock.viewmodels.ProductScanViewModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductScanFragment extends Fragment implements View.OnClickListener {

    // Constants
    private static final String TAG = ProductScanFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    // Activityviews
    private FloatingActionButton productSearchFab, productFormFab;
    private ExtendedFloatingActionButton productInputFab;
    private TextView productSearchTextView, productFormTextView;
    private SurfaceView surfaceView;
    private ProgressBar progressBar;
    private TextView barcodeText;

    // Barcode
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private String barcodeData;

    // Datepicker/Calendar
    private final Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);
    private DatePickerDialog.OnDateSetListener boughtDatePickerDialog, expiryDatePickerDialog;

    // Dialog
    private TextView productTitleTextViewOnDialog;
    private AlertDialog productAddDialog;
    private View productAddDialogView;
    private ImageView productImageOnDialog;
    private EditText productBoughtDateEditTextOnDialog, productExpiryDateEditTextInDialog;
    private Spinner storageSpinerOnDialog;
    private NumberPicker productUnitNumberPicker;

    // Utils
    private NetworkDataTransmitterSingleton dataTransmitter;
    private ProductScanViewModel barcodeScanViewModel;
    private FragmentScanBinding fragmentScanBinding;
    private FragmentManager parentFragmentManager;
    private MainActivity mainActivity;

    // Other Variables
    private boolean isAllFabsVisible;
    private Product currentProduct = new Product();

    public ProductScanFragment() {
        super(R.layout.fragment_scan);
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static ProductScanFragment newInstance(String param1, String param2) {
        ProductScanFragment fragment = new ProductScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentScanBinding = FragmentScanBinding.inflate(inflater, container, false);
        View view = fragmentScanBinding.getRoot();
        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(requireActivity());
        parentFragmentManager = getParentFragmentManager();
        mainActivity = (MainActivity) requireActivity();
        setUpViewModelObserving();
        setUpProductAddDialog();
        initializeViewsFromDialog();
        setUpStorageSpinner();
        initializeViewsOfActivity();
        setUpFloatingActionButtons();
        initialiseBarcodeDetectorsAndSources();
        setUpDatePicker(productBoughtDateEditTextOnDialog, boughtDatePickerDialog);
        setUpDatePicker(productExpiryDateEditTextInDialog, expiryDatePickerDialog);
    }

    private void setUpFloatingActionButtons() {
        isAllFabsVisible = false;
        productInputFab.shrink();
    }

    private void setUpViewModelObserving() {
        barcodeScanViewModel = new ViewModelProvider(this).get(ProductScanViewModel.class);
        barcodeScanViewModel.getBarcode().observe(requireActivity(), barcode -> {
            barcodeText.setText(barcode);
        });
        barcodeScanViewModel.getProduct().observe(requireActivity(), product -> {
            Log.d(TAG, "setUpViewModelObserving: " + product);
            currentProduct = product;
            if (productAddDialog.isShowing()) {
                productTitleTextViewOnDialog.setText(product.getProductName());
                Glide.with(this).load(product.getImageUrl()).centerCrop().placeholder(R.drawable.product_placeholder).into(productImageOnDialog);
                if (!product.getBoughtDate().toString().equals(""))
                    productBoughtDateEditTextOnDialog.setText(sdf.format(product.getBoughtDate()));
                if (!product.getExpiryDate().toString().equals(""))
                    productExpiryDateEditTextInDialog.setText(sdf.format(product.getExpiryDate()));

            }

        });
    }

    void setUpProductAddDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();
        productAddDialogView = inflater.inflate(R.layout.dialog_product_add, null);

        builder.setView(productAddDialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "onClick: Gespeichertes Produkt: " + currentProduct);
                        currentProduct.setStorage(storageSpinerOnDialog.getSelectedItem().toString());
                        barcodeScanViewModel.insertProduct(currentProduct);
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        barcodeScanViewModel.setBarcode("");
                        barcodeScanViewModel.setProduct(null);
                    }
                });
        productAddDialog = builder.create();
        productAddDialog.getWindow().getAttributes().windowAnimations = R.style.animation_fade_in_fade_out;

    }

    private void initializeViewsOfActivity() {
        productInputFab = fragmentScanBinding.fabProductInput;
        productInputFab.setOnClickListener(this);
        productFormFab = fragmentScanBinding.fabAddForm;
        productFormFab.setOnClickListener(this);
        productSearchFab = fragmentScanBinding.fabAddSearch;
        productSearchFab.setOnClickListener(this);
        productFormTextView = fragmentScanBinding.textViewAddForm;
        productSearchTextView = fragmentScanBinding.textViewAddSearch;
        progressBar = fragmentScanBinding.progressBar;
        surfaceView = fragmentScanBinding.surfaceView;
        barcodeText = fragmentScanBinding.barcodeText;
    }

    private void initialiseBarcodeDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        try {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                barcodeDetector = new BarcodeDetector.Builder(requireActivity())
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

                cameraSource = new CameraSource.Builder(requireActivity(), barcodeDetector)
                        .setRequestedPreviewSize(1920, 1080)
                        .setAutoFocusEnabled(true) //you should add this feature
                        .build();
                cameraSource.start(surfaceView.getHolder());
                startDetecting();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    void startDetecting(){
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {
                            String newBarcodeData;
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                newBarcodeData = barcodes.valueAt(0).email.address;
                                barcodeScanViewModel.setBarcode(barcodeData);

                            } else {
                                newBarcodeData = barcodes.valueAt(0).displayValue;
                                barcodeScanViewModel.setBarcode(barcodeData);
                            }
                            if (!newBarcodeData.equals(barcodeData)) {
                                String barcodeSearchUrl = UrlRequestConstants.OPENFOODFACTS_GET_PRODUCT_WITH_BARCODE;
                                String combinedUrl = barcodeSearchUrl + newBarcodeData + ".json";
                                JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.BARCODE_SEARCH, null);
                                dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, requireActivity());
                                mainActivity.setProgressBarVisibilityWithBool(true);
                            }
                            barcodeData = newBarcodeData;


                        }
                    });

                }
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
                new DatePickerDialog(getActivity(), finalDatePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setProductDateEditTextFromCurrentCalendar(EditText datePickerEditText) {
        if (datePickerEditText.getId() == R.id.editText_boughtDate)
            currentProduct.setBoughtDate(myCalendar.getTime());
        else if (datePickerEditText.getId() == R.id.editText_expiryDate)
            currentProduct.setExpiryDate(myCalendar.getTime());
        barcodeScanViewModel.setProduct(currentProduct);
        //datePickerEditText.setText(sdf.format(myCalendar.getTime()));
    }

    void initializeViewsFromDialog() {
        productTitleTextViewOnDialog = productAddDialogView.findViewById(R.id.textView_product_expiry_date);
        storageSpinerOnDialog = productAddDialogView.findViewById(R.id.spinner_productStorage);
        productBoughtDateEditTextOnDialog = productAddDialogView.findViewById(R.id.editText_boughtDate);
        productExpiryDateEditTextInDialog = productAddDialogView.findViewById(R.id.editText_expiryDate);
        productImageOnDialog = productAddDialogView.findViewById(R.id.imageView_product);
        productUnitNumberPicker = productAddDialogView.findViewById(R.id.numberPicker_productUnit);
        setUpProductUnitPicker();

    }

    private void setUpProductUnitPicker() {
        productUnitNumberPicker.setMinValue(1);
        productUnitNumberPicker.setMaxValue(250);
        productUnitNumberPicker.setWrapSelectorWheel(false);
        productUnitNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentProduct.setUnit(newVal);
                barcodeScanViewModel.setProduct(currentProduct);
            }
        });
    }

    void setUpStorageSpinner() {
        storageSpinerOnDialog.setAdapter(setUpStorageSpinnerAdapter());
        setUpStorageSpinnerClickListener();
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

    void setUpStorageSpinnerClickListener() {
        storageSpinerOnDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStorage = parent.getItemAtPosition(position).toString();
                currentProduct.setStorage(selectedStorage);
                barcodeScanViewModel.setProduct(currentProduct);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


    public void showProductAddDialog() {
        productAddDialog.show();
    }

    public void setBarcodeProduct(Product product) {
        barcodeScanViewModel.setProduct(product);
    }


    public void handleProductInputFab() {

        if (!isAllFabsVisible) {

            productSearchFab.show();
            productFormFab.show();
            productSearchTextView.setVisibility(View.VISIBLE);
            productFormTextView.setVisibility(View.VISIBLE);

            productInputFab.extend();

            isAllFabsVisible = true;

        } else {

            productSearchFab.hide();
            productFormFab.hide();
            productSearchTextView.setVisibility(View.GONE);
            productFormTextView.setVisibility(View.GONE);

            productInputFab.shrink();

            isAllFabsVisible = false;
        }
    }

    public void handleProductAddFormFab() {
        parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true).replace(R.id.navHostFragment, ProductFormFragment.class, null, "ProductForm")
                .addToBackStack("ProductForm").commit();
        /*
        Intent intent = new Intent(getActivity(), ProductFormActivity.class);
        startActivity(intent);
         */
    }

    public void handleProductSearchFab() {
        parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true).replace(R.id.navHostFragment, ProductRemoteSearchFragment.class, null, "ProductRemoteSearch")
                .addToBackStack("ProductRemoteSearch").commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_form:
                handleProductAddFormFab();
                break;
            case R.id.fab_add_search:
                handleProductSearchFab();
                break;
            case R.id.fab_product_input:
                handleProductInputFab();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentScanBinding = null;
        cameraSource.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseBarcodeDetectorsAndSources();
    }



    public void startCameraSource() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            try {
                Log.d(TAG, "startCameraSource: " + "called");
                barcodeDetector = new BarcodeDetector.Builder(requireActivity())
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

                cameraSource = new CameraSource.Builder(requireActivity(), barcodeDetector)
                        .setRequestedPreviewSize(1920, 1080)
                        .setAutoFocusEnabled(true) //you should add this feature
                        .build();
                cameraSource.start(surfaceView.getHolder());
                startDetecting();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}