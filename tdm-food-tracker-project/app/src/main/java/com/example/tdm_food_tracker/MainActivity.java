package com.example.tdm_food_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import com.example.tdm_food_tracker.ProduktForm.FormEdit;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private Context APP_CONTEXT;

    private NetworkDataTransmitterSingleton dataTransmitter;
    private JsonHandlerSingleton jsonHandler;

    //Barcode
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APP_CONTEXT = this.getApplicationContext();
        //Barcode
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        //initialiseDetectorsAndSources();
    }

    public void testQueue(View view) {
        testSearchQueueFoodRepo();
    }

    private void testSearchQueueFoodRepo(){
        jsonHandler = JsonHandlerSingleton.getInstance(this);
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(APP_CONTEXT);

        String testUrl_1 = UrlRequestConstants.FOODREPO_POST_PRODUCT_SEARCH;
        //JSONObject searchJsonObject = jsonHandler.parseJsonFileFromAssetsToJsonObject("search_test_01.json");
        JSONObject searchJsonObject = jsonHandler.parseJsonFileFromAssetsToJsonObject("search_test_01.json");
        JsonRequest jsonReq = new JsonRequest(testUrl_1, Request.Method.POST, searchJsonObject);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, this);
    }

    private void testBarcodesQueueFoodRepo(){
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(APP_CONTEXT);

        String testUrl_1 = UrlRequestConstants.FOODREPO_GET_BARCODES;
        JsonRequest jsonReq = new JsonRequest(testUrl_1, Request.Method.GET, null);

        dataTransmitter.requestJsonObjectResponseForJsonRequestWithContext(jsonReq, this);
    }


    private void testQueueSpoonacular(){
        String testUrl_1 = UrlRequestConstants.SPOONACULAR_PRODUCT_SEARCH + "zott monte";
        String test_upc = "041631000564";
        String monte_upc = "EAN 42203421";
        dataTransmitter = NetworkDataTransmitterSingleton.getInstance(APP_CONTEXT);
        /*
        dataTransmitter.requestStringResponseForUrlWithContext
                (testUrl_1 + "&" + UrlRequestConstants.API_KEY , this);
        */

        dataTransmitter.requestStringResponseForUrlWithContext
                ("https://api.spoonacular.com/food/products/upc/" + monte_upc +
                        "?" + UrlRequestConstants.API_KEY_SPOONACULAR , this);
    }

    //Barcode
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
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new
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

    public void buttonToFormClick(View view) {
        Log.d("clickform","yeeee");
        Intent intent = new Intent(this, FormEdit.class);
        startActivity(intent);


    }
}

