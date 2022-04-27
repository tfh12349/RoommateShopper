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

public class CartActivity extends AppCompatActivity
        implements PurchaseDialogFragment.PurchaseDialogListener, RemoveItemDialogFragment.RemoveItemDialogListener{

    private final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private Button button;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private List<Item> items;
    private List<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        recyclerView = (RecyclerView) findViewById( R.id.cartList );

        button = (Button) findViewById(R.id.purchaseCart);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PurchaseDialogFragment();
                newFragment.show(getSupportFragmentManager(), null);
            }
        });

        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String cartPath = userEmail.substring(0, userEmail.indexOf('.'));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts").child(cartPath);

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
                    keys.add(myRef.getKey());
                    Log.d( TAG, "ViewListActivity.onCreate(): added: " + item );
                }
                Log.d( TAG, "ViewListActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a JobLeadRecyclerAdapter to populate a ReceyclerView to display the job leads.
                adapter = new CartRecyclerAdapter( items, keys, CartActivity.this);
                recyclerView.setAdapter( adapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );
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

    @Override
    public void onFinishPurchaseDialog(double price){
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String cartPath = userEmail.substring(0, userEmail.indexOf('.'));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts").child(cartPath);
        DatabaseReference newRef = database.getReference("purchases");
        Purchase newPurch = new Purchase(cartPath, price, items);
        newRef.push().setValue(newPurch);

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
        adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
        items.clear();
        keys.clear();
    }

    @Override
    public void onFinishRemoveItemDialog(int position, Item item, String key ){
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String cartPath = userEmail.substring(0, userEmail.indexOf('.'));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts").child(cartPath);
        DatabaseReference shoppingReference = database.getReference("shoppinglist");
        shoppingReference.push().setValue(item);
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

        items.remove(position);
        adapter.notifyItemRemoved(position);
    }
}