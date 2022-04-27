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

public class AddItemDialogFragment extends DialogFragment {

    private EditText nameView, countView, detailsView;

    public interface AddItemDialogListener {
        void onFinishNewItemDialog(Item item);
    }

    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.root));
        nameView = layout.findViewById(R.id.nameOfItem);
        countView = layout.findViewById(R.id.count);
        detailsView = layout.findViewById(R.id.details);

        if(onSavedInstanceState != null){
            nameView.setText(onSavedInstanceState.getString("name"));
            if(onSavedInstanceState.getInt("count") != -1){ countView.setText("" + onSavedInstanceState.getInt("count"));}
            detailsView.setText(onSavedInstanceState.getString("details"));
        }

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
        builder.setPositiveButton(android.R.string.ok, new PositiveButtonClickListener());

        return builder.create();
    }

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            if((!nameView.getText().toString().equals(""))
                    && (!countView.getText().toString().equals(""))
                    && (!detailsView.getText().toString().equals(""))) {
                String name = nameView.getText().toString();
                int count = Integer.parseInt(countView.getText().toString());
                String details = detailsView.getText().toString();
                Item item = new Item(name, count, details);
                AddItemDialogListener listener = (AddItemDialogListener) getActivity();
                listener.onFinishNewItemDialog(item);
                dismiss();
            }
            else{
                Toast.makeText( getContext(), "One of the necessary elements is blank",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("name", nameView.getText().toString());
        if(!countView.getText().toString().equals("")){ outState.putInt("count", Integer.parseInt(countView.getText().toString()));}
        else{outState.putInt("count", -1);}
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