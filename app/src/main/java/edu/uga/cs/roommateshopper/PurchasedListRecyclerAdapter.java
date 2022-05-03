package edu.uga.cs.roommateshopper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.PurchaseHolder>{

    private List<Purchase> purchases;
    private List<String> keys;
    private Context context;
    private final String TAG = "PurchaseRecyclerAdapter";

    public PurchasedListRecyclerAdapter(List<Purchase> purchases, List<String> keys, Context context) {
        this.purchases = purchases;
        this.keys = keys;
        this.context = context;
    }

    @Override
    public PurchasedListRecyclerAdapter.PurchaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchased_items, parent, false);
        return new PurchasedListRecyclerAdapter.PurchaseHolder(view);
    }

    class PurchaseHolder extends RecyclerView.ViewHolder {
        TextView purchaseTitle;
        TextView purchasePrice;
        TextView purchasedItems;
        public PurchaseHolder(View view) {
            super(view);

            purchaseTitle = (TextView) view.findViewById(R.id.purchaseTitle);
            purchasePrice = (TextView) view.findViewById(R.id.purchasePrice);
            purchasedItems = (TextView) view.findViewById(R.id.purchasedItems);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double price = purchases.get(getAdapterPosition()).getPrice();
                    List<Item> items = purchases.get(getAdapterPosition()).getItems();
                    String userName = purchases.get(getAdapterPosition()).getUserName();
                    String key = keys.get(getAdapterPosition());

                    Intent intent = new Intent(view.getContext(), PurchaseLookActivity.class);
                    intent.putExtra("Key", key);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(PurchasedListRecyclerAdapter.PurchaseHolder purchaseHolder, int pos) {
        Log.d(TAG, "onBindViewHolder()");
        Purchase purchase = purchases.get(pos);
        purchaseHolder.purchaseTitle.setText(purchase.getUserName());
        purchaseHolder.purchasePrice.setText(String.format("$" + purchase.getPrice(), "%.2f"));
        String list = "";
        for(Item i : purchase.getItems()){
            list += i.getName() + "\n";
        }
        purchaseHolder.purchasedItems.setText(list);
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }
}
