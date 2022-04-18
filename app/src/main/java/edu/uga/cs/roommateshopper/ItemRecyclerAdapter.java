package edu.uga.cs.roommateshopper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder>{

    private List<Item> items;

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
    }

    @Override
    public int getItemCount(){
        return items.size();
    }
}
