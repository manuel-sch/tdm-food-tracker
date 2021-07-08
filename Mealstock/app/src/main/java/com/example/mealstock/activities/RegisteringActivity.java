package com.example.mealstock.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealstock.R;
import com.example.mealstock.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



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
        String confirmPassword = userPasswordConfirm.getText().toString().trim();

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


        if(confirmPassword.isEmpty()){
            userPasswordConfirm.setError(getString(R.string.field_not_filled_out));
            userPasswordConfirm.requestFocus();
            return;
        }



        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError(getString(R.string.email_pattern_error));
            userEmail.requestFocus();
            return;
        }

        if(!confirmPassword.equals(password)){
            Log.d("nya", "toast 2: "+name+password+email+confirmPassword);
            userPasswordConfirm.setError("Stimmt nicht überein");
            userPasswordConfirm.requestFocus();

            return;
        }




        if(password.length() < 6){
            userPasswordConfirm.setError(getString(R.string.password_to_short));
            userPasswordConfirm.requestFocus();
            return;

        }

        Log.d("nya", "toast 2: "+name+password+email+confirmPassword);

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
                                        progressBar.setVisibility(View.VISIBLE);
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisteringActivity.this);
                                        dialog.setMessage(R.string.user_has_been_registered_successfully);
                                        dialog.setTitle("Yay");
                                        AlertDialog alertDialog = dialog.create();
                                        alertDialog.show();

                                        onBackPressed();
                                    } else {
                                        progressBar.setVisibility(View.VISIBLE);
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisteringActivity.this);
                                        dialog.setMessage("Failed to register!");
                                        dialog.setTitle("Failed to register. Maybe you email is already used. Please Try again");
                                        AlertDialog alertDialog = dialog.create();
                                        alertDialog.show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(RegisteringActivity.this);
                            dialog.setMessage(R.string.user_has_been_registered_failed);
                            dialog.setTitle("Oh No");
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
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
