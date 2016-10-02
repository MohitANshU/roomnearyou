package com.adcoretechnologies.rny.core.components;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.core.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComponentItemSelector extends BaseFragment {


    @BindView(R.id.tvItem)
    TextView tvItem;
    private BaseActivity activity;
    private String[] items;
    private String title;

    public String getSelectedItem() {
        return tvItem.getText().toString();
    }

    public ComponentItemSelector() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.component_item_selector, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {

    }

    public void initialize(String[] items, String title) {
        activity = (BaseActivity) getActivity();
        this.items = items;
        this.title = title;
    }

    @OnClick(R.id.tvItem)
    public void onClick() {
        if (items == null) {
            throw new RuntimeException("Please initialize component");
        }
        showItemSelection(activity, items, title);
    }

    public void showItemSelection(BaseActivity activity, String[] items, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);

        final int[] position = {0};
        //list of items
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position[0] = which;
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        which = position[0];
                        if (which > 0) {
                            updateData(which);
                        }
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void updateData(int position) {
        tvItem.setText(items[position]);
    }

    @Override
    public void log(String message) {

    }
}
