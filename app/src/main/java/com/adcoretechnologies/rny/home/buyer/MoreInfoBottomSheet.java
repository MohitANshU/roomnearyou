package com.adcoretechnologies.rny.home.buyer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.other.BOEventData;
import com.adcoretechnologies.rny.util.Const;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Irfan on 08/10/16.
 */

public class MoreInfoBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    private BoProperty item;

    public static MoreInfoBottomSheet newInstance(BoProperty item) {
        MoreInfoBottomSheet bottomSheet = new MoreInfoBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(Const.ARG_PROPERTY, item);
        bottomSheet.setArguments(args);
        return bottomSheet;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        Bundle args = getArguments();
        if (args != null) {
            item = (BoProperty) args.getSerializable(Const.ARG_PROPERTY);
        }
        View contentView = View.inflate(getContext(), R.layout.fragment_more_info_bottom_sheet, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @OnClick(R.id.llCall)
    public void onCall() {
        EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_INFO_CLICK_CALL, 0, item.getOwnerContactNumber()));
    }

    @OnClick(R.id.llDirection)
    public void onDirection() {
        EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_INFO_CLICK_DIRECTION, 0, null, item));
    }

    @OnClick(R.id.llWishlist)
    public void onAddToWishlist() {
        EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_INFO_CLICK_WISHLIST,0,item.getPropertyId(),item));
    }
}
