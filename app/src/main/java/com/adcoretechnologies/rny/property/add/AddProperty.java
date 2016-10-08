package com.adcoretechnologies.rny.property.add;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.core.components.FragmentImageUpload;
import com.adcoretechnologies.rny.other.BOEventData;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddProperty extends BaseActivity implements FragmentImageUpload.ImageUploadListener {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    private static final int DATE_VACANT = 10;
    @BindView(R.id.etOwnwerName)
    TextInputEditText etOwnwerName;
    @BindView(R.id.etContactNumber)
    TextInputEditText etContactNumber;
    @BindView(R.id.tvLocality)
    TextView tvLocality;
    @BindView(R.id.rbRent)
    RadioButton rbRent;
    @BindView(R.id.rbSell)
    RadioButton rbSell;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.activity_add_property)
    LinearLayout activityAddProperty;
    @BindView(R.id.rgPropertyType)
    RadioGroup rgPropertyType;

    private double latitude;
    private double longitude;
    private FragmentImageUpload fragmentImageUploader;
    private FragmentRent fragmentRent;
    private FragmentSell fragmentSell;
    private String ownerName;
    private String contactNumber;
    private String locality;

    public double getLatitude() {
        return latitude;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getLocality() {
        return locality;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<String> getAllUploadedImage() {
        return allUploadedImage;
    }

    public boolean isUploading() {
        return fragmentImageUploader.isUploading();
    }

    private ArrayList<String> allUploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {
        if (Const.IS_TEST) {
            etOwnwerName.setText("Owner " + Common.getRandomInteger(100, 200));
            etContactNumber.setText("8080781242");
            tvLocality.setText("Hudko, HDFC Bank, Delhi");
            latitude = 23.23432432;
            longitude = 123.23423423;
        }

        fragmentImageUploader = (FragmentImageUpload) getSupportFragmentManager().findFragmentById(R.id.fragmentImageUpload);
        fragmentImageUploader.setVisibility(false);
    }

    public void onPropertySelection(View view) {
        ownerName = etOwnwerName.getText().toString();
        contactNumber = etContactNumber.getText().toString();
        locality = tvLocality.getText().toString();

        if (TextUtils.isEmpty(ownerName)) {
            etOwnwerName.setError("Please provide input");
            rgPropertyType.clearCheck();
            return;
        }
        if (TextUtils.isEmpty(contactNumber)) {
            etContactNumber.setError("Please provide input");
            rgPropertyType.clearCheck();
            return;
        } else if (contactNumber.length() != 10) {
            etContactNumber.setError("Contact number must be of 10 digit");
            rgPropertyType.clearCheck();
            return;
        }
        if (TextUtils.isEmpty(locality)) {
            toast("Please select locality of property");
            rgPropertyType.clearCheck();
            return;
        }

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rbRent:
                if (checked)
                    onRentSelection();
                break;
            case R.id.rbSell:
                if (checked)
                    onSellSelection();
                break;
        }
        btnSave.setVisibility(View.VISIBLE);
        fragmentImageUploader.setVisibility(true);
    }

    public void onRentSelection() {
        fragmentRent = FragmentRent.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentRent).commit();
    }

    public void onSellSelection() {
        fragmentSell = FragmentSell.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentSell).commit();
    }

    @OnClick(R.id.btnSave)
    public void onSave() {
        if (rbRent.isChecked())
            fragmentRent.onSave();
        else if (rbSell.isChecked()) {
            fragmentSell.onSave();
        }
    }

    @OnClick(R.id.tvLocality)
    public void openPlacePicker() {
        toast("Please wait...");
        PlacePicker.IntentBuilder picker = new PlacePicker.IntentBuilder();
        try {
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(new LatLng(28.535517, 77.391029));
//            LatLngBounds bounds = builder.build();
//            picker.setLatLngBounds(bounds);
            startActivityForResult(picker.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            toast("You need to update google play service in order to use this feature");
            updateStatus("You need to update google play service in order to use this feature");
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            toast("This feature requires Google play service to work.");
            updateStatus("This feature requires Google play service to work.");
            e.printStackTrace();
        }
    }

    public void updateStatus(String message) {
        toast(message);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    fragmentImageUploader.addNewUri(selectedImage);
                }
                break;
            case PLACE_PICKER_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(imageReturnedIntent, this);
                    updatePlace(place);
                } else
                    updateStatus("You have not selected any place");
            }
        }
    }

    public void updatePlace(Place place) {
        if (place != null) {
            tvLocality.setText(place.getAddress());
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

        }
    }



    public void onEventMainThread(BOEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {
            case BOEventData.EVENT_PROPERTY_SAVED: {
                toast("Property saved successfully");
                finish();
                break;
            }
        }
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

    @Override
    public void log(String message) {


    }

    @Override
    public void onImageUploadComplete(ArrayList<String> allUploadedUri) {
        this.allUploadedImage = allUploadedUri;
    }

    @Override
    public void onImageUploadFailed() {

    }

    public void setDate(int whichDate, String date) {
        if (fragmentRent != null && whichDate == DATE_VACANT) {
            fragmentRent.setDate(date);
        }
    }
}
