package com.adcoretechnologies.rny.home.buyer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.util.Common;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan on 06/10/16.
 */

public class AdapterRentInfo implements GoogleMap.InfoWindowAdapter {
    private final HomeBuyerActivity parent;
    @BindView(R.id.ivPropertyImage)
    ImageView ivPropertyImage;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPropertyName)
    TextView tvPropertyName;
    @BindView(R.id.tvAvailableFrom)
    TextView tvAvailableFrom;
    @BindView(R.id.ivDealType)
    ImageView ivDealType;
    @BindView(R.id.tvForWhom)
    TextView tvForWhom;
    @BindView(R.id.tvFloorNumber)
    TextView tvFloorNumber;
    @BindView(R.id.tvHouseType)
    TextView tvHouseType;
    @BindView(R.id.tvHouseCondition)
    TextView tvHouseCondition;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvCall)
    TextView tvCall;
    private View mWindow;

    AdapterRentInfo(HomeBuyerActivity activity) {
        this.parent = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String title = marker.getTitle();
        if (title.equals("You are here")) {
            return null;
        }
        mWindow = parent.getLayoutInflater().inflate(R.layout.info_window_rent, null);
        render(mWindow, marker.getTitle());
        return mWindow;
    }

    private void render(View mWindow, String propertyId) {
        ButterKnife.bind(this, mWindow);
        BoProperty item = parent.getPropertyById(propertyId);
        if (item != null) {
            Common.showBigImage(parent, ivPropertyImage, item.heroImageUrl);
            tvPropertyName.setText(item.getOwnerName());
            tvAvailableFrom.setText("{fou-clock} " + item.getAvailability());
            tvFloorNumber.setText("{ion-ios-paw} " + item.getFloorNumber());
            tvForWhom.setText("{gmd-store} " + item.getForWhom());
            tvHouseCondition.setText("{ion-ios-analytics} " + item.getRoomCondition());
            tvHouseType.setText("{ion-social-buffer} " + item.getRoomType());
            tvPrice.setText(item.getRent());
            LatLng currentLocation = parent.getCurrentLocation();
            if (currentLocation != null) {
                if (item.getLatitude() > 0.0 && item.getLongitude() > 0.0) {
                    String distance = Common.getDistanceBetweenPoints(currentLocation, new LatLng(item.getLatitude(), item.getLongitude()));
                    tvDistance.setText("{ion-android-navigate} " + distance + "");
                } else {
                    parent.toast("Location detail for this property is not available");
                }
            } else {
                parent.toast("Your current location is not available");
            }

            if (item.isRented()) {
                ivDealType.setImageResource(R.drawable.sv_for_rent);
            } else {
                ivDealType.setImageResource(R.drawable.sv_for_sale);
            }
        } else {
            parent.toast("Unable to fetch property detail");
        }
    }
}
