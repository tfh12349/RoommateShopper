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

/**
 * This is the dialog fragment used to view the item, and make any changes
 */
public class ItemViewDialogFragment extends DialogFragment {

    // Set up the EditText elements for updating, and the position int
    private EditText updateName, updateCount, updateDetails;
    private int position;

    // Set up the strings and ints needed
    private String name, details, key;
    private int count;


    /**
     * This is the interface ItemViewDialogListener. When you want to make changes in an activity after
     * something occurs, have the activity implement this and override the method
     */
    public interface ItemViewDialogListener {
        void onFinishItemViewDialog(int position, Item item, int action, String key);
    }

    // Just a general Constructor
    public ItemViewDialogFragment(){
        super();
    }

    /**
     * The newInstance method takes in all needed elements, adds them to the arguments, and returns a
     * new DialogFragment
     * @param position
     * @param name
     * @param count
     * @param details
     * @param key
     * @return
     */
    public static ItemViewDialogFragment newInstance(int position, String name, int count, String details, String key) {
        // Create the new dialog
        ItemViewDialogFragment dialog = new ItemViewDialogFragment();

        // Set up the arguments and add all inputs to the arguments
        Bundle args = new Bundle();
        args.putInt( "position", position );
        args.putString("name", name);
        args.putInt("count", count);
        args.putString("details", details);
        args.putString("key", key);

        // Set the arguments and return the dialog
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * onCreateDialog gets the needed elements, sets up the view, and builds the alert dialog
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Set the position, name, etc. to the connected arguments
        position = getArguments().getInt( "position" );
        name = getArguments().getString( "name" );
        count = getArguments().getInt ("count" );
        details = getArguments().getString( "details" );
        key = getArguments().getString("key");

        // If there is a saved instance state
        if(savedInstanceState != null){
            // Set the position, name, etc. to the connected saved arguments
            position = savedInstanceState.getInt( "position" );
            name = savedInstanceState.getString( "name" );
            count = savedInstanceState.getInt ("count" );
            details = savedInstanceState.getString( "details" );
            key = savedInstanceState.getString("key");
        }

        // Set up the layoutInflater and layout elements
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_item_view_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootItem));

        // Set the editTexts to their connected layout elements
        updateName = layout.findViewById( R.id.updateNameDialog );
        updateCount= layout.findViewById( R.id.updateCountDialog );
        updateDetails = layout.findViewById( R.id.updateDetailsDialog );

        // Pre-fill the edit texts with the current values for this item
        updateName.setText( name );
        updateCount.setText(""+count );
        updateDetails.setText( details );

        // Start building the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "View of Item Selected" );

        // The DELETE button handler
        builder.setNegativeButton( "DELETE", new ItemViewDialogFragment.DeleteButtonClickListener());

        // The SAVE button handler
        builder.setPositiveButton( "SAVE", new ItemViewDialogFragment.SaveButtonClickListener() );

        // The CART button handler
        builder.setNeutralButton( "CART", new ItemViewDialogFragment.CartButtonClickListener() );

        // Create the AlertDialog and show it
        return builder.create();
    }

    /**public String getDetails() {
        return details;
    }**/

    /**
     * This is the ClickListener class for the save button. It begins the process of saving the item
     */
    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //String originalDetails = getDetails();
            // If the input is not illegitimate
            if((!updateName.getText().toString().equals(""))
                    && (!updateCount.getText().toString().equals(""))
                    && (!updateDetails.getText().toString().equals(""))) {
                // Set the elements to the results of the EditTexts
                String name = updateName.getText().toString();
                int count = Integer.parseInt(updateCount.getText().toString());
                String details = updateDetails.getText().toString();
                // Create this new item
                Item item = new Item(name, count, details);

                // get the Activity's listener to add the new item
                ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

                // add the new item, using 1 as the action
                listener.onFinishItemViewDialog(position, item, 1, key);

                // close the dialog
                dismiss();
            }
            // Else, print an error
            else{
                Toast.makeText( getContext(), "One of the necessary elements is blank",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This is the ClickListener class for the delete button. It begins the process of deleting the item
     */
    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //String originalDetails = getDetails();

            // Set the elements to the results of the EditTexts
            String name = updateName.getText().toString();
            int count = Integer.parseInt(updateCount.getText().toString());
            String details = updateDetails.getText().toString();
            // Create the item
            Item item = new Item( name, count, details );

            //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


            // get the Activity's listener to delete the item
            ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

            // delete the item, with action 2
            listener.onFinishItemViewDialog( position, item, 2 , key);

            // close the dialog
            dismiss();
        }
    }

    /**
     * This is the ClickListener class for the cart button. It begins the process of putting the
     * item in the cart
     */
    private class CartButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //String originalDetails = getDetails();

            // Set the elements to the results of the EditTexts
            String name = updateName.getText().toString();
            int count = Integer.parseInt(updateCount.getText().toString());
            String details = updateDetails.getText().toString();
            // Create the item
            Item item = new Item( name, count, details );
            //String userEmail;
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

            // get the Activity's listener to put the item in the cart
            ItemViewDialogFragment.ItemViewDialogListener listener = (ItemViewDialogFragment.ItemViewDialogListener) getActivity();

            // add the item to cart with action 3
            listener.onFinishItemViewDialog( position, item, 3 , key);

            // close the dialog
            dismiss();
        }
    }

    /**
     * This method saves the instance state of the fragment
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // Put the position and name in
        outState.putInt( "position", position );
        outState.putString("name", updateName.getText().toString());
        // If the updateCount is not empty, add it. Else, send the old count
        if(!updateCount.getText().toString().equals("")){ outState.putInt("count", Integer.parseInt(updateCount.getText().toString()));}
        else{outState.putInt("count", count);}
        // Add the details and key
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