package android.avigoyal.imagepicker.Sample;

import android.avigoyal.imagepicker.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.avigoyal.imagepick.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageSampleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SINGLE_CHOICE = 10001;
    private static final int MULTI_CHOICE = 10002;
    private ImageView mImageView;
    private ArrayList<String> mArrayData = new ArrayList<>();
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);
        Button pickSingleImage = (Button) findViewById(R.id.btn_sample_activity_pick_image);
        mImageView = (ImageView) findViewById(R.id.imv_picked_image_single);
        Button pickMultiImage = (Button) findViewById(R.id.btn_sample_activity_pick_multi_image);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcv_sample_image_collection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mAdapter = new ImageAdapter(this, mArrayData);
        recyclerView.setAdapter(mAdapter);
        pickSingleImage.setOnClickListener(this);
        pickMultiImage.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.TAG_IMAGE_URI);
        Uri[] uris = new Uri[parcelableUris.length];
        System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
        if (requestCode == MULTI_CHOICE) {
            mArrayData.clear();
            for (Uri uri : uris) {
                File file = new File(uri.getPath());
                if (!file.exists()) {
                    return;
                }
                mArrayData.add("file:" + file.getAbsolutePath());
                mAdapter.notifyDataSetChanged();
            }

        } else if (requestCode == SINGLE_CHOICE) {
            for (Uri uri : uris) {
                File file = new File(uri.getPath());
                if (!file.exists()) {
                    return;
                }
                String path = "file:" + file.getAbsolutePath();
                if (!TextUtils.isEmpty(path)) {
                    Picasso.with(this)
                            .load(path)
                            .into(mImageView);
                }
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sample_activity_pick_image:
                Intent pickerChooser = new Intent(ImagePickerActivity.ACTION_INTENT);
                pickerChooser.putExtra(ImagePickerActivity.ACTION_MODE, ImagePickerActivity.PICK_SINGLE_IMAGE);
                startActivityForResult(pickerChooser, SINGLE_CHOICE);
                break;
            case R.id.btn_sample_activity_pick_multi_image:
                pickerChooser = new Intent(ImagePickerActivity.ACTION_INTENT);
                startActivityForResult(pickerChooser, MULTI_CHOICE);
                break;
        }
    }
}
