package com.avigoyal.imagepick.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.avigoyal.imagepick.R;

public class ActionBar extends LinearLayout implements View.OnClickListener {

    public static final int ID_DONE_BUTTON = 1;
    public static final int ID_BACK_BUTTON = 2;
    public static final int ID_CAMERA_BUTTON = 3;

    private TextView mTitle;
    private ImageButton mDoneButton;
    private ImageButton mCameraButton;
    private ImageView mImvBack;

    ImagePickerActionBarClickListener mImagePickerActionBarClickListener;


    public void setDoneButtonVisibility(int rightButtonVisibility) {
        mDoneButton.setVisibility(rightButtonVisibility);
    }

    public void setDoneButtonImageRes(int res) {
        mDoneButton.setImageResource(res);
    }

    public void setCameraButtonVisibility(int rightButtonVisibility) {
        mCameraButton.setVisibility(rightButtonVisibility);
    }

    public void setCameraButtonImageRes(int res) {
        mCameraButton.setImageResource(res);
    }

    public void setBackButtonVisibility(int backButtonVisibility) {
        mImvBack.setVisibility(backButtonVisibility);
    }

    public void setBackButtonImageRes(int res) {
        mImvBack.setImageResource(res);
    }

    public void setTitleVisibility(int titleVisibility) {
        mTitle.setVisibility(titleVisibility);
    }

    public void setTitleText(String text) {
        mTitle.setText(text);
    }


    public ActionBar(Context context) {
        super(context);
        initView();
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_image_picker_action_bar, this, true);

        mTitle = (TextView) view.findViewById(R.id.txv_image_picker_action_bar_title);
        mDoneButton = (ImageButton) view.findViewById(R.id.btn_image_picker_action_bar_done);
        mCameraButton = (ImageButton) view.findViewById(R.id.btn_image_picker_action_bar_camera);
        mImvBack = (ImageView) view.findViewById(R.id.imv_image_picker_action_bar_back);

        mDoneButton.setOnClickListener(this);
        mImvBack.setOnClickListener(this);
        mCameraButton.setOnClickListener(this);
    }

    public void setImagePickerActionBarClickListener(ImagePickerActionBarClickListener imagePickerActionBarClickListener) {
        this.mImagePickerActionBarClickListener = imagePickerActionBarClickListener;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_image_picker_action_bar_camera) {
            if (mImagePickerActionBarClickListener != null) {
                mImagePickerActionBarClickListener.onImagePickerActionButtonClick(ID_CAMERA_BUTTON);
            }
        } else if (v.getId() == R.id.btn_image_picker_action_bar_done) {
            if (mImagePickerActionBarClickListener != null) {
                mImagePickerActionBarClickListener.onImagePickerActionButtonClick(ID_DONE_BUTTON);
            }
        } else if (v.getId() == R.id.imv_image_picker_action_bar_back) {
            if (mImagePickerActionBarClickListener != null) {
                mImagePickerActionBarClickListener.onImagePickerActionButtonClick(ID_BACK_BUTTON);
            }
        }
    }

    public interface ImagePickerActionBarClickListener {
        public void onImagePickerActionButtonClick(int clickButtonId);
    }
}

