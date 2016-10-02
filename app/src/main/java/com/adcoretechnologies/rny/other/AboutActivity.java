package com.adcoretechnologies.rny.other;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.util.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tvAbout)
    TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        ButterKnife.bind(this);

        init();
    }

    @OnClick(R.id.btnContact)
    public void onContact() {
        Common.sendSupportMail(this);
    }

    @Override
    public void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvAbout.setText(Html.fromHtml(getString(R.string.about_us)));
    }

    @Override
    public void log(String message) {

    }
}
