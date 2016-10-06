package com.adcoretechnologies.rny.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.home.buyer.HomeBuyerActivity;
import com.adcoretechnologies.rny.home.seller.HomeSellerActivity;
import com.adcoretechnologies.rny.util.Const;
import com.adcoretechnologies.rny.util.Pref;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Irfan on 29/09/16.
 */

public class RoleChooserActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSeller)
    public void forSeller() {
        Pref.WriteBoolean(getApplicationContext(), Const.PREF_IS_SELLER, true);
        Intent intent = new Intent(this, HomeSellerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnBuyer)
    public void forBuyer() {
        Pref.WriteBoolean(getApplicationContext(), Const.PREF_IS_SELLER, false);
        Intent intent = new Intent(this, HomeBuyerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void init() {

    }

    @Override
    public void log(String message) {

    }
}
