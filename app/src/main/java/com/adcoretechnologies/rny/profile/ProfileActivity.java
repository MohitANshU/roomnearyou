package com.adcoretechnologies.rny.profile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.auth.BoUser;
import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {


    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    Toolbar toolbar;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.llProfile)
    LinearLayout llProfile;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etContactNumber)
    TextInputEditText etContactNumber;
    @BindView(R.id.tvLocality)
    TextView tvLocality;
    private DatabaseReference userRef;
    private double latitude;
    private double longitude;
    private boolean isUploading;
    public Uri profilePicUrl;
    private Uri selectedImage;
    private BoUser userProfile;
    private boolean isProfilePicChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public void init() {
        fillData();
    }

    private void fillData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            etName.setText(user.getDisplayName());
            etEmail.setText(user.getEmail());
            profilePicUrl = user.getPhotoUrl();
            if (profilePicUrl != null)
                Common.showRoundImage(this, ivProfilePic, profilePicUrl.toString());
            fetchDetailProfile(user.getUid());
        } else {
            toast("You are not logged in");
            return;
        }
    }

    private void fetchDetailProfile(String userId) {
        userRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_USER).child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BoUser user = dataSnapshot.getValue(BoUser.class);
                bindData(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                toast("Failed to read property detail");
                Common.logException(getApplicationContext(), "Failed to read user detail", error);
            }
        });
    }

    private void bindData(BoUser user) {
        if (user != null) {
            this.userProfile = user;
            tvLocality.setText(user.locality);
            etContactNumber.setText(user.contactNumber);
            etContactNumber.setEnabled(false);
            latitude = user.getLatitude();
            longitude = user.getLongitude();
        }
    }

    @OnClick(R.id.tvLocality)
    public void showPlacePicker() {
        openPlacePicker();
    }

    public void openPlacePicker() {
        toast("Please wait...");
        PlacePicker.IntentBuilder picker = new PlacePicker.IntentBuilder();
        try {
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

    private void updateStatus(String message) {
        toast(message);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(imageReturnedIntent, this);
                    updatePlace(place);
                } else
                    updateStatus("You have not selected any place");
            }
            case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    isProfilePicChanged = true;
                    ivProfilePic.setImageURI(selectedImage);
                }
                break;
        }
    }

    public void updatePlace(Place place) {
        if (place != null) {
            tvLocality.setText(place.getAddress());
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

        }
    }


    @OnClick(R.id.fabSave)
    public void onSave() {
        String name = etName.getText().toString();
        String locality = tvLocality.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Please provide input");
            return;
        }
        if (TextUtils.isEmpty(locality)) {
            toast("Please select locality");
            return;
        }

        if (isUploading) {
            toast("Please wait for uploading to be done");
        } else {
            if (isProfilePicChanged)
                uploadImageToStorage(selectedImage.toString(), name, locality, latitude, longitude);
            else {
                updateProfile(name, locality, latitude, longitude, profilePicUrl);
            }
        }
    }

    @OnClick({R.id.tvProfilePic, R.id.ivProfilePic})
    public void onProfilePicUpdate() {
        showImagePicker();
    }


    public void showImagePicker() {
        if (isUploading) {
            toast("Please wait for uploading to be done.");
        } else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);//one can be replaced with any action code
        }
    }

    private void uploadImageToStorage(final String mediaFile, final String name, final String locality, final double latitude, final double longitude) {
        showProgressDialog("Please wait...", "Updating your profile image");

        final String file = getRealPathFromURI(mediaFile);
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);
        StorageReference imageRef = storageRef.child("profile/profile_" + System.currentTimeMillis() + ".jpg");
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(file));


            UploadTask uploadTask = imageRef.putStream(stream);

            isUploading = true;
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    isUploading = false;
                    hideDialog();
                    Common.logException(getApplicationContext(), "Image uploading failed", exception, null);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    isUploading = false;
                    hideDialog();
                    Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                    log("Upload complete for URI : " + file);
                    updateProfile(name, locality, latitude, longitude, uploadedUrl);

                }
            });
        } catch (FileNotFoundException e) {
            hideDialog();
            e.printStackTrace();
            log("File not found for upload: " + file);
            toast("Failed uploading profile image. Please try again");
        }
    }

    private void updateProfile(final String name, final String locality, final double latitude, final double longitude, Uri profilePicUrl) {
        showProgressDialog("Please wait...", "Updating your profile");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(profilePicUrl)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userProfile.setName(name);
                            userProfile.setLocality(locality);
                            userProfile.setLatitude(latitude);
                            userProfile.setLongitude(longitude);

                            Map<String, Object> preferenceValues = userProfile.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/" + Const.FIREBASE_DB_USER + "/" + user.getUid(), preferenceValues);
                            FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    hideDialog();
                                    if (databaseError == null) {
                                        toast("Profile details updated successfully");
                                        finish();
                                    } else {
                                        Common.logException(getApplicationContext(), "There was an error while performing update", databaseError);
                                        toast("There was an error while performing update");
                                    }
                                }
                            });
                        }
                    }
                });


    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getApplicationContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    @Override
    public void log(String message) {

    }
}
