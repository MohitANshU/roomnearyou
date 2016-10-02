package com.adcoretechnologies.rny.property.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.property.bo.BoPropertyMy;
import com.adcoretechnologies.rny.util.Common;
import com.adcoretechnologies.rny.util.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPropertyActivity extends
        RecyclerView.Adapter<AdapterPropertyActivity.ViewHolder> {
    Context context;

    private List<BoPropertyMy> allItems;

    public AdapterPropertyActivity(List<BoPropertyMy> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_property_my,
                parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BoPropertyMy property = getItem(position);
        if (property.isRented()) {
            holder.tvPropertyName.setText(property.ownerName);
            holder.tvPostedOn.append(Common.convertLongToTimestamp(property.postedOnLong));
            holder.tvOccupiedOn.setText(property.locality);
            holder.ivDealType.setImageResource(R.drawable.sv_for_rent);
            Common.showBigImage(context, holder.ivPropertyIcon, property.heroImageUrl);
        } else {
            holder.tvPropertyName.setText(property.ownerName);
            holder.tvPostedOn.append(Common.convertLongToTimestamp(property.postedOnLong));
            holder.tvOccupiedOn.setText(property.locality);
            holder.ivDealType.setImageResource(R.drawable.sv_for_sale);
            Common.showBigImage(context, holder.ivPropertyIcon, property.heroImageUrl);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EventBus.getDefault().Property(new BOEventData(BOEventData.EVENT_Property_DETAIL, position, Property.PropertyId));
            }
        });
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoPropertyMy getItem(int position) {
        return allItems.get(position);
    }

    private void log(String message) {
        Log.d(Const.DEBUG_TAG, getClass().getSimpleName() + " :" + message);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.ivPropertyIcon)
        ImageView ivPropertyIcon;
        @BindView(R.id.tvPropertyName)
        TextView tvPropertyName;
        @BindView(R.id.tvPostedOn)
        TextView tvPostedOn;
        @BindView(R.id.tvOccupiedOn)
        TextView tvOccupiedOn;
        @BindView(R.id.ivDealType)
        ImageView ivDealType;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
