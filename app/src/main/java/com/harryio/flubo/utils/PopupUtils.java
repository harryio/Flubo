package com.harryio.flubo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.harryio.flubo.R;

public class PopupUtils {
    private static int selectedRepeatInterval;

    public static void showRepeatIntervalDialog(Context context, int checkedItem, final RepeatIntervalSelectedListener listener) {
        final String[] repeatIntervals = context.getResources().getStringArray(R.array.repeat_intervals);
        selectedRepeatInterval = checkedItem;
        new AlertDialog.Builder(context, R.style.RepeatIntervalDialog)
                .setSingleChoiceItems(repeatIntervals, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedRepeatInterval = which;
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onRepeatIntervalSelected(selectedRepeatInterval, repeatIntervals[selectedRepeatInterval]);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setTitle("Frequency")
                .create()
                .show();

    }

    public interface RepeatIntervalSelectedListener {
        void onRepeatIntervalSelected(int which, String intervalString);
    }

    public static void showConfirmDeleteDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.dialog_delete_option, onClickListener)
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
