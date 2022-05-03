package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettleActivity extends AppCompatActivity {

    private TextView totalPrice, pricePerPerson;
    private Button button;
    private List<String> userNames;
    private double totalPriceDouble;
    String allIndividualText;
    private List<Price> prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.show();

        totalPrice = findViewById(R.id.totalPrice);
        pricePerPerson = findViewById(R.id.pricePerPerson);
        button = findViewById(R.id.settleFinish);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchases");
        DatabaseReference userRef = database.getReference("users");

        /**userNames = new ArrayList<String>();
        double personalPrice;**/

        //prices = new ArrayList<Price>();
        allIndividualText = "";
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Price price = postSnapshot.getValue(Price.class);
                    allIndividualText += price.getUserName() + ": $" + String.format("" + price.getPriceTotal(), "%.2f") + "\n";
                }
                pricePerPerson.setText(allIndividualText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /**for(String user : userNames){
            prices.add(new Price(user, 0));
        }**/
        //Price priceTotal;
        totalPriceDouble = 0;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Purchase purchase = postSnapshot.getValue(Purchase.class);
                    /**if(!userNames.contains(purchase.getUserName())){
                        userNames.add(purchase.getUserName());
                        //priceTotal = new Price(purchase.getUserName(), 0);
                        prices.add(new Price(purchase.getUserName(), 0));
                    }**/
                    totalPriceDouble += purchase.getPrice();
                    Log.d("SettleActivity", "Total Price = " + totalPriceDouble);
                }
                totalPrice.setText(String.format("Total Price: $" + totalPriceDouble, "%.2f"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /**for(String user: userNames){
            Log.d("SettleActivity", "User" + user);
        }**/
        /**for(Price pri : prices){
            Log.d("SettleActivity", "Price " + pri.getUserName());
        }**/

        /**for(Price pri : prices) {
            //Query queryForIndividual = myRef.orderByKey();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postSnapshot: snapshot.getChildren()){
                        Purchase purchase = postSnapshot.getValue(Purchase.class);
                        if(purchase.getUserName().equals(pri.getUserName())){
                            pri.addToPrice(purchase.getPrice());
                        }
                    }
                    if(prices.get(prices.size()-1).equals(pri)){
                        String allIndividualText = "";
                        for(Price pri: prices){
                            allIndividualText += pri.getUserName() + ": $" + String.format("" + pri.getPriceTotal(), "%.2f") + "\n";
                        }
                        pricePerPerson.setText(allIndividualText);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }**/

        //totalPrice.setText(String.format("Total Price: $" + totalPriceDouble, "%.2f"));
        //String allIndividualText = "";
        /**for(Price pri: prices){
            allIndividualText += pri.getUserName() + ": $" + String.format("" + pri.getPriceTotal(), "%.2f") + "\n";
        }**/

        //pricePerPerson.setText(allIndividualText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.removeValue();
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            Price p = snapshot1.getValue(Price.class);
                            String key = snapshot1.getKey();
                            userRef.child(key).child("priceTotal").setValue(0);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(getApplicationContext(), PurchasedListActivity.class);
                startActivity(intent);
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
}