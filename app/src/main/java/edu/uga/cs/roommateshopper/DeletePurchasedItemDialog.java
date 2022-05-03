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
 * A simple {@link Fragment} subclass.
 * Use the {@link DeletePurchasedItemDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeletePurchasedItemDialog extends DialogFragment {

    private TextView text;
    private int pos;
    private String name, details, key;
    private int count;

    public DeletePurchasedItemDialog() {
        // Required empty public constructor
    }

    public interface DeletePurchasedItemDialogListener {
        void onDeletePurchasedItemDialogue(int pos, Item item, String key);
    }

    public static DeletePurchasedItemDialog newInstance(int pos, String name, int count, String details, String key) {
        DeletePurchasedItemDialog dialog = new DeletePurchasedItemDialog();

        Bundle args = new Bundle();
        args.putInt("position", pos);
        args.putString("name", name);
        args.putInt("count", count);
        args.putString("details", details);
        args.putString("key", key);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState) {
        pos = getArguments().getInt("position");
        name = getArguments().getString("name");
        count = getArguments().getInt ("count");
        details = getArguments().getString("details");
        key = getArguments().getString("key");

        if(onSavedInstanceState != null){
            // Set the position, name, etc. to the connected saved arguments
            pos = onSavedInstanceState.getInt("position");
            name = onSavedInstanceState.getString("name");
            count = onSavedInstanceState.getInt ("count");
            details = onSavedInstanceState.getString("details");
            key = onSavedInstanceState.getString("key");
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_delete_purchased_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootDeleteItem));

        text = layout.findViewById(R.id.askDelete);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        builder.setTitle("Delete?");

        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("DELETE", new DeletePurchasedItemDialog.DeleteButtonClickListener());

        return builder.create();
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which){
            Item item = new Item(name, count, details);
            DeletePurchasedItemDialog.DeletePurchasedItemDialogListener listener =
                    (DeletePurchasedItemDialog.DeletePurchasedItemDialogListener) getActivity();

            listener.onDeletePurchasedItemDialogue(pos, item, key);

            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt("position", pos);
        outState.putString("name", name);
        outState.putInt("count", count);
        outState.putString("details", details);
        outState.putString("key", key);
    }
}