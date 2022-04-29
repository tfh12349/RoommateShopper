package edu.uga.cs.roommateshopper;

import android.content.Context;
import android.content.Intent;
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
 * This is the RecyclerAdapter used for the shopping list
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder>{

    // Set up the list of items, keys, and the context
    private List<Item> items;
    private List<String> keys;
    private Context context;
    //private View.OnClickListener itemClickListener
    private final String TAG = "ItemRecyclerAdapter";

    /**
     * This is the constructor for the class. It takes in the itemList, keys, and context, and sets
     * up the local variables
     * @param itemList
     * @param keyList
     * @param context
     */
    public ItemRecyclerAdapter( List<Item> itemList , List<String> keyList, Context context) {
        // Set the local variables to the passed in elements
        this.items = itemList;
        this.keys = keyList;
        this.context = context;
    }

    /**
     * This class creates the viewholder for each view, with the item layout used
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item, parent, false );
        return new ItemHolder( view );
    }

    /**
     * This is the ItemHolder class, which is used for the setup of the item elements in the recyclerView
     */
    class ItemHolder extends RecyclerView.ViewHolder /**implements View.OnClickListener**/{
        // Set up the textViews
        TextView name;
        TextView count;
        TextView details;

        /**
         * This is the constructor that sets up the ItemHolder
         * @param itemView
         */
        public ItemHolder(View itemView ) {
            super(itemView);

            // Set up the name, count, and details textViews
            name = (TextView) itemView.findViewById( R.id.nameHolder );
            count = (TextView) itemView.findViewById( R.id.countHolder);
            details = (TextView) itemView.findViewById(R.id.detailsHolder);
            // Set up the onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(context, ItemLookActivity.class);
                    //intent.putExtra("name", item.getName());
                    //intent.putExtra("count", item.getCount());
                    //intent.putExtra("details", item.getDetails());
                    //context.startActivity(intent);
                    // Print a Log message
                    Log.d(TAG, "Item name clicked" + name.getText().toString());
                    // set the count to the clicked item count, and the key to the matching key
                    int countInt = items.get(getAdapterPosition()).getCount();
                    String key = keys.get(getAdapterPosition());
                    // Set up the itemViewFragment and show it
                    DialogFragment itemViewFragment =
                            ItemViewDialogFragment.newInstance(getAdapterPosition(), name.getText().toString(), countInt, details.getText().toString(), key );
                    itemViewFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "fragment");

                }
            });
        }

        /**@Override
        public void onClick(View v){
            Log.d(TAG, "Item name clicked" + name.getText().toString());
            DialogFragment itemViewFragment =
                    ItemViewDialogFragment.newInstance( this.getAdapterPosition(), name.getText().toString(), Integer.parseInt(count.getText().toString()), details.getText().toString() );
            itemViewFragment.show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), null);
        }**/
    }

    /**
     * The onBindViewHolder method just sets up the elements on the itemHolder
     * @param itemHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position){
        Log.d(TAG, "onBindViewHolder");
        // Set the text of the three textViews
        Item item = items.get(position);
        itemHolder.name.setText(item.getName());
        itemHolder.count.setText("Count: " + item.getCount());
        itemHolder.details.setText(item.getDetails());

        /**itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, ItemLookActivity.class);
                //intent.putExtra("name", item.getName());
                //intent.putExtra("count", item.getCount());
                //intent.putExtra("details", item.getDetails());
                //context.startActivity(intent);
                Log.d(TAG, "Item name clicked" + item.getName());
                DialogFragment itemViewFragment =
                        ItemViewDialogFragment.newInstance( itemHolder.getAdapterPosition(), item.getName(), item.getCount(), item.getDetails() );
                itemViewFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), "fragment");

            }
        });**/
    }

    /**
     * This method returns the count
     * @return
     */
    @Override
    public int getItemCount(){
        return items.size();
    }
}
