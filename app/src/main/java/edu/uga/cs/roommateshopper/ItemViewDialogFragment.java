package edu.uga.cs.roommateshopper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ItemViewDialogFragment extends DialogFragment {

    private EditText updateName, updateCount, updateDetails;
    private int position;

    private String name, details, key;
    private int count;



    public interface ItemViewDialogListener {
        void onFinishItemViewDialog(int position, Item item, int action, String key);
    }

    public ItemViewDialogFragment(){
        super();
    }

    public static ItemViewDialogFragment newInstance(int position, String name, int count, String details, String key) {
        ItemViewDialogFragment dialog = new ItemViewDialogFragment();

        // Supply job lead values as an argument.
        Bundle args = new Bundle();
        args.putInt( "position", position );
        args.putString("name", name);
        args.putInt("count", count);
        args.putString("details", details);
        args.putString("key", key);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        position = getArguments().getInt( "position" );
        name = getArguments().getString( "name" );
        count = getArguments().getInt ("count" );
        details = getArguments().getString( "details" );
        key = getArguments().getString("key");

        if(savedInstanceState != null){
            position = savedInstanceState.getInt( "position" );
            name = savedInstanceState.getString( "name" );
            count = savedInstanceState.getInt ("count" );
            details = savedInstanceState.getString( "details" );
            key = savedInstanceState.getString("key");
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_item_view_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootItem));

        updateName = layout.findViewById( R.id.updateNameDialog );
        updateCount= layout.findViewById( R.id.updateCountDialog );
        updateDetails = layout.findViewById( R.id.updateDetailsDialog );

        // Pre-fill the edit texts with the current values for this job lead.
        // The user will be able to modify them.
        updateName.setText( name );
        updateCount.setText(""+count );
        updateDetails.setText( details );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "View of Item Selected" );

        // The Cancel button handler
        builder.setNegativeButton( "DELETE", new ItemViewDialogFragment.DeleteButtonClickListener());

        // The Save button handler
        builder.setPositiveButton( "SAVE", new ItemViewDialogFragment.SaveButtonClickListener() );

        // The Delete button handler
        builder.setNeutralButton( "CART", new ItemViewDialogFragment.CartButtonClickListener() );

        // Create the AlertDialog and show it
        return builder.create();
    }

    public String getDetails() {
        return details;
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //String originalDetails = getDetails();
            if((!updateName.getText().toString().equals(""))
                    && (!updateCount.getText().toString().equals(""))
                    && (!updateDetails.getText().toString().equals(""))) {
                String name = updateName.getText().toString();
                int count = Integer.parseInt(updateCount.getText().toString());
                String details = updateDetails.getText().toString();
                Item item = new Item(name, count, details);

                // get the Activity's listener to add the new job lead
                ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

                // add the new job lead
                listener.onFinishItemViewDialog(position, item, 1, key);

                // close the dialog
                dismiss();
            }
            else{
                Toast.makeText( getContext(), "One of the necessary elements is blank",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //String originalDetails = getDetails();
            String name = updateName.getText().toString();
            int count = Integer.parseInt(updateCount.getText().toString());
            String details = updateDetails.getText().toString();
            Item item = new Item( name, count, details );

            //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


            // get the Activity's listener to add the new job lead
            ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

            // add the new job lead
            listener.onFinishItemViewDialog( position, item, 2 , key);

            // close the dialog
            dismiss();
        }
    }

    private class CartButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //String originalDetails = getDetails();
            String name = updateName.getText().toString();
            int count = Integer.parseInt(updateCount.getText().toString());
            String details = updateDetails.getText().toString();
            Item item = new Item( name, count, details );
            String userEmail;
            //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            //Query query = ref.child("shoppingList").orderByChild("details").equalTo(item.getDetails());

            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //if (user != null) {
            //    userEmail = user.getEmail();
            //} else {
                // get the Activity's listener to add the new job lead
            //    ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

                // add the new job lead
            //    listener.onFinishItemViewDialog( position, item, 4 );
            //    dismiss();
            //}

            //query.addListenerForSingleValueEvent(new ValueEventListener() {
            //    @Override
            //    public void onDataChange(DataSnapshot dataSnapshot) {
            //        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
             //           snapshot.getRef().removeValue();
             //       }
            //    }
//
            //    @Override
            //    public void onCancelled(DatabaseError databaseError) {
            //    }
            //});

            // get the Activity's listener to add the new job lead
            ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

            // add the new job lead
            listener.onFinishItemViewDialog( position, item, 3 , key);

            // close the dialog
            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt( "position", position );
        outState.putString("name", updateName.getText().toString());
        if(!updateCount.getText().toString().equals("")){ outState.putInt("count", Integer.parseInt(updateCount.getText().toString()));}
        else{outState.putInt("count", count);}
        outState.putString("details", updateDetails.getText().toString());
        outState.putString("key", key);
    }

    /**@Override
    public void onDestroyView(){
        Dialog dialog = getDialog();
        if(dialog != null && dialog.isShowing()){
            dismiss();
        }
        super.onDestroyView();
    }**/
}