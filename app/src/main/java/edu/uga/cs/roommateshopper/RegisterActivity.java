package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";
    private EditText emailText, passwordText;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.registButton);
        register.setOnClickListener(new RegisterFinalizeClickListener());
    }

    /**
     * This private class if for the new quiz button, creates a new quiz intent
     */
    private class RegisterFinalizeClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            final String email = emailText.getText().toString();
            final String password = passwordText.getText().toString();

            if (!email.equals("") && !password.equals("")) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(RegisterActivity.this,
                               new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       System.out.println(task.isSuccessful());
                                       if (task.isSuccessful()) {
                                           Toast.makeText(getApplicationContext(),
                                                   "New user: " + email,
                                                   Toast.LENGTH_SHORT).show();

                                           Log.d(TAG, "RegisterFinalizeClickListener: " +
                                                   "successfully created new user");

                                           FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                           Intent intent = new Intent(RegisterActivity.this,
                                                   ShoppingManagerActivity.class);
                                           startActivity(intent);
                                       }
                                       else {
                                           Log.d(TAG, "RegisterFinalizeClickListener: " +
                                                   "failed to create new user");
                                           Toast.makeText(RegisterActivity.this,
                                                   "Registration Failed",
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
            }
            else {
                Log.d(TAG, "RegisterFinalizeClickListener: invalid email/password input");

                Toast.makeText(RegisterActivity.this, "Invalid email and/or " +
                        "password input", Toast.LENGTH_SHORT).show();
            }

            /*Log.d(TAG, "RegisterClickListener.onClick");
            if(email.getText() != null && password.getText() != null){
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
            else{

            }*/
        }
    }
}

