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
import com.adcoretechnologies.rny.property.bo.BoPostRent;
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

public class FragmentRent extends BaseFragment {


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
    @BindView(R.id.rbPg)
    RadioButton rbPg;
    @BindView(R.id.rbOther)
    RadioButton rbOther;
    @BindView(R.id.rbBoys)
    RadioButton rbBoys;
    @BindView(R.id.rbGirls)
    RadioButton rbGirls;
    @BindView(R.id.rbBoth)
    RadioButton rbBoth;
    @BindView(R.id.cbFamily)
    RadioButton cbFamily;
    @BindView(R.id.cbBachelor)
    RadioButton cbBachelor;
    @BindView(R.id.cbBoth)
    RadioButton cbBoth;
    @BindView(R.id.rbVacant)
    RadioButton rbVacant;
    @BindView(R.id.rbAboutToVacant)
    RadioButton rbAboutToVacant;
    @BindView(R.id.rbOccupied)
    RadioButton rbOccupied;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.cb2)
    CheckBox cb2;
    @BindView(R.id.cb3)
    CheckBox cb3;
    @BindView(R.id.cb4)
    CheckBox cb4;
    @BindView(R.id.cb5)
    CheckBox cb5;
    @BindView(R.id.etRent)
    EditText etRent;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.rgCondition)
    RadioGroup rgCondition;
    @BindView(R.id.rgRoomType)
    RadioGroup rgRoomType;
    @BindView(R.id.rgSuitableFor)
    RadioGroup rgSuitableFor;
    @BindView(R.id.rgWhom)
    RadioGroup rgWhom;
    @BindView(R.id.rgVacant)
    RadioGroup rgAvailability;
    private View view;
    private String vacantDate;
    private ComponentItemSelector componentFloorSelector;

    public FragmentRent() {
        // Required empty public constructor
    }

    public static FragmentRent newInstance() {
        return new FragmentRent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_property_rent, null);

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
            rbBoys.setChecked(true);
            cbBoth.setChecked(true);
            rbVacant.setChecked(true);
            vacantDate = Common.getTimestampString();
            etRent.setText("30000");
        }
    }

    @OnClick(R.id.btnSave)
    public void onSave() {
        String roomCondition = ((RadioButton) ButterKnife.findById(view, rgCondition.getCheckedRadioButtonId())).getText().toString();
        String roomType = ((RadioButton) ButterKnife.findById(view, rgRoomType.getCheckedRadioButtonId())).getText().toString();
        String suitableFor = ((RadioButton) ButterKnife.findById(view, rgSuitableFor.getCheckedRadioButtonId())).getText().toString();
        String forWhom = ((RadioButton) ButterKnife.findById(view, rgWhom.getCheckedRadioButtonId())).getText().toString();
        String availability = ((RadioButton) ButterKnife.findById(view, rgAvailability.getCheckedRadioButtonId())).getText().toString();
        String floorNumber = componentFloorSelector.getSelectedItem();
        String rent = etRent.getText().toString();

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
            performPropertySave(roomCondition, roomType, suitableFor, forWhom, availability, vacantDate, floorNumber, rent, ownerName, contactNumber, locality, latitude, longitude, allUploadedImageUrl);
        }

    }

    private void performPropertySave(String roomCondition, String roomType, String suitableFor, String forWhom, String availability, String vacantDate, String floorNumber, String rent, String ownerName, String contactNumber, String locality, double latitude, double longitude, ArrayList<String> allUploadedImageUrl) {
        showProgressDialog("Saving property", "Please wait..");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(Const.FIREBASE_DB_POST_RENT).push().getKey();
        BoPostRent post = new BoPostRent(ownerName, user.getEmail(), contactNumber, locality, latitude, longitude, "Delhi", Common.getTimestamp(), allUploadedImageUrl, 1, roomCondition, roomType, suitableFor, forWhom, availability, vacantDate, floorNumber, rent);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Const.FIREBASE_DB_POST_RENT + "/" + key, postValues);
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
