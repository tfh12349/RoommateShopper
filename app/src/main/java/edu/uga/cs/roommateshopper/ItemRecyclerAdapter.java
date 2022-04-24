package edu.uga.cs.roommateshopper;

import android.content.Context;
import android.content.Intent;
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

    public ItemRecyclerAdapter( List<Item> itemList, Context context ) {
        this.items = itemList;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item, parent, false );
        return new ItemHolder( view );
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView count;
        TextView details;
        public ItemHolder(View itemView ) {
            super(itemView);

            name = (TextView) itemView.findViewById( R.id.nameHolder );
            count = (TextView) itemView.findViewById( R.id.countHolder);
            details = (TextView) itemView.findViewById(R.id.detailsHolder);
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position){
        Item item = items.get(position);
        itemHolder.name.setText(item.getName());
        itemHolder.count.setText("Count: " + item.getCount());
        itemHolder.details.setText(item.getDetails());

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemLookActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return items.size();
    }
}
