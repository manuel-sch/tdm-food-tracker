package com.example.tdm_food_tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm_food_tracker.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainPage extends AppCompatActivity {

    private Button logOutButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        logOutButton = (Button) findViewById(R.id.userLogOutButton);


    }

    public void logOut(View view) {

        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(UserMainPage.this, MainActivity.class));


    }

}
