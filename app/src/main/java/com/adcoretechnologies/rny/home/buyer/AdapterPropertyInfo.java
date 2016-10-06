package com.adcoretechnologies.rny.home.buyer;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.adcoretechnologies.rny.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Irfan on 06/10/16.
 */

public class AdapterPropertyInfo implements GoogleMap.InfoWindowAdapter {
    private final Activity parent;
    private final BoProperty item;
    private View mWindow;

    AdapterPropertyInfo(Activity activity, BoProperty item) {
        this.parent = activity;
        this.item = item;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        mWindow = parent.getLayoutInflater().inflate(R.layout.info_window_buyer, null);
        render(mWindow, marker.getTitle());
        return mWindow;
    }

    private void render(View mWindow, String title) {
        Toast.makeText(parent, "Title : " + item.ownerName, Toast.LENGTH_LONG).show();
    }
}
