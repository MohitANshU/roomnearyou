package com.adcoretechnologies.rny.home.buyer;

import android.content.Intent;
import android.net.Uri;
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
import com.adcoretechnologies.rny.util.Common;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class HomeBuyerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int BOTTOM_SHEET_PEEK_HEIGHT = 600;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvEmail;
    private FragmentDashboard dashboard;
    private MoreInfoBottomSheet bottomSheetDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_buyer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        MenuItem menu = navigationView.getMenu().getItem(0);
        menu.setChecked(true);
        onNavigationItemSelected(menu);
    }

    @Override
    public void init() {

    }

    @Override
    public void log(String message) {

    }

    public BoProperty getPropertyById(String propertyId) {
        if (dashboard != null) {
            return dashboard.getProperty(propertyId);
        }
        return null;
    }

    public LatLng getCurrentLocation() {
        if (dashboard != null) {
            return dashboard.getCurrentLocation();
        }
        return null;
    }

    public void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
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
        getMenuInflater().inflate(R.menu.home_buyer, menu);
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
        drawerLayout.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.nav_rent) {
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showProperty(EpropertyType.RENT);
        } else if (id == R.id.nav_purchase) {
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            showProperty(EpropertyType.BUY);
        } else if (id == R.id.nav_home) {
            showProperty(EpropertyType.All);
        } else if (id == R.id.nav_wishlist) {
            showWishlist();
        } else if (id == R.id.nav_logout) {
            performLogout();
            return true;
        } else if (id == R.id.nav_share) {
            Common.shareApp(this);
            return true;
        }
        return true;
    }

    private void showProperty(EpropertyType propertyType) {
        if (propertyType == EpropertyType.All) {

        } else if (propertyType == EpropertyType.RENT) {
            dashboard = FragmentRent.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, dashboard).commit();
        } else if (propertyType == EpropertyType.BUY) {
            dashboard = FragmentSell.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, dashboard).commit();
        }

    }

    private void showWishlist() {

    }

    public void onEventMainThread(BOEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {

            case BOEventData.EVENT_INFO_CLICK: {
                bottomSheetDialogFragment = MoreInfoBottomSheet.newInstance((BoProperty) object);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            }
            case BOEventData.EVENT_INFO_CLICK_CALL: {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data));
                startActivity(intent);
                bottomSheetDialogFragment.dismiss();
                break;
            }
            case BOEventData.EVENT_INFO_CLICK_DIRECTION: {
                BoProperty item= (BoProperty) object;
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", item.getLatitude(),item.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                bottomSheetDialogFragment.dismiss();
                break;
            }
            case BOEventData.EVENT_INFO_CLICK_WISHLIST: {
                bottomSheetDialogFragment.dismiss();
                addToWishlist(FirebaseAuth.getInstance().getCurrentUser(),data);
                break;
            }
        }
    }

    private void addToWishlist(FirebaseUser currentUser, String propertyId) {

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
