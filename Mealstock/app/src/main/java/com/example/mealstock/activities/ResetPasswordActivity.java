package com.example.mealstock.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText userEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = (EditText) findViewById(R.id.resetPasswordWithEmail);


    }

    public void resetPassword(View v){

        String email = userEmail.getText().toString().trim();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ResetPasswordActivity.this);
                            dialog.setMessage("Check your email to reset your password");
                            dialog.setTitle("Password Reset");
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        }
                    }
                });

    }
}
