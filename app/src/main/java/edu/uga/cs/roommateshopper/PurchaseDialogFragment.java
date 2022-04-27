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

public class PurchaseDialogFragment extends DialogFragment {

    private EditText editText;

    public interface PurchaseDialogListener {
        void onFinishPurchaseDialog(double price);
    }


    public PurchaseDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_purchase_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootPurchase));
        editText = (EditText) layout.findViewById(R.id.priceEditText) ;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setTitle("New Item for List");
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("PURCHASE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if(editText.getText().toString() != null){
                    double price = Double.parseDouble(editText.getText().toString());
                    PurchaseDialogListener listener = (PurchaseDialogListener) getActivity();
                    listener.onFinishPurchaseDialog(price);
                }
                else{

                }
                // close the dialog
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}