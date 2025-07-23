package com.arastudent.tid0093.mazegame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class WinDialog extends DialogFragment {
    public static WinDialog newInstance(int moves) {
        WinDialog fragment = new WinDialog();
        Bundle args = new Bundle();
        args.putInt("moves", moves);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int moves = getArguments().getInt("moves", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Congratulations!")
                .setMessage("You have completed the level in " + moves + " moves. What do you want to do next?")
                .setPositiveButton("Next Level (non-functional)", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle restart logic here or notify the activity
                        // For example:
                        ((MainActivity) getActivity()).resetGame();
                    }
                });
        return builder.create();
    }
}