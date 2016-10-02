package com.adcoretechnologies.rny.core.components;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adcoretechnologies.rny.R;
import com.adcoretechnologies.rny.other.BOEventData;
import com.adcoretechnologies.rny.util.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class AdapterImageUploadPreview extends
        RecyclerView.Adapter<AdapterImageUploadPreview.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE_EMPTY = 1;
    private static final int VIEW_TYPE_IMAGE_UPLOAD = 0;
    Context context;

    private List<Uri> allItems;

    public AdapterImageUploadPreview(List<Uri> allItems) {
        this.allItems = allItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        context = parent.getContext();
        View v = null;

        if (viewType == VIEW_TYPE_IMAGE_EMPTY) {
            v = LayoutInflater.from(context).inflate(R.layout.item_posts_image,
                    parent, false);
        } else if (viewType == VIEW_TYPE_IMAGE_UPLOAD) {
            v = LayoutInflater.from(context).inflate(R.layout.item_posts_images_upload,
                    parent, false);
        }

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (getItemViewType(position) == VIEW_TYPE_IMAGE_EMPTY) {
            final Uri item = getItem(position);
            holder.imageView.setImageURI(item);
            holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_POST_IMAGE_UPLOAD_REMOVE, position));
                }
            });
        } else {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BOEventData(BOEventData.EVENT_POST_IMAGE_UPLOAD, 0));
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return allItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == allItems.size())
            return VIEW_TYPE_IMAGE_UPLOAD;
        else return VIEW_TYPE_IMAGE_EMPTY;
    }

    public Uri getItem(int position) {
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

        @BindView(R.id.ivImage)
        @Nullable
        ImageView imageView;

        @BindView(R.id.ivRemove)
        @Nullable
        ImageView ivRemove;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
