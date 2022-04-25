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

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder>{

    private List<Item> items;
    private Context context;
    //private View.OnClickListener itemClickListener
    private final String TAG = "ItemRecyclerAdapter";

    public ItemRecyclerAdapter( List<Item> itemList , Context context) {
        this.items = itemList;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item, parent, false );
        return new ItemHolder( view );
    }

    class ItemHolder extends RecyclerView.ViewHolder /**implements View.OnClickListener**/{
        TextView name;
        TextView count;
        TextView details;
        public ItemHolder(View itemView ) {
            super(itemView);

            name = (TextView) itemView.findViewById( R.id.nameHolder );
            count = (TextView) itemView.findViewById( R.id.countHolder);
            details = (TextView) itemView.findViewById(R.id.detailsHolder);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(context, ItemLookActivity.class);
                    //intent.putExtra("name", item.getName());
                    //intent.putExtra("count", item.getCount());
                    //intent.putExtra("details", item.getDetails());
                    //context.startActivity(intent);
                    Log.d(TAG, "Item name clicked" + name.getText().toString());
                    int countInt = items.get(getAdapterPosition()).getCount();
                    DialogFragment itemViewFragment =
                            ItemViewDialogFragment.newInstance(getAdapterPosition(), name.getText().toString(), countInt, details.getText().toString() );
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

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position){
        Log.d(TAG, "onBindViewHolder");
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

    @Override
    public int getItemCount(){
        return items.size();
    }
}
