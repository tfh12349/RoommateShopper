package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PurchaseLookActivity extends AppCompatActivity
    implements UpdatePriceDialogFragment.UpdatePriceDialogListener {

    private final String TAG = "PurchaseLookActivity";

    private RecyclerView purchasedItemList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private TextView priceTextView;
    private Button updatePriceButton, deleteListButton;

    private List<Item> items;
    private List<Item> itemList;
    private List<String> keys;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_look);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        purchasedItemList = (RecyclerView) findViewById(R.id.purchasedItemList);
        priceTextView = (TextView) findViewById(R.id.priceTextView);

        updatePriceButton = (Button) findViewById(R.id.updatePriceButton);
        deleteListButton = (Button) findViewById(R.id.deleteListButton);

        layoutManager = new LinearLayoutManager(this);
        purchasedItemList.setLayoutManager(layoutManager);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String path = email.substring(0, email.indexOf('.'));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchases");

        items = new ArrayList<Item>();
        keys = new ArrayList<String>();
        String key = getIntent().getStringExtra("Key");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(key)) {
                        Purchase purchase = dataSnapshot.getValue(Purchase.class);
                        items.addAll(purchase.getItems());
                        userName = purchase.getUserName();
                        double price = purchase.getPrice();
                        itemList = purchase.getItems();
                        priceTextView.setText("Price: $" + price);

                        updatePriceButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogFragment updateFragment = UpdatePriceDialogFragment.newInstance(0, userName, price,
                                        itemList, key);
                                updateFragment.show(((AppCompatActivity) PurchaseLookActivity.this).getSupportFragmentManager(),
                                        "fragment");
                            }
                        });

                        deleteListButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogFragment deleteFragment = DeletePurchaseDialogFragment.newInstance(0, itemList, price,
                                        userName, key);
                                deleteFragment.show(((AppCompatActivity) PurchaseLookActivity.this).getSupportFragmentManager(),
                                        "fragment");
                            }
                        });
                    }
                }
                adapter = new PurchasedItemRecyclerAdapter(items, keys, PurchaseLookActivity.this);
                purchasedItemList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "ViewListActivity.onCreate(): could not set recycler adapter.");
            }
        });
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
    public void onFinishUpdatePriceDialog(int pos, Purchase purchase, int action, String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchases");

        if (action == 1) {
            myRef.child(key).child("items").setValue(itemList);
            myRef.child(key).child("price").setValue(purchase.getPrice());
            myRef.child(key).child("userName").setValue(userName);

            adapter.notifyItemChanged(pos);
        }
    }
}