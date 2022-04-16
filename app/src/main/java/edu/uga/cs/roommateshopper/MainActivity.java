package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Button objects for the two buttons
    private final String TAG = "MainActivity";
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registrationButton);

        login.setOnClickListener(new LoginClickListener());
        register.setOnClickListener(new RegisterClickListener());
    }

    /**
     * This private class if for the new quiz button, creates a new quiz intent
     */
    private class LoginClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            Log.d(TAG, "LoginClickListener.onClick");
        }
    }

    /**
     * This private class if for the new quiz button, creates a new quiz intent
     */
    private class RegisterClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            Log.d(TAG, "RegisterClickListener.onClick");
        }
    }
}