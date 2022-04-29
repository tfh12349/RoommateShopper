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
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the AddItemDialogFragment. It is used to add items to the shopping list
 */
public class AddItemDialogFragment extends DialogFragment {

    // Set up the EditTexts
    private EditText nameView, countView, detailsView;

    /**
     * This is the interface AddItemDialogListener. When you want to make changes in an activity after
     * something occurs, have the activity implement this and override the method
     */
    public interface AddItemDialogListener {
        void onFinishNewItemDialog(Item item);
    }

    /**
     * onCreateDialog gets the needed elements, sets up the view, and builds the alert dialog
     * @param onSavedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState){

        // Set up the layoutInflater and layout elements
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.root));

        // Set the editTexts to their connected layout elements
        nameView = layout.findViewById(R.id.nameOfItem);
        countView = layout.findViewById(R.id.count);
        detailsView = layout.findViewById(R.id.details);

        // If there is a saved instance state
        if(onSavedInstanceState != null){
            // Set the nameView text to the saved one
            nameView.setText(onSavedInstanceState.getString("name"));
            // Only save the countView text if it really was saved
            if(onSavedInstanceState.getInt("count") != -1){ countView.setText("" + onSavedInstanceState.getInt("count"));}
            // Save the detailsView
            detailsView.setText(onSavedInstanceState.getString("details"));
        }

        // Start building the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle("New Item for List");

        // The cancel button handler, which dismisses the dialog
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        // The ok button handler
        builder.setPositiveButton(android.R.string.ok, new PositiveButtonClickListener());

        // Create the AlertDialog
        return builder.create();
    }

    /**
     * This is the onClickListener for the ok button
     */
    private class PositiveButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            // If the input is valid
            if((!nameView.getText().toString().equals(""))
                    && (!countView.getText().toString().equals(""))
                    && (!detailsView.getText().toString().equals(""))) {
                // Create the elements, then create the item
                String name = nameView.getText().toString();
                int count = Integer.parseInt(countView.getText().toString());
                String details = detailsView.getText().toString();
                Item item = new Item(name, count, details);

                // Create the listener, then run the overloaded method
                AddItemDialogListener listener = (AddItemDialogListener) getActivity();
                listener.onFinishNewItemDialog(item);

                // dismiss the dialog
                dismiss();
            }
            else{
                Toast.makeText( getContext(), "One of the necessary elements is blank",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method saves the instance state of the fragment
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // Add the name
        outState.putString("name", nameView.getText().toString());
        // Add the count if it exists, else throw in -1 for future reference
        if(!countView.getText().toString().equals("")){ outState.putInt("count", Integer.parseInt(countView.getText().toString()));}
        else{outState.putInt("count", -1);}
        // Add details
        outState.putString("details", detailsView.getText().toString());
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