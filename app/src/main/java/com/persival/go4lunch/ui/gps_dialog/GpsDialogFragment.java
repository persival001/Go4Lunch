package com.persival.go4lunch.ui.gps_dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.persival.go4lunch.R;

public class GpsDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_gps, null);
        builder.setView(view);

        view.findViewById(R.id.dialog_gps_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.dialog_gps_activate).setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            dismiss();
        });

        return builder.create();
    }

}
