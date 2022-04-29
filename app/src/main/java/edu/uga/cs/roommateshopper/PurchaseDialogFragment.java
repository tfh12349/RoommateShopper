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
 * This class is for the dialog fragment for purchasing the items from the cart
 */
public class PurchaseDialogFragment extends DialogFragment {

    private EditText editText;

    /**
     * This is the interface PurchaseDialogListener. When you want to make changes in an activity after
     * something occurs, have the activity implement this and override the method
     */
    public interface PurchaseDialogListener {
        void onFinishPurchaseDialog(double price);
    }

    public PurchaseDialogFragment() {
        // Required empty public constructor
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
        final View layout = inflater.inflate(R.layout.fragment_purchase_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootPurchase));

        // Set up the edit text
        editText = (EditText) layout.findViewById(R.id.priceEditText) ;

        // If there is a saved instance state
        if(onSavedInstanceState != null){
            // Only save the editText text if it really was saved
            if(onSavedInstanceState.getInt("price") != -1){ editText.setText("" + onSavedInstanceState.getInt("price"));}
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

        // The PURCHASE button handler
        builder.setPositiveButton("PURCHASE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // If the price is marked
                if(!editText.getText().toString().equals("")){
                    // Get the price, get the listener for the activity, and call the overloaded method
                    double price = Double.parseDouble(editText.getText().toString());
                    PurchaseDialogListener listener = (PurchaseDialogListener) getActivity();
                    listener.onFinishPurchaseDialog(price);
                }
                // Else, print an error message
                else{
                    Toast.makeText(getContext(), "No Price", Toast.LENGTH_SHORT).show();
                }
                // close the dialog
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    /**
     * This method saves the instance state of the fragment
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        // If there is a price, add it, else mark it
        if(!editText.getText().toString().equals("")){ outState.putInt("price", Integer.parseInt(editText.getText().toString()));}
        else{outState.putInt("price", -1);}
    }
}