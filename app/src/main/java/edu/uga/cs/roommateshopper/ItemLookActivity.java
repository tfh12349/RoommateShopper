/**
 * This is code for updating and deleting the user's price
 *
 *
 * For updating the price:
 * Let's assume current price is called price, and the new price is newPrice
 * String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
 * String cartPath = userEmail.substring(0, userEmail.indexOf('.'));
 * DatabaseReference userRef = database.getReference("users");
 *
 * userRef.addListenerForSingleValueEvent(new ValueEventListener() {
 *             @Override
 *             public void onDataChange(@NonNull DataSnapshot snapshot) {
 *                 for(DataSnapshot snapshot1: snapshot.getChildren()){
 *                     Price p = snapshot1.getValue(Price.class);
 *                     if(p.getUserName().equals(cartPath)){
 *                         String key = snapshot1.getKey();
 *                         double updatePrice = p.getTotalPrice() - price + newPrice;
 *                         userRef.child(key).child("priceTotal").setValue(updatePrice);
 *                     }
 *                 }
 *             }
 *
 *             @Override
 *             public void onCancelled(@NonNull DatabaseError error) {
 *
 *             }
 *         });
 *
 * For deleting the purchase:
 * Again, assume current price is simply called price
 *
 * String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
 *  * String cartPath = userEmail.substring(0, userEmail.indexOf('.'));
 *  * DatabaseReference userRef = database.getReference("users");
 * userRef.addListenerForSingleValueEvent(new ValueEventListener() {
 *  *             @Override
 *  *             public void onDataChange(@NonNull DataSnapshot snapshot) {
 *  *                 for(DataSnapshot snapshot1: snapshot.getChildren()){
 *  *                     Price p = snapshot1.getValue(Price.class);
 *  *                     if(p.getUserName().equals(cartPath)){
 *  *                         String key = snapshot1.getKey();
 *  *                         double updatePrice = p.getTotalPrice() - price;
 *  *                         userRef.child(key).child("priceTotal").setValue(updatePrice);
 *  *                     }
 *  *                 }
 *  *             }
 *  *
 *  *             @Override
 *  *             public void onCancelled(@NonNull DatabaseError error) {
 *  *
 *  *             }
 *  *         });
 */



package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

// This class is not used.
public class ItemLookActivity extends AppCompatActivity {

    private EditText updateName, updateCount, updateDetails;
    private Button updateButton, deleteButton, addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_look);

        Intent intent = getIntent();

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();
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
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.viewCart:
                Toast.makeText(this, "View Cart Clicked", Toast.LENGTH_SHORT).show();
                Intent intentTwo = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intentTwo);
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}