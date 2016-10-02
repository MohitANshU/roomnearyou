package com.adcoretechnologies.rny.core.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.adcoretechnologies.rny.R;

/**
 * Created by Vishwa android on 11-Jun-16.
 */
public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Alert);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //  This trick is used to hide title bar in api level < 21
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    ProgressDialog dialog;

    public ProgressDialog getProgressDialog(@Nullable String title, @Nullable String description) {
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(description);
        dialog.show();
        return dialog;
    }

    public void hideDialog() {
        if (dialog != null)
            dialog.dismiss();
    }


    public void toast(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {

        }
    }
}
