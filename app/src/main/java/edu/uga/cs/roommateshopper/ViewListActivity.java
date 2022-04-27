package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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

public class ViewListActivity extends AppCompatActivity
        implements AddItemDialogFragment.AddItemDialogListener, ItemViewDialogFragment.ItemViewDialogListener {

    private final String TAG = "ViewListActivity";
    private RecyclerView recyclerView;
    //private FloatingActionButton floater;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private List<Item> items;
    private List<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddItemDialogFragment();
                showDialogFragment( newFragment );
            }
        });

        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");

        items = new ArrayList<Item>();
        keys = new ArrayList<String>();

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item item = postSnapshot.getValue(Item.class);
                    items.add(item);
                    keys.add(postSnapshot.getKey());
                    Log.d( TAG, "ViewListActivity.onCreate(): added: " + item );
                }
                Log.d( TAG, "ViewListActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a JobLeadRecyclerAdapter to populate a ReceyclerView to display the job leads.
                adapter = new ItemRecyclerAdapter( items, keys, ViewListActivity.this);
                recyclerView.setAdapter( adapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );
    }

    @Override
    public void onFinishNewItemDialog(Item item) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");
        myRef.push().setValue( item )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Update the recycler view to include the new job lead
                        items.add( item );
                        keys.add(myRef.push().getKey());
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
                        Toast.makeText( getApplicationContext(), "Failed to create a Job lead for " + item.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onFinishItemViewDialog(int position, Item item, int action, String key){ //originalDetails used to be at end
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");

        if(action == 1){
            //DatabaseReference updateRef = myRef.child(item.getDetails());
            ///Query query = myRef.child(key);
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
            items.set(position, item);
            adapter.notifyItemChanged(position);
        }
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
            myRef.child(key).removeValue();
            items.remove(position);
            keys.remove(position);
            adapter.notifyItemRemoved(position);
        }
        else if (action == 3){
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String cartPath = userEmail.substring(0, userEmail.indexOf('.'));
            DatabaseReference cartRef = database.getReference("carts").child(cartPath);
            cartRef.push().setValue(item);

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