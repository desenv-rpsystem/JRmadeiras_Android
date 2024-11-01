package com.rp_grf.jrmadeiras.Utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.rp_grf.jrmadeiras.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                R.style.PickerTheme,
                (TimePickerDialog.OnTimeSetListener) getActivity(),
                hora, minuto, DateFormat.is24HourFormat(getActivity()));

        timePickerDialog.setTitle("");

        return timePickerDialog;
    }
}
