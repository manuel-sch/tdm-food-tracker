package com.example.tdm_food_tracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tdm_food_tracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RequestTester reqTester;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = activityMainBinding.getRoot();
        setContentView(viewRoot);
        AppInfoConstants utilConstants = new AppInfoConstants(this);
        reqTester = new RequestTester(this);

        Button testButton = findViewById(R.id.button);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Produkteingabe");

                final EditText userInput = new EditText(MainActivity.this);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(userInput);

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,"Produkt erfolgreich gespeichert",Toast.LENGTH_LONG).show();
                    }
                });

                dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    public void testQueue(View view) {

        reqTester.testBarcodeSearchQueueOpenFoodFacts();
        //reqTester.testProductNameSearchQueueOpenFoodFacts();
    }


}