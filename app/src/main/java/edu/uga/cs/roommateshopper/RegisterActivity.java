package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";
    private EditText email, password;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
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
            Log.d(TAG, "RegisterClickListener.onClick");
            if(email.getText() != null && password.getText() != null){
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
            else{

            }
        }
    }
}

