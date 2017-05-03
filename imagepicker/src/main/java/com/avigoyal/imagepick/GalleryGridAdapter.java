package com.avigoyal.imagepick;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.avigoyal.imagepick.model.GalleryImage;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter class which use to show image list for the device images and provide selection among them
 */
@SuppressWarnings("unused")
class GalleryGridAdapter extends RecyclerView.Adapter<GalleryGridAdapter.GalleryHolder> {

    private ArrayList<GalleryImage> mData;
    private ImagePickerActivity mActivity;
    private OnRecyclerItemClickListener mListener;

    GalleryGridAdapter(ImagePickerActivity activity, ArrayList<GalleryImage> data
            , OnRecyclerItemClickListener listener) {
        mActivity = activity;
        mData = data;
        mListener = listener;
    }

    GalleryGridAdapter(ImagePickerActivity activity, ArrayList<GalleryImage> data) {
        mActivity = activity;
        mData = data;
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View parentView = LayoutInflater.from(mActivity).inflate(R.layout.grid_item_thumbnail, parent, false);

        return new GalleryHolder(parentView, mListener);
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {
        GalleryImage image = getItem(position);
        boolean isSelected = mActivity.containsImage(image);


        ((FrameLayout) holder.itemView).setForeground(isSelected ?
                ContextCompat.getDrawable(mActivity, R.drawable.gallery_photo_selected) : null);


        File file;
        holder.mThumbnail.setImageResource(R.drawable.img_placeholder);
        if (null != image && null != image.mUri && !TextUtils.isEmpty(image.mUri.getPath())
                && (file = new File(image.mUri.getPath())).exists()) {
            Glide.with(mActivity)
                    .load(file) // Uri of the picture
                    .override(80, 80)
                    .centerCrop()
                    .into(holder.mThumbnail);
        } else {
            holder.mThumbnail.setImageResource(R.drawable.img_placeholder);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    private GalleryImage getItem(int position) {
        return mData.get(position);
    }

    static class GalleryHolder extends RecyclerView.ViewHolder {
        final ImageView mThumbnail;
        final OnRecyclerItemClickListener mListener;

        GalleryHolder(final View itemView, OnRecyclerItemClickListener listener) {
            super(itemView);
            mListener = listener;
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(itemView, getAdapterPosition());
                    }
                }
            });
        }

    }
}
