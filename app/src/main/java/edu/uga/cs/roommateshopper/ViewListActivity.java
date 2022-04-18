package edu.uga.cs.roommateshopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewListActivity extends AppCompatActivity implements AddItemDialogFragment.AddItemDialogListener {

    private RecyclerView recyclerView;
    private FloatingActionButton floater;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddItemDialogFragment();
                showDialogFragment( newFragment );
            }
        });

        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );

        items = new ArrayList<Item>();

    }

    @Override
    public void onFinishNewItemDialog(Item item) {

    }

    void showDialogFragment( DialogFragment newFragment ) {
        newFragment.show( getSupportFragmentManager(), null);
    }
}