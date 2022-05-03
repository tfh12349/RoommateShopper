package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the activity for the cart.
 */
public class CartActivity extends AppCompatActivity
        implements PurchaseDialogFragment.PurchaseDialogListener, RemoveItemDialogFragment.RemoveItemDialogListener{

    private final String TAG = "CartActivity";

    // Set up the RecyclerView, the Layout Manager, and the adapter, as well as the Button
    private RecyclerView recyclerView;
    private Button button;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    // Set up the list of items and the list of keys (keys are for the items
    private List<Item> items;
    private List<String> keys;

    /**
     * This is the onCreate method. It sets up the action bar, the recycler view, and the adapter
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Set up the action bar
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        // Set the recyclerView to the cartList
        recyclerView = (RecyclerView) findViewById( R.id.cartList );

        // Set up the button
        button = (Button) findViewById(R.id.purchaseCart);
        // Set up the onClickListener
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the PurchaseDialogFragment
                DialogFragment newFragment = new PurchaseDialogFragment();
                newFragment.show(getSupportFragmentManager(), null);
            }
        });

        // Set up the layoutManager and give it to the recyclerView
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );

        // Set the cartPath string by getting the beginning of the user email
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String cartPath = userEmail.substring(0, userEmail.indexOf('.'));

        // Get the firebase database and get the cart for the user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts").child(cartPath);

        // Set the two lists to new ArrayLists of the necessary types
        items = new ArrayList<Item>();
        keys = new ArrayList<String>();

        // Add the listener for the single event
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // For every item in the list
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    // Get the item in the form of the Item class
                    Item item = postSnapshot.getValue(Item.class);
                    // Add the item to items and add the key to keys
                    items.add(item);
                    keys.add(postSnapshot.getKey());//myRef.getKey());
                    Log.d( TAG, "ViewListActivity.onCreate(): added: " + item );
                }
                Log.d( TAG, "ViewListActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a cartRecyclerAdapter to populate a RecyclerView to display the cart.
                adapter = new CartRecyclerAdapter( items, keys, CartActivity.this);
                recyclerView.setAdapter( adapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );
    }

    /**
     * This method creates the option menu, based on the menu_signed_in_menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signed_in_menu, menu);
        return true;
    }

    /**
     * This method is for when one of the options for the menu is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                // Print a message
                Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                // Sign out the user
                FirebaseAuth.getInstance().signOut();
                // Return to the MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.viewCart:
                // Print a message
                Toast.makeText(this, "Already Viewing Cart", Toast.LENGTH_SHORT).show();
                //Intent intentTwo = new Intent(getApplicationContext(), CartActivity.class);
                //startActivity(intentTwo);
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This is a method overridden from the PurchaseDialogFragment.PurchaseDialogListener class. It
     * is used to finish the purchase
     *
     * @param price
     */
    @Override
    public void onFinishPurchaseDialog(double price){
        // Set up the cart path
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String cartPath = userEmail.substring(0, userEmail.indexOf('.'));

        // Get the database and the cart of the user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts").child(cartPath);
        DatabaseReference userRef = database.getReference("users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Price p = snapshot1.getValue(Price.class);
                    if(p.getUserName().equals(cartPath)){
                        String key = snapshot1.getKey();
                        double newPrice = p.getPriceTotal() + price;
                        userRef.child(key).child("priceTotal").setValue(newPrice);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Get the purchases list
        DatabaseReference newRef = database.getReference("purchases");

        // Create a purchase, and add it to the purchases list
        Purchase newPurch = new Purchase(cartPath, price, items);
        newRef.push().setValue(newPurch);

        // Remove the cart
        myRef.removeValue();

        /**Query query = myRef.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });**/
        // Notify the adapter that it is all gone, and clear the lists
        adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
        items.clear();
        keys.clear();
    }

    /**
     * This is a method overridden from the RemoveItemDialogFragment.RemoveItemDialogListener class. It
     * is used to finish removing an item
     *
     * @param position
     * @param item
     * @param key
     */
    @Override
    public void onFinishRemoveItemDialog(int position, Item item, String key ){
        // Set up the cart path
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String cartPath = userEmail.substring(0, userEmail.indexOf('.'));

        // Get the database and the cart of the user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts").child(cartPath);

        // Get a reference to the shopping list, then add the item back
        DatabaseReference shoppingReference = database.getReference("shoppinglist");
        shoppingReference.push().setValue(item);

        // Remove the value from the cart
        Log.d("CartActivity", "key: " + key);
        myRef.child(key).removeValue();
        /**Query query = myRef.child(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });**/

        // Remove the item from the lists and notify the adapter
        items.remove(position);
        keys.remove(position);
        adapter.notifyItemRemoved(position);
    }
}