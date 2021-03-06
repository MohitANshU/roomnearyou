package com.adcoretechnologies.rny.home.buyer;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseFragment;
import com.adcoretechnologies.rny.other.BOEventData;
import com.adcoretechnologies.rny.util.Common;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public abstract class FragmentDashboard extends BaseFragment implements OnMapReadyCallback {


    @BindView(R.id.llParent)
    LinearLayout llParent;
    private View view;
    private HomeBuyerActivity parent;
    private GoogleMap mMap;
    private DatabaseReference propertyRef;
    public ArrayList<BoProperty> allItems;
    private HashMap<String, BoProperty> allProperty;
    private boolean isMapReady;

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    private LatLng currentLocation;

    public FragmentDashboard() {
        // Required empty public constructor
    }

    public BoProperty getProperty(String propertyId) {
        return allProperty.get(propertyId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, null);

        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        isMapReady = false;
        parent = (HomeBuyerActivity) getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        allItems = new ArrayList<>();
        allProperty = new HashMap<>();
    }

    public abstract void fillData();

    public void bindData(ArrayList<BoProperty> allItems, EpropertyType propertyType) {
        try {
            if (isMapReady && mMap != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (allItems != null && allItems.size() > 0) {
                    for (int i = 0; i < allItems.size(); i++) {

                        BoProperty item = allItems.get(i);
                        LatLng locProperty = new LatLng(item.latitude, item.longitude);
                        final MarkerOptions mark = new MarkerOptions()
                                .position(locProperty)
                                .title(item.propertyId)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green));
//                        mark.snippet("Available in " + Common.convertLongToTimestamp(item.vacantDateLong).toString());
                        mMap.addMarker(mark);
                        if (propertyType == EpropertyType.RENT)
                            mMap.setInfoWindowAdapter(new AdapterRentInfo(parent));
                        else if (propertyType == EpropertyType.SELL)
                            mMap.setInfoWindowAdapter(new AdapterSellInfo(parent));
                        else {
                            //  Do nothing
                        }
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                handleInfoClick(marker);
                            }
                        });
                        builder.include(locProperty);
                        allProperty.put(item.propertyId, item);
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                    mMap.animateCamera(cu);
                } else updateStatus("Seems you have not added any property yet");
            } else {
                updateStatus("Map is not rendered properly. Can't show data");
            }
        } catch (Exception ex) {
            Common.logException(getContext(), "Error on creating marker", ex, null);
        }
    }

    private void handleInfoClick(Marker marker) {
        String title = marker.getTitle();
        BoProperty item = allProperty.get(title);
        if (item != null) {
            EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_INFO_CLICK, 0, title, item));
        }
    }

    private void updateStatus(String message) {
//        Snackbar.make(llParent, message, Snackbar.LENGTH_LONG).show();
        toast(message);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        isMapReady = true;

        SmartLocation.with(getContext()).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        addMarker(location, "You are here");
                    }
                });

    }

    private void addMarker(Location location, String title) {
        LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(marker).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }


    @Override
    public void log(String message) {
        super.log(getClass().getSimpleName(), message);
    }

}
