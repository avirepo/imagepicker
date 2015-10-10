package com.avigoyal.imagepick;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.avigoyal.imagepick.model.GalleryImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class GalleryGridAdapter extends RecyclerView.Adapter<GalleryGridAdapter.GalleryHolder> {

    private ArrayList<GalleryImage> mData;
    private ImagePickerActivity mActivity;
    protected OnRecyclerItemClickListener mListener;

    public GalleryGridAdapter(ImagePickerActivity activity, ArrayList<GalleryImage> data) {
        mActivity = activity;
        mData = data;
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View parentView = LayoutInflater.from(mActivity).inflate(R.layout.grid_item_thumbnail, parent, false);

        return new GalleryHolder(parentView);
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {
        GalleryImage image = getItem(position);
        boolean isSelected = mActivity.containsImage(image);

        ((FrameLayout) holder.itemView).setForeground(isSelected ?
                mActivity.getResources().getDrawable(R.drawable.gallery_photo_selected) : null);


        if (null != image && null != image.mUri && !TextUtils.isEmpty(image.mUri.getPath())) {
            File file = new File(image.mUri.getPath());
            String path = "file:" + file.getAbsolutePath();
            if (!TextUtils.isEmpty(path)) {
                Picasso.with(mActivity)
                        .load(path)
                        .resize(250, 250)
                        .centerCrop()
                        .into(holder.mThumbnail);
            }
        } else {
            holder.mThumbnail.setImageResource(R.drawable.img_placeholder);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public GalleryImage getItem(int position) {
        return mData.get(position);
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {
        final ImageView mThumbnail;

        public GalleryHolder(final View itemView) {
            super(itemView);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(itemView, getPosition());
                    }
                }
            });
        }
    }

    public void setListener(OnRecyclerItemClickListener listener) {
        mListener = listener;
    }
}
