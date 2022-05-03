package edu.uga.cs.roommateshopper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.List;

public class UpdatePriceDialogFragment extends DialogFragment {

    private EditText updatePrice;
    private int pos;

    private double price;
    private String key;

    public interface UpdatePriceDialogListener {
        void onFinishUpdatePriceDialog(int pos, Purchase purchase, int action, String key);
    }

    public UpdatePriceDialogFragment() {
        super();
    }

    public static UpdatePriceDialogFragment newInstance(int pos, String userName, double price, List<Item> items, String key) {
        UpdatePriceDialogFragment dialogFragment = new UpdatePriceDialogFragment();

        Bundle args = new Bundle();
        args.putInt("position", pos);
        args.putDouble("price", price);
        args.putString("key", key);


        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState != null){
            // Set the position, name, etc. to the connected saved arguments
            pos = savedInstanceState.getInt("position");
            price = savedInstanceState.getDouble("price");
            key = savedInstanceState.getString("key");
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_update_price,
                (ViewGroup) getActivity().findViewById(R.id.rootUpdate));

        updatePrice = layout.findViewById(R.id.updatePrice);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        builder.setTitle("Update Price");

        builder.setPositiveButton("UPDATE", new UpdatePriceDialogFragment.UpdateButtonClickListener());

        builder.setNegativeButton("CANCEL", new UpdatePriceDialogFragment.CancelButtonClickListener());

        return builder.create();
    }

    private class UpdateButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            /*if (!updatePrice.getText().toString().equals("") &&
            !updatePrice.getText().toString().equals(".")) {
                double price = Double.parseDouble(updatePrice.getText().toString());
                Purchase purchase = new Purchase(null, price, null);

                UpdatePriceDialogFragment.UpdatePriceDialogListener listener =
                        (UpdatePriceDialogFragment.UpdatePriceDialogListener) getActivity();

                listener.onFinishUpdatePriceDialog(pos, purchase, 1, key);
            }*/
            dismiss();
        }
    }

    private class CancelButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", pos);
        outState.putString("price", updatePrice.getText().toString());
        outState.putString("key", key);
    }
}
