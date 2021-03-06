package com.adcoretechnologies.rny.wishlist;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.core.base.BaseFragment;
import com.adcoretechnologies.rny.core.components.FragmentDataLoader;
import com.adcoretechnologies.rny.home.buyer.HomeBuyerActivity;
import com.adcoretechnologies.rny.property.bo.BoPropertyMy;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentWishlist extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<BoPropertyMy> allItems;

    FragmentDataLoader fragmentLoader;
    private AdapterWishlist adapter;
    private DatabaseReference propertyRef;
    private HomeBuyerActivity parent;

    public FragmentWishlist() {
        // Required empty public constructor
    }

    public static FragmentWishlist newInstance() {
        return new FragmentWishlist();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property, null);

        ButterKnife.bind(this, view);
        fragmentLoader = (FragmentDataLoader) getChildFragmentManager().findFragmentById(R.id.fragmentLoader);
        if (fragmentLoader == null)
            toast("null");
        else
            init();
        return view;
    }

    @Override
    public void init() {
        parent = (HomeBuyerActivity) getActivity();
        allItems = new ArrayList<>();
        adapter = new AdapterWishlist(allItems);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0)
                    parent.setFabVisibility(false);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    parent.setFabVisibility(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        propertyRef = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_DB_USER_WISHLIST).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fillData();
    }

    private void fillData() {
        fragmentLoader.setDataLoading(null);
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
                toast("Failed to read wishlist property");
                Common.logException(getContext(), "Failed to read wishlist property detail", error);
            }
        });
    }

    private void bindData(ArrayList<BoPropertyMy> allItems) {
        adapter.notifyDataSetChanged();
        updateViews(allItems.size());
    }

    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("You don't have any property in your wishlist");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void log(String message) {

    }
}
