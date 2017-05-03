package android.avigoyal.imagepicker;

import android.avigoyal.imagepicker.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<String> mData;

    public ImageAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(mContext).inflate(R.layout.image_view, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageHolder viewHolder = (ImageHolder) holder;
        if (!TextUtils.isEmpty(mData.get(position))) {
            Picasso.with(mContext)
                    .load(mData.get(position))
                    .into(viewHolder.mImageView);
        }
        viewHolder.mImageView.setImageResource(R.drawable.img_placeholder);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;

        public ImageHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imv_picked_image);
        }
    }
}
