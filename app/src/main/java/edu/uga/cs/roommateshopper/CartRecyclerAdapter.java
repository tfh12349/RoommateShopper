package edu.uga.cs.roommateshopper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartHolder>{

    private List<Item> items;
    private Context context;
    private final String TAG = "ItemRecyclerAdapter";

    public CartRecyclerAdapter(List<Item> itemList , Context context) {
        this.items = itemList;
        this.context = context;
    }

    @Override
    public CartRecyclerAdapter.CartHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.cart_item, parent, false );
        return new CartRecyclerAdapter.CartHolder( view );
    }

    class CartHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        public CartHolder(View view){
            super(view);
            itemName = (TextView) view.findViewById(R.id.cartItemName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(context, ItemLookActivity.class);
                    //intent.putExtra("name", item.getName());
                    //intent.putExtra("count", item.getCount());
                    //intent.putExtra("details", item.getDetails());
                    //context.startActivity(intent);
                    //Log.d(TAG, "Item name clicked" + name.getText().toString());
                    int countInt = items.get(getAdapterPosition()).getCount();
                    String details = items.get(getAdapterPosition()).getDetails();
                    DialogFragment removeViewFragment =
                            RemoveItemDialogFragment.newInstance(getAdapterPosition(), itemName.getText().toString(), countInt, details );
                    removeViewFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "fragment");

                }
            });
        }
    }

    @Override
    public void onBindViewHolder(CartRecyclerAdapter.CartHolder cartHolder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Item item = items.get(position);
        cartHolder.itemName.setText(item.getName());
    }

    public int getItemCount(){
        return items.size();
    }
}
