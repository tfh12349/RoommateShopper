package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the song for the viewing of the shopping list. It creates the recycler view, deals with the adapter,
 * and shows the current items.
 *
 * It implements the DialogListeners from the dialogs that would show up.
 */
public class ViewListActivity extends AppCompatActivity
        implements AddItemDialogFragment.AddItemDialogListener, ItemViewDialogFragment.ItemViewDialogListener {

    // The TAG for the class
    private final String TAG = "ViewListActivity";

    // Set up the RecyclerView, the Layout Manager, and the adapter
    private RecyclerView recyclerView;
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
        setContentView(R.layout.activity_view_list);

        // Set up the action bar
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        // Set the recyclerView
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );

        // Set up the FloatingActionButton
        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        // Set the onClickListener
        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set up the AddItemDialogFragment and show it
                DialogFragment newFragment = new AddItemDialogFragment();
                //showDialogFragment( newFragment );
                newFragment.show(getSupportFragmentManager(), "addFragment");
            }
        });

        // Set up the layoutManager and give it to the recyclerView
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );

        // Get the firebase database and get the shopping list
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");

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
                    keys.add(postSnapshot.getKey());
                    Log.d( TAG, "ViewListActivity.onCreate(): added: " + item );
                }
                Log.d( TAG, "ViewListActivity.onCreate(): setting recyclerAdapter" );

                // Create a ItemRecyclerAdapter to populate a RecyclerView to display the items.
                adapter = new ItemRecyclerAdapter( items, keys, ViewListActivity.this);
                recyclerView.setAdapter( adapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );
    }

    /**
     * This is a method overridden from the AddItemDialogFragment.AddItemDialogListener class. It is
     * used to complete the addition of the new item, including adding the item to the list, getting
     * the key, etc.
     * @param item
     */
    @Override
    public void onFinishNewItemDialog(Item item) {
        // Get the database and the shopping list
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");
        // Set the value and add an onSuccessListener
        String addValue = myRef.push().getKey();
        myRef.child(addValue).setValue(item).addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Add the item to items, the key to keys, and update the adapter
                        items.add( item );
                        keys.add(addValue);//myRef.push().getKey());
                        Log.d(TAG, "Key added: " + keys.get(keys.size()-1));
                        adapter.notifyItemInserted(items.size() - 1);

                        Log.d( TAG, "Item saved: " + item );
                        // Show a quick confirmation
                        Toast.makeText(getApplicationContext(), "Item created for " + item.getName(),
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText( getApplicationContext(), "Failed to create an item for " + item.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * This is a method overridden from the ItemViewDialogFragment.ItemViewDialogListener class. It
     * is used to complete one of 3 tasks: update (1), delete (2), or add to cart (3)
     *
     * @param position
     * @param item
     * @param action
     * @param key
     */
    @Override
    public void onFinishItemViewDialog(int position, Item item, int action, String key){
        // Get the database and the shopping list
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");

        // If the action is updating the item
        if(action == 1){
            // Set the elements of the item in the database
            myRef.child(key).child("name").setValue(item.getName());
            myRef.child(key).child("count").setValue(item.getCount());
            myRef.child(key).child("details").setValue(item.getDetails());

            /**query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        //snapshot.getRef().child("name").setValue(item.getName());
                        //snapshot.getRef().child("details").setValue(item.getDetails());
                        if(snapshot.getKey().equals("name")){ snapshot.getRef().setValue(item.getName()); }
                        else if(snapshot.getKey().equals("count")){ snapshot.getRef().setValue(item.getCount()); }
                        else if(snapshot.getKey().equals("details")){ snapshot.getRef().setValue(item.getDetails()); }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });**/
            //updateRef.child("name").setValue(item.getName());
            //updateRef.child("count").setValue(item.getCount());
            //updateRef.child("details").setValue(item.getDetails());

            // Set the item in the list, and notify the adapter of the change
            items.set(position, item);
            adapter.notifyItemChanged(position);
        }
        // Else if the action is deleting the item
        else if(action == 2){
            //Query query = myRef.child(key); //orderByChild("details").equalTo(item.getDetails());

            /**query.addListenerForSingleValueEvent(new ValueEventListener() {
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
            // Remove the element using the key
            Log.d("ViewListActivity", "key: " + key);
            myRef.child(key).removeValue();

            // Update the lists, then notify the adapter of the removed item
            items.remove(position);
            keys.remove(position);
            adapter.notifyItemRemoved(position);
        }
        // else if the action is adding to cart
        else if (action == 3){
            // get the user email, then get the usable part for the cart database
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String cartPath = userEmail.substring(0, userEmail.indexOf('.'));

            // Get the reference to the cart, and add the item
            DatabaseReference cartRef = database.getReference("carts").child(cartPath);
            cartRef.push().setValue(item);

            // Remove the item from the database
            Log.d("ViewListActivity", "key: " + key);
            myRef.child(key).removeValue();
            //Query query = myRef.child(key);//.orderByChild("details").equalTo(item.getDetails());

            /**query.addListenerForSingleValueEvent(new ValueEventListener() {
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
            // Update the lists, then notify the adapter of the removed item
            items.remove(position);
            keys.remove(position);
            adapter.notifyItemRemoved(position);
        }
        else{
            Toast.makeText( getApplicationContext(), "Failed to do anything" + item.getName(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    void showDialogFragment( DialogFragment newFragment ) {
        newFragment.show( getSupportFragmentManager(), null);
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
        // The switch for the different menu buttons
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
                Toast.makeText(this, "View Cart Clicked", Toast.LENGTH_SHORT).show();
                // Go to the cart page
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

    @Override
    protected void onResume() {
        Log.d( TAG, "ViewListActivity.onResume()" );
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( TAG, "ViewListActivity.onPause()" );
        super.onPause();
    }
}