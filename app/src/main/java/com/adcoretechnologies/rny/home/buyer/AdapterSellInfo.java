package com.adcoretechnologies.rny.home.buyer;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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

public class AdapterSellInfo implements GoogleMap.InfoWindowAdapter {
    private final HomeBuyerActivity parent;
    @BindView(R.id.ivPropertyImage)
    ImageView ivPropertyImage;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPropertyName)
    TextView tvPropertyName;
    @BindView(R.id.tvLocality)
    TextView tvLocality;
    @BindView(R.id.ivDealType)
    ImageView ivDealType;
    @BindView(R.id.tvArea)
    TextView tvArea;
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

    AdapterSellInfo(HomeBuyerActivity activity) {
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
        mWindow = parent.getLayoutInflater().inflate(R.layout.info_window_sell, null);
        render(mWindow, marker.getTitle());
        return mWindow;
    }

    private void render(View mWindow, String propertyId) {
        ButterKnife.bind(this, mWindow);
        final BoProperty item = parent.getPropertyById(propertyId);
        if (item != null) {
            Common.showBigImage(parent, ivPropertyImage, item.heroImageUrl);
            tvPropertyName.setText(item.getOwnerName());
            tvArea.setText("{fou-clock} " + item.getArea());
            tvFloorNumber.setText("{ion-ios-paw} " + item.getFloorNumber());
            tvLocality.setText("{gmd-store} " + item.getLocality());
            tvHouseCondition.setText("{ion-ios-analytics} " + item.getHouseCondition());
            tvHouseType.setText("{ion-social-buffer} " + item.getHouseType());
            tvPrice.setText(item.getSellingPrice());
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
            if (!TextUtils.isEmpty(item.getOwnerContactNumber())) {
                tvCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + item.getOwnerContactNumber()));
                        parent.startActivity(intent);
                    }
                });
            }
        } else {
            parent.toast("Unable to fetch property detail");
        }
    }
}
