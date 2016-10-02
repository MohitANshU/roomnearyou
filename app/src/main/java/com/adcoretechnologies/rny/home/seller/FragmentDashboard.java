package com.adcoretechnologies.rny.home.seller;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseFragment;
import com.adcoretechnologies.rny.property.bo.BoPropertyMy;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class FragmentDashboard extends BaseFragment implements OnMapReadyCallback {


    @BindView(R.id.llParent)
    LinearLayout llParent;
    private View view;
    private HomeSellerActivity parent;
    private GoogleMap mMap;
    private DatabaseReference propertyRef;
    private ArrayList<BoPropertyMy> allItems;
    private boolean isMapReady;

    public FragmentDashboard() {
        // Required empty public constructor
    }

    public static FragmentDashboard newInstance() {
        return new FragmentDashboard();
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
        parent = (HomeSellerActivity) getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        allItems = new ArrayList<>();
        propertyRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_USER_POSTS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fillData();
    }

    private void fillData() {
        toast("Fetching your property details");
        propertyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.removeAll(allItems);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    allItems.add(snapshot.getValue(BoPropertyMy.class));
                }
                bindData(allItems);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                toast("Failed to read property detail");
                Common.logException(getContext(), "Failed to read property detail", error);
            }
        });
    }

    private void bindData(ArrayList<BoPropertyMy> allItems) {
        try {
            if (isMapReady && mMap != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (allItems != null && allItems.size() > 0) {
                    for (int i = 0; i < allItems.size(); i++) {

                        BoPropertyMy item = allItems.get(i);
                        LatLng locProperty = new LatLng(item.latitude, item.longitude);
                        final MarkerOptions mark = new MarkerOptions()
                                .position(locProperty)
                                .title(item.ownerName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green));
                        mark.snippet("Available in " + Common.convertLongToTimestamp(item.vacantDateLong).toString());
                        mMap.addMarker(mark);
                        builder.include(locProperty);
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
