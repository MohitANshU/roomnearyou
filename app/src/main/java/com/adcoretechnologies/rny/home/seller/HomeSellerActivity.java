package com.adcoretechnologies.rny.home.seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.auth.login.LoginActivity;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.other.AboutActivity;
import com.adcoretechnologies.rny.other.BOEventData;
import com.adcoretechnologies.rny.profile.ProfileActivity;
import com.adcoretechnologies.rny.property.FragmentMyProperty;
import com.adcoretechnologies.rny.property.activity.FragmentPropertyActivity;
import com.adcoretechnologies.rny.property.add.AddProperty;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class HomeSellerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvEmail;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_seller);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ButterKnife.findById(headerView, R.id.llProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });
        ButterKnife.findById(headerView, R.id.ivProfilePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });
        tvName = ButterKnife.findById(headerView, R.id.tvName);
        tvEmail = ButterKnife.findById(headerView, R.id.tvEmail);

        init();
        MenuItem menu = navigationView.getMenu().getItem(0);
        menu.setChecked(true);
        onNavigationItemSelected(menu);
    }

    public void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void init() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //TODO Redirect to login
        } else {
            tvName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
        }

        if (Const.IS_TEST)
            showDashboard();
    }

    @Override
    public void log(String message) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.nav_add) {
            openAddProperty();
        } else if (id == R.id.nav_home) {
            showDashboard();
        } else if (id == R.id.nav_activity) {
            showPropertyActivity();
        } else if (id == R.id.nav_logout) {
            performLogout();
            return true;
        } else if (id == R.id.nav_property) {
            showMyProperty();
        } else if (id == R.id.nav_share) {
            Common.shareApp(this);
            return true;
        }
        return true;
    }

    private void showDashboard() {
        updateTitle("Dashboard");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentDashboard.newInstance())
                .commit();
    }

    private void showPropertyActivity() {
        updateTitle("Property Activity");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentPropertyActivity.newInstance())
                .commit();
    }

    private void updateTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @OnClick(R.id.fab)
    public void onAdd() {
        openAddProperty();
    }

    private void openAddProperty() {
        startActivity(new Intent(this, AddProperty.class));
    }

    private void showMyProperty() {
        updateTitle("My Property");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentMyProperty.newInstance())
                .commit();
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        toast("You have been logged out successfully");
        redirectToLogin();
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void showLogin() {
        Snackbar snackbar = Snackbar.make(toolbar, "You must login to use this feature", Snackbar.LENGTH_LONG);
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeSellerActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        snackbar.show();
    }

    public void onEventMainThread(BOEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {

            case BOEventData.EVENT_POST_DETAIL: {

                break;
            }
        }
    }

    public void setFabVisibility(boolean shouldVisibile) {
        if (shouldVisibile)
            fab.show();
        else fab.hide();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
