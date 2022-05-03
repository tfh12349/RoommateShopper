package edu.uga.cs.roommateshopper;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasedItemRecyclerAdapter extends RecyclerView.Adapter<PurchasedItemRecyclerAdapter.ItemHolder>{
    private List<Item> items;
    private List<String> keys;
    private Context context;
    private final String TAG = "PurItemRecyclerAdapter";

    public PurchasedItemRecyclerAdapter(List<Item> items, List<String> keys, Context context) {
        this.items = items;
        this.keys = keys;
        this.context = context;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;

        public ItemHolder(View view) {
            super(view);

            itemName = (TextView) view.findViewById(R.id.cartItemName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = items.get(getAdapterPosition()).getCount();
                    String details = items.get(getAdapterPosition()).getDetails();
                    System.out.println(getAdapterPosition());
                    String key = keys.get(0);

                    DialogFragment deleteItem = DeletePurchasedItemDialog.newInstance(
                            getAdapterPosition(), itemName.getText().toString(), count, details, key);
                    deleteItem.show(((AppCompatActivity) context).getSupportFragmentManager(), "fragment");
                }
            });
        }
    }

    public PurchasedItemRecyclerAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new PurchasedItemRecyclerAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(PurchasedItemRecyclerAdapter.ItemHolder itemHolder, int pos) {
        Log.d(TAG, "onBindViewHolder");
        Item item = items.get(pos);
        itemHolder.itemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
