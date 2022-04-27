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


public class RemoveItemDialogFragment extends DialogFragment {

    private TextView text;
    private int position;
    private String name,details, key;
    private int count;

    public interface RemoveItemDialogListener {
        void onFinishRemoveItemDialog(int position, Item item, String key );
    }

    public RemoveItemDialogFragment(){
        super();
    }

    public static RemoveItemDialogFragment newInstance(int position, String name, int count, String details, String key) {
        RemoveItemDialogFragment dialog = new RemoveItemDialogFragment();

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
    public Dialog onCreateDialog(Bundle onSavedInstanceState){
        position = getArguments().getInt( "position" );
        name = getArguments().getString( "name" );
        count = getArguments().getInt ("count" );
        details = getArguments().getString( "details" );
        key = getArguments().getString("key");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_remove_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootRemove));
        text = layout.findViewById(R.id.askDelete);
        text.setText("Are you sure you want to delete" + name + "?");

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
        builder.setPositiveButton("DELETE", new RemoveItemDialogFragment.PositiveButtonClickListener());

        return builder.create();
    }

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            Item item = new Item(name, count, details);
            RemoveItemDialogFragment.RemoveItemDialogListener listener = (RemoveItemDialogFragment.RemoveItemDialogListener) getActivity();
            listener.onFinishRemoveItemDialog(position, item, key);
            dismiss();
        }
    }
}