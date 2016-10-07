package com.adcoretechnologies.rny;

import android.os.Bundle;

import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.home.buyer.MoreInfoBottomSheet;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    private MoreInfoBottomSheet bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

    }

    @Override
    public void init() {

    }

    @Override
    public void log(String message) {

    }

    boolean toggle = false;

    @OnClick(R.id.btnClick)
    public void onClick() {
        bottomSheetDialogFragment = new MoreInfoBottomSheet();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }
}
