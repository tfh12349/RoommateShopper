package edu.uga.cs.roommateshopper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class is for the dialog fragment for removing the item
 */
public class RemoveItemDialogFragment extends DialogFragment {

    // Set up the textView, the position, the strings and the int values
    private TextView text;
    private int position;
    private String name,details, key;
    private int count;

    /**
     * This is the interface RemoveItemDialogListener. When you want to make changes in an activity after
     * something occurs, have the activity implement this and override the method
     */
    public interface RemoveItemDialogListener {
        void onFinishRemoveItemDialog(int position, Item item, String key);
    }

    // Just a general Constructor
    public RemoveItemDialogFragment(){
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
    public static RemoveItemDialogFragment newInstance(int position, String name, int count, String details, String key) {
        // Create the new dialog
        RemoveItemDialogFragment dialog = new RemoveItemDialogFragment();

        // Set up the arguments and add all inputs to the arguments
        Bundle args = new Bundle();
        args.putInt( "position", position );
        args.putString("name", name);
        args.putInt("count", count);
        args.putString("details", details);
        args.putString("key", key);

        // add the arguments to the dialog, then return it
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * onCreateDialog gets the needed elements, sets up the view, and builds the alert dialog
     * @param onSavedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState){

        // Set the position, name, etc. to the connected arguments
        position = getArguments().getInt( "position" );
        name = getArguments().getString( "name" );
        count = getArguments().getInt ("count" );
        details = getArguments().getString( "details" );
        key = getArguments().getString("key");

        // If there is a saved instance state
        if(onSavedInstanceState != null){
            // Set the position, name, etc. to the connected saved arguments
            position = onSavedInstanceState.getInt( "position" );
            name = onSavedInstanceState.getString( "name" );
            count = onSavedInstanceState.getInt ("count" );
            details = onSavedInstanceState.getString( "details" );
            key = onSavedInstanceState.getString("key");
        }

        // Set up the layoutInflater and layout elements
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_remove_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootRemove));

        // Set the textView and the text
        text = layout.findViewById(R.id.askDelete);
        text.setText("Are you sure you want to delete " + name + "?");

        // Start building the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle("Delete?");

        // The cancel button handler, which dismisses the dialog
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        // The DELETE button handler
        builder.setPositiveButton("DELETE", new RemoveItemDialogFragment.PositiveButtonClickListener());

        // Create the AlertDialog and show it
        return builder.create();
    }

    /**
     * This is the ClickListener class for the delete button. It begins the process of deleting the item
     */
    private class PositiveButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            // Create the item
            Item item = new Item(name, count, details);
            // get the Activity's listener to delete the item
            RemoveItemDialogFragment.RemoveItemDialogListener listener = (RemoveItemDialogFragment.RemoveItemDialogListener) getActivity();
            // call the activity's overloaded method
            listener.onFinishRemoveItemDialog(position, item, key);
            // dismiss
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

        // Add all the needed elements to the outState
        outState.putInt( "position", position );
        outState.putString("name", name);
        outState.putInt("count", count);
        outState.putString("details", details);
        outState.putString("key", key);
    }

    // Not necessary, but here nonetheless
    @Override
    public void onDestroyView(){
        Dialog dialog = getDialog();
        if(dialog != null && dialog.isShowing()){
            dismiss();
        }
        super.onDestroyView();
    }
}