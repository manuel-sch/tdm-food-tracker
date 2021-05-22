package com.example.tdm_food_tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm_food_tracker.R;
import com.example.tdm_food_tracker.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class UserMainPage extends AppCompatActivity {

    private Button logOutButton;
    private TextView userDisplayName, userDisplayEmail;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String uID, uName, uEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        logOutButton = (Button) findViewById(R.id.userLogOutButton);
        userDisplayName = (TextView) findViewById(R.id.userDisplayName);
        userDisplayEmail = (TextView) findViewById(R.id.userDisplayEmail);



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uID = user.getUid();

        reference.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    uName = userProfile.name;
                    uEmail = userProfile.email;
                    userDisplayName.setText(uName);
                    userDisplayEmail.setText(uEmail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserMainPage.this,"Oh no something went wrong",Toast.LENGTH_LONG).show();

            }
        });

    }

    public void logOut(View view) {

        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(UserMainPage.this, MainActivity.class));


    }

}
