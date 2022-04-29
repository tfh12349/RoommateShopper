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

/**
 * This is the CartRecyclerAdapter used for the cart
 */
public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartHolder>{

    // Set up the list of items, keys, and the context
    private List<Item> items;
    private List<String> keys;
    private Context context;
    private final String TAG = "CartRecyclerAdapter";

    /**
     * This is the constructor for the class. It takes in the itemList, keys, and context, and sets
     * up the local variables
     * @param itemList
     * @param keys
     * @param context
     */
    public CartRecyclerAdapter(List<Item> itemList, List<String> keys , Context context) {
        // Set the local variables to the passed in elements
        this.items = itemList;
        this.keys = keys;
        this.context = context;
    }

    /**
     * This class creates the viewholder for each view, with the cart_item layout used
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CartRecyclerAdapter.CartHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.cart_item, parent, false );
        return new CartRecyclerAdapter.CartHolder( view );
    }

    /**
     * This is the CartHolder class, which is used for the setup of the cart elements in the recyclerView
     */
    class CartHolder extends RecyclerView.ViewHolder{
        // Set up the TextView
        TextView itemName;
        public CartHolder(View view){
            super(view);
            // Set the textView up
            itemName = (TextView) view.findViewById(R.id.cartItemName);
            // Set the onClickListener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(context, ItemLookActivity.class);
                    //intent.putExtra("name", item.getName());
                    //intent.putExtra("count", item.getCount());
                    //intent.putExtra("details", item.getDetails());
                    //context.startActivity(intent);
                    //Log.d(TAG, "Item name clicked" + name.getText().toString());

                    // Get the count and details, and the key of the item
                    int countInt = items.get(getAdapterPosition()).getCount();
                    String details = items.get(getAdapterPosition()).getDetails();
                    String key = keys.get(getAdapterPosition());
                    // Create the dialogFragment for removing the item, then show it
                    DialogFragment removeViewFragment =
                            RemoveItemDialogFragment.newInstance(getAdapterPosition(), itemName.getText().toString(), countInt, details, key );
                    removeViewFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "fragment");

                }
            });
        }
    }

    /**
     * The onBindViewHolder method just sets up the elements on the cartHolder
     * @param cartHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(CartRecyclerAdapter.CartHolder cartHolder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Item item = items.get(position);
        cartHolder.itemName.setText(item.getName());
    }

    /**
     * This method returns the count
     * @return
     */
    public int getItemCount(){
        return items.size();
    }
}
