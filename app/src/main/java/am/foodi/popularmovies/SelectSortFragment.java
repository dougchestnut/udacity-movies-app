package am.foodi.popularmovies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class SelectSortFragment extends DialogFragment {

    public interface SelectSortListener {
        public void onDialogSortSelect(int sort_index);
    }

    SelectSortListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SelectSortListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SelectSortListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int sort_index = getArguments().getInt("sort_index");
        builder.setTitle(R.string.select_sort)
                .setSingleChoiceItems(R.array.sort_array, sort_index, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        mListener.onDialogSortSelect(which);
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
