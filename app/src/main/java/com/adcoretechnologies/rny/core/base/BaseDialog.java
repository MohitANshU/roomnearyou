package com.adcoretechnologies.rny.core.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by Irfan on 19/09/15.
 */
public abstract class BaseDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //  Removing white background from dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        return dialog;
    }

    public void toast(String message) {
        try {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            log("Window has been closed");
        }
    }

    public abstract void initViews();

    //  Use this as default : Log.d(Const.DEBUG_TAG, getClass().getSimpleName() + " : " + message);
    public abstract void log(String message);
}
