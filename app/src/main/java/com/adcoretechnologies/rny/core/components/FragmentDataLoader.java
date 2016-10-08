package com.adcoretechnologies.rny.core.components;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDataLoader extends BaseFragment {


    @BindView(R.id.pbStatus)
    ProgressBar pbStatus;
    @BindView(R.id.ivStatus)
    ImageView ivStatus;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.llStatus)
    LinearLayout llStatus;

    public FragmentDataLoader() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_loader, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        setDataLoading(null);
    }

    public void setDataLoading(@Nullable String message) {
        if (message == null)
            message = "Please wait...";
        tvStatus.setText(message);
        tvStatus.setVisibility(View.VISIBLE);
        ivStatus.setVisibility(View.GONE);
        pbStatus.setVisibility(View.VISIBLE);
        llStatus.setVisibility(View.VISIBLE);
    }

    public void setDataEmpty(@Nullable String message) {
        if (message == null)
            message = "No items to show";
        tvStatus.setText(message);
        pbStatus.setVisibility(View.GONE);
        ivStatus.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);
        llStatus.setVisibility(View.VISIBLE);
    }

    public void setDataAvailable() {
        llStatus.setVisibility(View.GONE);
    }

    @Override
    public void log(String message) {

    }
}
