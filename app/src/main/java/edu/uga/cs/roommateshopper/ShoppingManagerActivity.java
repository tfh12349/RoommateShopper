package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShoppingManagerActivity extends AppCompatActivity {

    private final String TAG = "ShoppingManagerActivity";
    private Button listButton, purchasedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_manager);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        listButton = findViewById(R.id.currentList);
        purchasedButton = findViewById(R.id.purchasedList);
        listButton.setOnClickListener(new ListClickListener());
        purchasedButton.setOnClickListener(new PurchasedClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signed_in_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.viewCart:
                Toast.makeText(this, "View Cart Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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