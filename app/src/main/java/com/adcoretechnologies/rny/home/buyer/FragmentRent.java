package com.adcoretechnologies.rny.home.buyer;

import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Irfan on 06/10/16.
 */

public class FragmentRent extends FragmentDashboard {

    private DatabaseReference propertyRef;

    public static FragmentRent newInstance() {
        return new FragmentRent();
    }

    @Override
    public void init() {
        super.init();
        propertyRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_POST_RENT);
        fillData();
    }

    @Override
    public void fillData() {
        toast("Fetching all property available for rent");
        propertyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.removeAll(allItems);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    allItems.add(snapshot.getValue(BoProperty.class));
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
}
