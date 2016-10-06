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

public class FragmentSell extends FragmentDashboard {

    private DatabaseReference propertyRef;

    public static FragmentSell newInstance() {
        return new FragmentSell();
    }

    @Override
    public void init() {
        super.init();
        propertyRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_POST_SELL);
        fillData();
    }

    @Override
    public void fillData() {
        toast("Fetching all property available for sell");
        propertyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.removeAll(allItems);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BoProperty item = snapshot.getValue(BoProperty.class);
                    item.propertyId = snapshot.getKey();
                    allItems.add(item);
                }
                bindData(allItems, EpropertyType.SELL);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                toast("Failed to read property detail");
                Common.logException(getContext(), "Failed to read property detail", error);
            }
        });
    }
}
