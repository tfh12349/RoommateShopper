package edu.uga.cs.roommateshopper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeletePurchaseDialogFragment extends DialogFragment {

    private final String TAG = "DeletePurchaseDialog";

    private TextView textView;
    private int pos;
    private String userName, key;
    private double price;
    private List<Item> items;

    public interface DeletePurchaseDialogListener {
        void onFinishDeletePurchaseDialog(int pos, Purchase purchase, String key);
    }

    public DeletePurchaseDialogFragment(List<Item> items) {
        super();
        this.items = items;
    }

    public static DeletePurchaseDialogFragment newInstance(int pos, List<Item> items, double price, String userName, String key) {
        DeletePurchaseDialogFragment fragment = new DeletePurchaseDialogFragment(items);

        Bundle args = new Bundle();
        args.putInt("position", pos);
        args.putDouble("price", price);
        args.putString("userName", userName);
        args.putString("key", key);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState) {
        if(onSavedInstanceState != null){
            // Set the position, name, etc. to the connected saved arguments
            pos = onSavedInstanceState.getInt( "position" );
            price = onSavedInstanceState.getDouble("price");
            userName = onSavedInstanceState.getString( "userName" );
            key = onSavedInstanceState.getString("key");
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_delete_purchase_dialog,
                (ViewGroup) getActivity().findViewById(R.id.rootDelete));

        textView = layout.findViewById(R.id.deleteAsk);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        builder.setTitle("Delete?");

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("DELETE", new DeletePurchaseDialogFragment.DeleteButtonClickListener());

        return builder.create();
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Purchase purchase = new Purchase(userName, price, items);

            DeletePurchaseDialogFragment.DeletePurchaseDialogListener listener =
                    (DeletePurchaseDialogFragment.DeletePurchaseDialogListener) getActivity();

            listener.onFinishDeletePurchaseDialog(pos, purchase, key);
            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", pos);
        outState.putString("userName", userName);
        outState.putDouble("price", price);
        outState.putString("key", key);
    }
}
