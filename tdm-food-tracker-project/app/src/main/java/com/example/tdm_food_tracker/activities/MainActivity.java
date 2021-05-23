package com.example.tdm_food_tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm_food_tracker.R;
import com.example.tdm_food_tracker.constants.AppInfoConstants;
import com.example.tdm_food_tracker.databinding.ActivityMainBinding;
import com.example.tdm_food_tracker.utils.RequestTester;
import com.example.tdm_food_tracker.viewmodels.ProductListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 0;
    private final String TAG = MainActivity.class.getSimpleName();


    //Google signIn
    private GoogleSignInClient mGoogleSignInClient;
    private RequestTester reqTester;

    // ViewBinding ermöglicht direktes zugreifen auf Views aus Layout über Objekt mit .viewName
    private ActivityMainBinding activityMainBinding;

    private ProductListViewModel productViewModel;


    private FirebaseAuth mAuth;
    TextView password, email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        AppInfoConstants utilConstants = new AppInfoConstants(this);
        reqTester = new RequestTester(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //LogIn
        password = (TextView) findViewById(R.id.signInPasswordInput);
        email = (TextView) findViewById(R.id.signInEmailInput);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();


    }

    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_SIGN_IN:
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                    break;
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "onActivityResult not ok result.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(this, GoogleSignInActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error sign in", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    public void goToInputActivity(View view) {
        Intent intent = new Intent(this, ProductInputActivity.class);
        startActivity(intent);
    }

    public void goToRegisteringActivity(View view) {
        Intent intent = new Intent(this, RegisteringActivity.class);
        startActivity(intent);
    }

    public void userPasswordForgotButton(View v){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);

    }

    public void userLogIn(View view) {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(userPassword.isEmpty()){
            password.setError(""+R.string.field_not_filled_out);
            password.requestFocus();
            return;
        }

        if(userEmail.isEmpty()){
            email.setError(""+R.string.field_not_filled_out);
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError(""+R.string.email_pattern_error);
            email.requestFocus();
            return;
        }

        if(password.length() < 6){
            password.setError(""+R.string.password_to_short);
            password.requestFocus();
            return;

        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {

                        startActivity(new Intent(MainActivity.this, UserMainPageActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Failed to log in. Check your Credits", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}

