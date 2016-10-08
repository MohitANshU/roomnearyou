package com.adcoretechnologies.rny.core.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.adcoretechnologies.rny.property.add.AddProperty;

import java.util.Calendar;

/**
 * Created by Irfan on 14/06/16.
 */
public class DialogDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final String KEY_WHICH_DATE = "whichDate";
    int whichDate = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.whichDate = getArguments().getInt(KEY_WHICH_DATE);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((AddProperty) getActivity()).setDate(whichDate, year + "/" + (month + 1) + "/" + day);
    }

    public static DialogFragment newInstance(int whichDate) {
        DialogDatePicker datePicker = new DialogDatePicker();
        Bundle args = new Bundle();
        args.putInt(KEY_WHICH_DATE, whichDate);
        datePicker.setArguments(args);
        return datePicker;
    }
}
