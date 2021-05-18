package com.example.tdm_food_tracker.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.example.tdm_food_tracker.constants.AppInfoConstants;
import com.example.tdm_food_tracker.constants.UrlRequestConstants;
import com.example.tdm_food_tracker.databinding.ActivityBarcodeScanBinding;
import com.example.tdm_food_tracker.network.JsonRequest;
import com.example.tdm_food_tracker.network.NetworkDataTransmitterSingleton;
import com.example.tdm_food_tracker.utils.RequestMethod;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScanActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private SurfaceView surfaceView;
    private TextView barcodeText;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    private ToneGenerator toneGen1;
    private String barcodeData;

    private NetworkDataTransmitterSingleton dataTransmitter;


    private ActivityBarcodeScanBinding activityBarcodeScanBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarcodeScanBinding = ActivityBarcodeScanBinding.inflate(getLayoutInflater());
        View viewRoot = activityBarcodeScanBinding.getRoot();
        setContentView(viewRoot);

        this.dataTransmitter = NetworkDataTransmitterSingleton.getInstance(AppInfoConstants.getAppContext());

        initializeViews();
        initialiseDetectorsAndSources();
    }

    private void initializeViews(){
        surfaceView = activityBarcodeScanBinding.surfaceView;
        barcodeText = activityBarcodeScanBinding.barcodeText;
    }

    private void initialiseDetectorsAndSources() {

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

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }

                            String barcodeSearchUrl = UrlRequestConstants.OPENFOODFACTS_GET_PRODUCT_WITH_BARCODE;
                            String combinedUrl = barcodeSearchUrl + barcodeData + ".json";
                            JsonRequest jsonReq = new JsonRequest(combinedUrl, Request.Method.GET, RequestMethod.BARCODE, null);
                            dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, BarcodeScanActivity.this);

                        }
                    });

                }
            }
        });
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
        initialiseDetectorsAndSources();
    }

}