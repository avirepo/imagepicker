package com.avigoyal.imagepick;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avigoyal.imagepick.Utils.DialogUtil;
import com.avigoyal.imagepick.model.GalleryImage;
import com.avigoyal.imagepick.ui.ActionBar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * <p>
 * Image picker activity allow user to pick image from gallery and camera
 * Use #ACTION_INTENT to launch activity from eny where the app.
 * You can set action mode single image or multi image using tag ACTION_MODE in bundle
 * Using MAX_IMAGE bundle argument you can set maximum limit for image pickeing
 * </p>
 */
public class ImagePickerActivity extends Activity implements ActionBar.ImagePickerActionBarClickListener,
        OnRecyclerItemClickListener {

    public static final String ACTION_INTENT = "com.sixthcontinent.citizen.PICK_IMAGE";
    public static final String TAG_IMAGE_URI = "TAG_IMAGE_URI";
    public static final String ACTION_MODE = "action_mode";
    public static final String MAX_IMAGE = "max_image";

    private static final int PICK_FROM_CAMERA = 100100;
    public static final int PICK_SINGLE_IMAGE = 1;
    public static final int PICK_MULTIPLE_IMAGE = 10001;
    private int mMaxImage = -1;

    private RecyclerView mGalleryGridView;
    private GalleryGridAdapter mAdapter;
    private ArrayList<GalleryImage> mGalleyImageCol = new ArrayList<>();

    private Set<GalleryImage> mSelectedImagesList;
    private LinearLayout mSelectedImagesContainer;
    private LinearLayout mFrameLayout;
    private TextView mSelectedImageEmptyMessage;
    private TextView mSelectedImages;
    private ActionBar mActionBar;
    private File mFile;
    private String mCurrentImagePath = null;
    private int mPicActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_picker);

        mSelectedImagesContainer = (LinearLayout) findViewById(R.id.selected_photos_container);
        mSelectedImages = (TextView) findViewById(R.id.txv_total_selected_image);
        mSelectedImageEmptyMessage = (TextView) findViewById(R.id.selected_photos_empty);
        mFrameLayout = (LinearLayout) findViewById(R.id.selected_photos_container_frame);

        mGalleryGridView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mAdapter = new GalleryGridAdapter(this, mGalleyImageCol);
        mGalleryGridView.setLayoutManager(manager);
        mGalleryGridView.setAdapter(mAdapter);
        mAdapter.setListener(this);

        Intent intent = getIntent();
        mPicActionMode = intent.getIntExtra(ACTION_MODE, PICK_MULTIPLE_IMAGE);
        mMaxImage = intent.getIntExtra(MAX_IMAGE, -1);
        setGalleryImageInGrid();

        setActionBar();
    }

    private void setGalleryImageInGrid() {
        mSelectedImagesList = new HashSet<>();

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.ORIENTATION};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        while (imageCursor.moveToNext()) {
            int columnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            if (columnIndex != -1 && columnIndex < imageCursor.getColumnCount()) {
                String stringUri = imageCursor.getString(columnIndex);
                if (!TextUtils.isEmpty(stringUri)) {
                    Uri uri = Uri.parse(stringUri);
                    int orientationIndex = imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
                    int orientation = ExifInterface.ORIENTATION_NORMAL;
                    if (orientationIndex != -1 && orientationIndex < imageCursor.getColumnCount()) {
                        orientation = imageCursor.getInt(orientationIndex);
                    }
                    mGalleyImageCol.add(new GalleryImage(uri, orientation));
                }
            }
        }
        imageCursor.close();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Set action bar for layout
     */
    private void setActionBar() {
        mActionBar = (ActionBar) findViewById(R.id.cab_multi_picker_custom_action_bar);
        mActionBar.setImagePickerActionBarClickListener(this);

        mActionBar.setBackButtonVisibility(View.VISIBLE);
        mActionBar.setDoneButtonVisibility(View.VISIBLE);

        setActionBarTextAndSelectedImageListVisibility();
    }

    /**
     *
     */
    private void setActionBarTextAndSelectedImageListVisibility() {
        mActionBar.setTitleVisibility(View.VISIBLE);
        mActionBar.setTitleText(getString(R.string.select_image));

        switch (mPicActionMode) {
            case PICK_SINGLE_IMAGE:
                mActionBar.setDoneButtonVisibility(View.GONE);
                mFrameLayout.setVisibility(View.GONE);
                break;
            case PICK_MULTIPLE_IMAGE:
                mFrameLayout.setVisibility(View.VISIBLE);
                break;
            default:
                if (mPicActionMode < 0) {
                    mFrameLayout.setVisibility(View.GONE);
                } else {
                    mActionBar.setDoneButtonVisibility(View.GONE);
                    mFrameLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * add selected image into the bottom layout
     *
     * @param image GalleryImage image description and uri
     * @return if image is added in the container return true else return false
     */
    public boolean addImageInContainer(GalleryImage image) {
        if (image != null && image.mUri != null && mSelectedImagesList.add(image)) {
            updateState();
            View rootView = LayoutInflater.from(ImagePickerActivity.this).inflate(R.layout.item_selected_thumbnail, null);
            ImageView thumbnail = (ImageView) rootView.findViewById(R.id.selected_photo);
            rootView.setTag(image.mUri);
            mFile = new File(image.mUri.getPath());
            String path = "file:" + mFile.getAbsolutePath();

            Picasso.with(this)
                    .load(path)
                    .fit()
                    .centerCrop()
                    .into(thumbnail);

            mSelectedImagesContainer.addView(rootView, 0);

            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                    getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(px, px));

            if (mSelectedImagesList.size() == 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
                mSelectedImageEmptyMessage.setVisibility(View.GONE);
            }
            return true;
        }
        return false;
    }


    private void updateState() {
        toggleCamera();
        updateImagesCount();
    }

    /**
     * remove image from container
     *
     * @param image GalleryImage image description and uri which have to be removed
     * @return if image is removed return true else false
     */
    public boolean removeImageFromContainer(GalleryImage image) {
        if (mSelectedImagesList.remove(image)) {
            updateState();
            for (int i = 0; i < mSelectedImagesContainer.getChildCount(); i++) {
                View childView = mSelectedImagesContainer.getChildAt(i);
                if (childView.getTag().equals(image.mUri)) {
                    mSelectedImagesContainer.removeViewAt(i);
                    break;
                }
            }

            if (mSelectedImagesList.size() == 0) {
                mSelectedImagesContainer.setVisibility(View.GONE);
                mSelectedImageEmptyMessage.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return false;
    }

    public boolean containsImage(GalleryImage image) {
        return mSelectedImagesList.contains(image);
    }


    @Override
    public void onImagePickerActionButtonClick(int clickButtonId) {
        switch (clickButtonId) {
            case ActionBar.ID_DONE_BUTTON:
                onDoneButtonClick();
                break;
            case ActionBar.ID_BACK_BUTTON:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case ActionBar.ID_CAMERA_BUTTON:
                if (isValidSelection()) {
                    try {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mFile = createImageFile();
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                        cameraIntent.putExtra("return-data", true);
                        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private boolean isValidSelection() {
        boolean isValid = mMaxImage == -1 || mMaxImage > mSelectedImagesList.size();
        if (!isValid) {
            String image = mMaxImage == 1 ? "image" : "images";
            String message = String.format(Locale.getDefault(), getString(R.string.max_image_allowed), String.valueOf(mMaxImage));
            message = message.replace("images", image);

            DialogUtil.showAlertDialog(this, "Alert"
                    , message
                    , null, getString(R.string.ok), true, null);
        }
        return isValid;
    }

    private void toggleCamera() {
        if (mMaxImage == -1 || mMaxImage > mSelectedImagesList.size()) {
            mActionBar.setCameraButtonVisibility(View.VISIBLE);
        } else {
            mActionBar.setCameraButtonVisibility(View.INVISIBLE);
        }
    }

    /**
     * pass selected image uri's to the called view
     */
    private void onDoneButtonClick() {
        Uri[] uris = new Uri[mSelectedImagesList.size()];
        int i = 0;
        for (GalleryImage img : mSelectedImagesList)
            uris[i++] = img.mUri;
        Intent intent = new Intent();
        intent.putExtra(TAG_IMAGE_URI, uris);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * when image is capture from camera then handle the response
     *
     * @param requestCode RequestCode of image
     * @param resultCode  ResultCode of intent
     * @param data        data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK && mFile != null) {
            Uri[] uris = new Uri[1];
            uris[0] = Uri.fromFile(mFile);
            GalleryImage image = new GalleryImage(uris[0], 0);
            mGalleyImageCol.add(0, image);
            mAdapter.notifyDataSetChanged();
            addImageInContainer(image);
        }
    }

    private void ImageClickAction(GalleryImage image) {
        switch (mPicActionMode) {
            case PICK_SINGLE_IMAGE:
                onDoneButtonClick();
                break;
            case PICK_MULTIPLE_IMAGE:
                break;
            default:
                if (mPicActionMode < 0) {
                    onDoneButtonClick();
                } else if (mPicActionMode > mSelectedImagesList.size()) {
                    removeImageFromContainer(image);
                    Toast.makeText(this, "You can not selected more than" + mSelectedImagesList.size() + "images", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * set selected image count on view
     */
    private void updateImagesCount() {
        String selection = getString(R.string.select_image);
        int size = mSelectedImagesList.size();
        if (size > 0) {
            selection = String.format(getString(R.string.selected_images), String.valueOf(size));
        }
        mSelectedImages.setText(selection);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        GalleryImage image = mGalleyImageCol.get(position);
        if (mSelectedImagesList.contains(image) || isValidSelection()) {
            if (!addImageInContainer(image)) {
                removeImageFromContainer(image);
            }
            ImageClickAction(image);
            mAdapter.notifyDataSetChanged();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
