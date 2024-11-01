package com.rp_grf.jrmadeiras.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.rp_grf.jrmadeiras.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.PickerTheme,
                (DatePickerDialog.OnDateSetListener) getActivity(), dia, mes, ano);

        datePickerDialog.updateDate(ano, mes, dia);
        datePickerDialog.setTitle("");

        return datePickerDialog;
    }
}
