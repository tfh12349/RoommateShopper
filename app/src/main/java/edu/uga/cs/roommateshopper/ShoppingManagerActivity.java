package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ShoppingManagerActivity extends AppCompatActivity {

    private final String TAG = "ShoppingManagerActivity";
    private Button listButton, purchasedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_manager);

        listButton = findViewById(R.id.currentList);
        purchasedButton = findViewById(R.id.purchasedList);
        listButton.setOnClickListener(new ListClickListener());
        purchasedButton.setOnClickListener(new PurchasedClickListener());
    }

    /**
     * This private class if for the new quiz button, creates a new quiz intent
     */
    private class ListClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            Log.d(TAG, "ListClickListener.onClick");
            Intent intent = new Intent(view.getContext(), ViewListActivity.class);
            view.getContext().startActivity(intent);
        }
    }
    /**
     * This private class if for the new quiz button, creates a new quiz intent
     */
    private class PurchasedClickListener implements View.OnClickListener{
        /**
         * onClick stores the creation of the intent and starts the activity
         * @param view
         */
        @Override
        public void onClick(View view){
            Log.d(TAG, "PurchasedClickListener.onClick");
        }
    }
}