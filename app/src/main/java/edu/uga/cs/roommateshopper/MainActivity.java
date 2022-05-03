package edu.uga.cs.roommateshopper;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Button objects for the two buttons
    private final String TAG = "MainActivity";
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registrationButton);

        login.setOnClickListener(new LoginClickListener());
        register.setOnClickListener(new RegisterClickListener());
    }

    /**
     * This private class if for the login button, starts the login process
     */
    private class LoginClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );

            Log.d(TAG, "MainActivity.LoginClickListener.onClick: Begun signing in.");

            // creates the sign in activity based on the
            // AuthUI.SignInIntentBuilder
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Theme_RoommateShopper)
                    .build();
            signInLauncher.launch(signInIntent);
        }
    }

    // this launcher starts the AuthUI login process, then overrides
    // on activity result to call onSignInResult, which will handle
    // the result of the intent
    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onSignInResult(result);
                        }
                    });

    /**
     * This method handles the result of the login process. If a
     * valid user was called, it is obtained by the process and
     * calls the ShoppingManagerActivity. Otherwise, it returns
     * the user to the MainActivity and informs them of the error
     * @param result the result of the login process
     */
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        // if the user had a successful login
        if (result.getResultCode() == RESULT_OK)  {
            // if the response exists
            if(response != null) {
                Log.d(TAG, "MainActivity: onSignInResult: response.getEmail(): " + response.getEmail());
            }


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
            Query query = myRef.orderByChild("userName").equalTo(response.getEmail().substring(0, response.getEmail().indexOf('.')));

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Price p = new Price(response.getEmail().substring(0, response.getEmail().indexOf('.')), 0);
                        myRef.push().setValue(p);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Intent intent = new Intent(this, ShoppingManagerActivity.class);
            startActivity(intent);
        }
        // if the login failed for any reason
        else {
            Log.d(TAG, "MainActivity: onSignInResult: Sign-in failed.");

            Toast.makeText(getApplicationContext(),
                    "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This private class if for the registration button, creates a registration intent
     */
    private class RegisterClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            Log.d(TAG, "RegisterClickListener.onClick");
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}