package com.example.tdm_food_tracker.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.tdm_food_tracker.R;
import com.example.tdm_food_tracker.constants.UrlRequestConstants;
import com.example.tdm_food_tracker.databinding.ActivityBarcodeScanBinding;
import com.example.tdm_food_tracker.models.Product;
import com.example.tdm_food_tracker.network.JsonRequest;
import com.example.tdm_food_tracker.network.NetworkDataTransmitterSingleton;
import com.example.tdm_food_tracker.utils.RequestMethod;
import com.example.tdm_food_tracker.viewmodels.BarcodeScanViewModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BarcodeScanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = BarcodeScanActivity.class.getSimpleName();

    private Product observedProduct = new Product();

    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private SurfaceView surfaceView;
    private Spinner storageSpinerOnDialog;

    private TextView barcodeText;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    private Dialog dialog;
    private String clickedDialogElement;
    private EditText productBoughtDateEditTextOnDialog;
    private EditText productExpiryDateEditTextInDialog;
    private TextView productNameOnDialog;
    private ImageView productImageOnDialog;


    private final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener boughtDatePickerDialog;
    private DatePickerDialog.OnDateSetListener expiryDatePickerDialog;


    private ToneGenerator toneGen1;
    private String barcodeData;

    private NetworkDataTransmitterSingleton dataTransmitter;

    private BarcodeScanViewModel barcodeScanViewModel;

    private ActivityBarcodeScanBinding activityBarcodeScanBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarcodeScanBinding = ActivityBarcodeScanBinding.inflate(getLayoutInflater());
        View viewRoot = activityBarcodeScanBinding.getRoot();
        setContentView(viewRoot);


        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(this.getApplicationContext());

        setUpViewModelObserving();
        setUpProductDialog();
        initializeViews();
        initialiseBarcodeDetectorsAndSources();
        setUpDatePicker(productBoughtDateEditTextOnDialog, boughtDatePickerDialog);
        setUpDatePicker(productExpiryDateEditTextInDialog, expiryDatePickerDialog);

    }

    private void setUpViewModelObserving() {
        barcodeScanViewModel = new ViewModelProvider(this).get(BarcodeScanViewModel.class);
        barcodeScanViewModel.getBarcode().observe(this, barcode -> {
            barcodeText.setText(barcode);
        });
        barcodeScanViewModel.getProduct().observe(this, product -> {
            Log.d(TAG, "setUpViewModelObserving: " + product);
            if(dialog.isShowing()){
                productNameOnDialog.setText(product.getProductName());
                Glide.with(this).load(product.getImageUrl()).centerCrop().placeholder(R.drawable.product_placeholder).into(productImageOnDialog);
            }

        });
    }


    public void setBarcodeProduct(Product product){
        barcodeScanViewModel.setProduct(product);
    }

    private void initializeViews() {
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
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            } else {
                                newBarcodeData = barcodes.valueAt(0).displayValue;
                                //barcodeData = barcodes.valueAt(0).displayValue;
                                //barcodeText.setText(barcodeData);
                                barcodeScanViewModel.setBarcode(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
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
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        datePickerEditText.setText(sdf.format(myCalendar.getTime()));
    }

    void initializeViewsOnDialog(){
        storageSpinerOnDialog = dialog.findViewById(R.id.spinner);
        productBoughtDateEditTextOnDialog = dialog.findViewById(R.id.editText_boughtDate);
        productExpiryDateEditTextInDialog = dialog.findViewById(R.id.editText_expiryDate);
        productNameOnDialog = dialog.findViewById(R.id.textView_productTitle);
        productImageOnDialog = dialog.findViewById(R.id.imageView_product);
    }

    void setUpProductDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product_from_barcode);

        initializeViewsOnDialog();

        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
         */

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.product_storage, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storageSpinerOnDialog.setAdapter(adapter);
        storageSpinerOnDialog.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        clickedDialogElement = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), clickedDialogElement, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "gespeichert in " + clickedDialogElement,Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    public void bestaetigen(View v){
        Toast.makeText(this, "gespeichert in " + clickedDialogElement,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }


    public void showDialogForProductRequest(){
        dialog.show();
    }

    public void closeDialogForProductRequest(View v){
        dialog.dismiss();
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
        getSupportActionBar().hide();
        initialiseBarcodeDetectorsAndSources();
    }

}