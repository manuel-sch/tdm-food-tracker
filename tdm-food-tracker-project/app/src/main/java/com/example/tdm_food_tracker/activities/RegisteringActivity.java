package com.example.tdm_food_tracker.activities;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm_food_tracker.R;
import com.example.tdm_food_tracker.user.User;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class RegisteringActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText userEmail, userName, userPassword, userPasswordConfirm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);

        mAuth = FirebaseAuth.getInstance();

        userName = (EditText) findViewById(R.id.userNameInput);
        userEmail = (EditText) findViewById(R.id.userEmailInput);
        userPassword = (EditText) findViewById(R.id.userPasswordInput);
        userPasswordConfirm = (EditText) findViewById(R.id.userPasswordConfirmInput);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }

    public void registerUser(View v){
        String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmpassword = userPasswordConfirm.getText().toString().trim();

        if(name.isEmpty()){
            userName.setError(getString(R.string.field_not_filled_out));
            userName.requestFocus();
            return;
        }


        if(email.isEmpty()){
            userEmail.setError(getString(R.string.field_not_filled_out));
            userEmail.requestFocus();
            return;
        }



        if(password.isEmpty()){
            userPassword.setError(getString(R.string.field_not_filled_out));
            userPassword.requestFocus();
            return;
        }


        if(confirmpassword.isEmpty()){
            userPasswordConfirm.setError(getString(R.string.field_not_filled_out));
            userPasswordConfirm.requestFocus();
            return;
        }


        /*
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError(getString(R.string.email_pattern_error));
            userEmail.requestFocus();
            return;
        }

         */



        if(password.length() < 6){
            userPasswordConfirm.setError(getString(R.string.password_to_short));
            userPasswordConfirm.requestFocus();
            return;

        }

        Log.d("nya", "toast 2: "+name+password+email+confirmpassword);
        toToast(name+password+email+confirmpassword);


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(name, email);
                            Log.d("nya", "toast 1: ");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("nya", "toast: ");
                                        Toast.makeText(RegisteringActivity.this, R.string.user_has_been_registered_successfully, Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(RegisteringActivity.this, "Faild to register! Please try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisteringActivity.this, R.string.user_has_been_registered_failed, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }

    public String toToast(String massage){
        Toast.makeText(RegisteringActivity.this, massage, Toast.LENGTH_LONG).show();
        return massage;
    }

}
