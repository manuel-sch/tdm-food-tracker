package com.example.mealstock.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.mealstock.R;
import com.example.mealstock.constants.UrlRequestConstants;
import com.example.mealstock.databinding.ActivityBarcodeScanBinding;
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
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BarcodeScanActivity extends AppCompatActivity{

    // Constants
    private static final String TAG = BarcodeScanActivity.class.getSimpleName();
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
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);
    private DatePickerDialog.OnDateSetListener boughtDatePickerDialog, expiryDatePickerDialog;

    // Dialog
    private TextView productTitleTextViewOnDialog;
    private AlertDialog productAddDialog;
    private View productAddDialogView;
    private ImageView productImageOnDialog;
    private EditText productBoughtDateEditTextOnDialog, productExpiryDateEditTextInDialog;
    private Spinner storageSpinerOnDialog;

    // Utils
    private NetworkDataTransmitterSingleton dataTransmitter;
    private ProductScanViewModel barcodeScanViewModel;
    private ActivityBarcodeScanBinding activityBarcodeScanBinding;

    // Other Variables
    boolean isAllFabsVisible;
    private Product currentProduct = new Product();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarcodeScanBinding = ActivityBarcodeScanBinding.inflate(getLayoutInflater());
        View viewRoot = activityBarcodeScanBinding.getRoot();
        setContentView(viewRoot);

        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(this.getApplicationContext());


        setUpViewModelObserving();
        setUpProductAddDialog();
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
        barcodeScanViewModel.getBarcode().observe(this, barcode -> {
            barcodeText.setText(barcode);
        });
        barcodeScanViewModel.getProduct().observe(this, product -> {
            Log.d(TAG, "setUpViewModelObserving: " + product);
            currentProduct = product;
            if(productAddDialog.isShowing()){
                productTitleTextViewOnDialog.setText(product.getProductName());
                Glide.with(this).load(product.getImageUrl()).centerCrop().placeholder(R.drawable.product_placeholder).into(productImageOnDialog);
                if(!product.getBoughtDate().toString().equals(""))
                    productBoughtDateEditTextOnDialog.setText(sdf.format(product.getBoughtDate()));
                if(!product.getExpiryDate().toString().equals(""))
                    productExpiryDateEditTextInDialog.setText(sdf.format(product.getExpiryDate()));

            }

        });
    }

    void setUpProductAddDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        productAddDialogView = inflater.inflate(R.layout.dialog_product_from_barcode, null);

        builder.setView(productAddDialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        barcodeScanViewModel.insertProduct(currentProduct);
                        // User clicked OK button
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        barcodeData = null;
                        barcodeScanViewModel.setBarcode("");
                        barcodeScanViewModel.setProduct(null);
                    }
                });
        productAddDialog = builder.create();
        productAddDialog.getWindow().getAttributes().windowAnimations = R.style.animation_fade_in_fade_out;
        initializeViewsFromDialog();
        storageSpinerOnDialog.setAdapter(setUpStorageSpinner());

    }

    private void initializeViewsOfActivity() {
        productInputFab = activityBarcodeScanBinding.fabProductInput;
        productFormFab = activityBarcodeScanBinding.fabAddForm;
        productSearchFab = activityBarcodeScanBinding.fabAddSearch;
        productFormTextView = activityBarcodeScanBinding.textViewAddForm;
        productSearchTextView = activityBarcodeScanBinding.textViewAddSearch;
        progressBar = activityBarcodeScanBinding.progressBar;
        surfaceView = activityBarcodeScanBinding.surfaceView;
        barcodeText = activityBarcodeScanBinding.barcodeText;
    }

    private void initialiseBarcodeDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(BarcodeScanActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


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
                                //barcodeData = barcodes.valueAt(0).email.address;
                                //barcodeText.setText(barcodeData);
                                barcodeScanViewModel.setBarcode(barcodeData);

                            } else {
                                newBarcodeData = barcodes.valueAt(0).displayValue;
                                //barcodeData = barcodes.valueAt(0).displayValue;
                                //barcodeText.setText(barcodeData);
                                barcodeScanViewModel.setBarcode(barcodeData);
                            }
                            if(!newBarcodeData.equals(barcodeData)){
                                String barcodeSearchUrl = UrlRequestConstants.OPENFOODFACTS_GET_PRODUCT_WITH_BARCODE;
                                String combinedUrl = barcodeSearchUrl + newBarcodeData + ".json";
                                JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.BARCODE_SEARCH, null);
                                dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, BarcodeScanActivity.this);
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
                new DatePickerDialog(BarcodeScanActivity.this, finalDatePickerDialog, myCalendar
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
        barcodeScanViewModel.setProduct(currentProduct);
        //datePickerEditText.setText(sdf.format(myCalendar.getTime()));
    }

    void initializeViewsFromDialog(){
        productTitleTextViewOnDialog = productAddDialogView.findViewById(R.id.textView_productTitle);
        storageSpinerOnDialog = productAddDialogView.findViewById(R.id.spinner);
        productBoughtDateEditTextOnDialog = productAddDialogView.findViewById(R.id.editText_boughtDate);
        productExpiryDateEditTextInDialog = productAddDialogView.findViewById(R.id.editText_expiryDate);
        productImageOnDialog = productAddDialogView.findViewById(R.id.imageView_product);

    }


    ArrayAdapter<CharSequence> setUpStorageSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_storage, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }


    public void showProductAddDialog(){
        productAddDialog.show();
    }

    public void showProductAddErrorSnackbar(){
        Snackbar.make(findViewById(R.id.coordinatorLayout), "Das Produkt ist leider nicht in der Datenbank.", Snackbar.LENGTH_SHORT).show();
    }
    
    public void setProgressBarVisibilityWithBool(boolean showProgressbar){
        if(showProgressbar)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    public void setBarcodeProduct(Product product){
        barcodeScanViewModel.setProduct(product);
    }


    public void handleProductInputFab(View view) {

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

    public void handleProductAddFormFab(View view) {
        Intent intent = new Intent(this, ProductFormActivity.class);
        startActivity(intent);
    }

    public void handleProductSearchFab(View view) {
        /*
        Intent intent = new Intent(this, ProductSearchActivity.class);
        startActivity(intent);
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().show();
        initialiseBarcodeDetectorsAndSources();
    }

}