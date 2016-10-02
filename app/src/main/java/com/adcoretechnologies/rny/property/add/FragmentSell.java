package com.adcoretechnologies.rny.property.add;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseFragment;
import com.adcoretechnologies.rny.core.components.ComponentItemSelector;
import com.adcoretechnologies.rny.other.BOEventData;
import com.adcoretechnologies.rny.property.bo.BoPropertySell;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class FragmentSell extends BaseFragment {


    @BindView(R.id.rbSelf)
    RadioButton rbSelf;
    @BindView(R.id.rbBuilder)
    RadioButton rbBuilder;
    @BindView(R.id.rbFlat)
    RadioButton rbFlat;
    @BindView(R.id.rbFurnished)
    RadioButton rbFurnished;
    @BindView(R.id.rbSemiFurnished)
    RadioButton rbSemiFurnished;
    @BindView(R.id.rbUnfurnished)
    RadioButton rbUnfurnished;
    @BindView(R.id.rb1Bhk)
    RadioButton rb1Bhk;
    @BindView(R.id.rb2Bhk)
    RadioButton rb2Bhk;
    @BindView(R.id.rb3Bhk)
    RadioButton rb3Bhk;
    @BindView(R.id.rbOther)
    RadioButton rbOther;
    @BindView(R.id.etArea)
    EditText etArea;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.etSellingPrice)
    EditText etSellingPrice;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.isNegotiable)
    CheckBox isNegotiable;
    View view;
    @BindView(R.id.rgPropertyType)
    RadioGroup rgPropertyType;
    @BindView(R.id.rgHouseCondition)
    RadioGroup rgHouseCondition;
    @BindView(R.id.rgHouseType)
    RadioGroup rgHouseType;
    private ComponentItemSelector componentFloorSelector;

    public FragmentSell() {
        // Required empty public constructor
    }

    public static FragmentSell newInstance() {
        return new FragmentSell();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_property_sell, null);

        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        componentFloorSelector = (ComponentItemSelector) getChildFragmentManager().findFragmentById(R.id.componentFloorSelector);
        if (componentFloorSelector != null) {
            String[] items = {"Basement", "Ground", "First", "Second", "Third", "Other"};
            componentFloorSelector.initialize(items, "Choose floor");
        }
        if (Const.IS_TEST) {
            rbFurnished.setChecked(true);
            rb1Bhk.setChecked(true);
            rbSelf.setChecked(true);
            etSellingPrice.setText("3000000");
            etArea.setText("1000");
            etDescription.setText("SOmething ");
        }
    }

    @OnClick(R.id.btnSave)
    public void onSave() {
        String roomCondition = ((RadioButton) ButterKnife.findById(view, rgHouseCondition.getCheckedRadioButtonId())).getText().toString();
        String roomType = ((RadioButton) ButterKnife.findById(view, rgHouseType.getCheckedRadioButtonId())).getText().toString();
        String propertyType = ((RadioButton) ButterKnife.findById(view, rgPropertyType.getCheckedRadioButtonId())).getText().toString();
        String floorNumber = componentFloorSelector.getSelectedItem();
        String sellingPrice = etSellingPrice.getText().toString();
        String area = etArea.getText().toString();
        String description = etDescription.getText().toString();

        AddProperty parent = (AddProperty) getActivity();
        String ownerName = parent.getOwnerName();
        String contactNumber = parent.getContactNumber();
        String locality = parent.getLocality();
        double latitude = parent.getLatitude();
        double longitude = parent.getLongitude();

        if (parent.isUploading()) {
            toast("Please wait for uploading to be finished");
            return;
        } else {
            ArrayList<String> allUploadedImageUrl = parent.getAllUploadedImage();
            performPropertySave(roomCondition, roomType, propertyType, area, floorNumber, sellingPrice, description, ownerName, contactNumber, locality, latitude, longitude, allUploadedImageUrl);
        }
    }

    private void performPropertySave(String houseCondition, String houseType, String propertyType, String area, String floorNumber, String sellingPrice, String description, String ownerName, String contactNumber, String locality, double latitude, double longitude, ArrayList<String> allUploadedImageUrl) {
        showProgressDialog("Saving property", "Please wait..");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(Const.FIREBASE_DB_POST_SELL).push().getKey();
        BoPropertySell post = new BoPropertySell(ownerName, user.getEmail(), contactNumber, locality, latitude, longitude, "Delhi", Common.getTimestamp(), allUploadedImageUrl, 1, houseCondition, houseType, propertyType, description, area, floorNumber, sellingPrice, isNegotiable.isChecked());
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Const.FIREBASE_DB_POST_SELL + "/" + key, postValues);
        childUpdates.put("/" + Const.FIREBASE_DB_USER_POSTS + "/" + user.getUid() + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideDialog();

                if (databaseError == null) {
                    redirectToNextScreen();
                } else {
                    toast("Error on saving property details. Please check all your input");
                    Common.logException(getContext(), "Error while saving property detail", databaseError);
                }
            }
        });
    }


    private void redirectToNextScreen() {
        EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_PROPERTY_SAVED, 0));
    }

    @Override
    public void log(String message) {
        super.log(getClass().getSimpleName(), message);
    }
}
