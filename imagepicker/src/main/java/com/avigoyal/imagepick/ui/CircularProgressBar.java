package com.avigoyal.imagepick.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import com.avigoyal.imagepick.R;


public class CircularProgressBar extends ProgressBar {

    private static final String TAG = "CircularProgressBar";

    private static final int STROKE_WIDTH = 12;
    public static final int TITLE_TEXT_SIZE = 60;
    public static final int SUB_TITLE_TEXT_SIZE = 48;

    private String mTitle = "";
    private String mSubTitle = "";

    private int mStrokeWidth = STROKE_WIDTH;
    private float mTitleTextSize = TITLE_TEXT_SIZE;
    private float mSubTitleTextSize = SUB_TITLE_TEXT_SIZE;

    private final RectF mCircleBounds = new RectF();

    private final Paint mProgressColorPaint = new Paint();
    private final Paint mBackgroundColorPaint = new Paint();
    private final Paint mTitlePaint = new Paint();
    private final Paint mSubtitlePaint = new Paint();

    private boolean mHasShadow = true;
    private int mShadowColor = Color.BLACK;

    public interface ProgressAnimationListener {
        void onAnimationStart();

        void onAnimationFinish();

        void onAnimationProgress(int progress);
    }

    public CircularProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int style) {
        //so that shadow shows up properly for lines and arcs
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        int color;

        if (null != attrs) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CircularProgressBar, style, 0);


            this.mHasShadow = a.getBoolean(R.styleable.CircularProgressBar_hasShadow, true);

            color = a.getColor(R.styleable.CircularProgressBar_progressColor, Color.TRANSPARENT);
            mProgressColorPaint.setColor(color);

            color = a.getColor(R.styleable.CircularProgressBar_backgroundColor, Color.TRANSPARENT);
            mBackgroundColorPaint.setColor(color);

            color = a.getColor(R.styleable.CircularProgressBar_titleColor, Color.parseColor("#fff2f2f2"));
            mTitlePaint.setColor(color);

            color = a.getColor(R.styleable.CircularProgressBar_subtitleColor, Color.parseColor("#ff387CBE"));
            mSubtitlePaint.setColor(color);


            String t = a.getString(R.styleable.CircularProgressBar_progressTitle);
            if (t != null)
                mTitle = t;

            t = a.getString(R.styleable.CircularProgressBar_progressSubtitle);
            if (t != null)
                mSubTitle = t;

            mStrokeWidth = a.getInt(R.styleable.CircularProgressBar_strokeWidth, STROKE_WIDTH);

            mTitleTextSize = a.getDimension(R.styleable.CircularProgressBar_progressTitleTextSize, TITLE_TEXT_SIZE);
            mSubTitleTextSize = a.getDimension(R.styleable.CircularProgressBar_progressSubtitleTitleTextSize, TITLE_TEXT_SIZE);

            a.recycle();

        }

        mProgressColorPaint.setAntiAlias(true);
        mProgressColorPaint.setStyle(Style.STROKE);
        mProgressColorPaint.setStrokeWidth(mStrokeWidth);

        mBackgroundColorPaint.setAntiAlias(true);
        mBackgroundColorPaint.setStyle(Style.STROKE);
        mBackgroundColorPaint.setStrokeWidth(mStrokeWidth);

        mTitlePaint.setTextSize(mTitleTextSize);
        mTitlePaint.setStyle(Style.FILL);
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTypeface(Typeface.create("Roboto-Thin", Typeface.NORMAL));
        mTitlePaint.setShadowLayer(0.1f, 0, 1, Color.GRAY);
        mSubtitlePaint.setTextSize(mSubTitleTextSize);
        mSubtitlePaint.setStyle(Style.FILL);
        mSubtitlePaint.setAntiAlias(true);
        mSubtitlePaint.setTypeface(Typeface.create("Roboto-Thin", Typeface.BOLD));
        mSubtitlePaint.setShadowLayer(0.1f, 0, 1, Color.GRAY);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.drawArc(mCircleBounds, 0, 360, false, mBackgroundColorPaint);

        int prog = getProgress();
        float scale = getMax() > 0 ? (float) prog / getMax() * 360 : 0;

        if (mHasShadow)
            mProgressColorPaint.setShadowLayer(3, 0, 1, mShadowColor);
        canvas.drawArc(mCircleBounds, 270, scale, false, mProgressColorPaint);


        if (!TextUtils.isEmpty(mTitle)) {
            int xPos = (int) (getMeasuredWidth() / 2 - mTitlePaint.measureText(mTitle) / 2);
            int yPos = (int) (getMeasuredHeight() / 2);

            float titleHeight = Math.abs(mTitlePaint.descent() + mTitlePaint.ascent());
            if (TextUtils.isEmpty(mSubTitle)) {
                yPos += titleHeight / 2;
            }
            canvas.drawText(mTitle, xPos, yPos, mTitlePaint);

            yPos += titleHeight;
            xPos = (int) (getMeasuredWidth() / 2 - mSubtitlePaint.measureText(mSubTitle) / 2);

            canvas.drawText(mSubTitle, xPos, yPos, mSubtitlePaint);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min + 2 * STROKE_WIDTH, min + 2 * STROKE_WIDTH);

        mCircleBounds.set(STROKE_WIDTH, STROKE_WIDTH, min + STROKE_WIDTH, min + STROKE_WIDTH);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

        // the setProgress super will not change the details of the progress bar
        // anymore so we need to force an update to redraw the progress bar
        invalidate();
    }

    public void animateProgressTo(final int start, final int end, final ProgressAnimationListener listener) {
        if (start != 0)
            setProgress(start);

        final ObjectAnimator progressBarAnimator = ObjectAnimator.ofFloat(this, "animateProgress", start, end);
        progressBarAnimator.setDuration(1500);
        //		progressBarAnimator.setInterpolator(new AnticipateOvershootInterpolator(2f, 1.5f));
        progressBarAnimator.setInterpolator(new LinearInterpolator());

        progressBarAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                CircularProgressBar.this.setProgress(end);
                if (listener != null)
                    listener.onAnimationFinish();
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
                if (listener != null)
                    listener.onAnimationStart();
            }
        });

        progressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                int progress = ((Float) animation.getAnimatedValue()).intValue();
                if (progress != CircularProgressBar.this.getProgress()) {
                    Log.d(TAG, progress + "");
                    CircularProgressBar.this.setProgress(progress);
                    if (listener != null)
                        listener.onAnimationProgress(progress);
                }
            }
        });
        progressBarAnimator.start();
    }

    public synchronized void setTitle(String title) {
        this.mTitle = title;
        invalidate();
    }

    public synchronized void setTitleTextSize(int titleTextSize) {
        this.mTitleTextSize = titleTextSize;
        mTitlePaint.setTextSize(mTitleTextSize);
        invalidate();
    }

    public synchronized void setSubTitle(String subtitle) {
        this.mSubTitle = subtitle;
        invalidate();
    }

    public synchronized void setSubTitleColor(int color) {
        mSubtitlePaint.setColor(color);
        invalidate();
    }

    public synchronized void setTitleColor(int color) {
        mTitlePaint.setColor(color);
        invalidate();
    }

    public synchronized void setHasShadow(boolean flag) {
        this.mHasShadow = flag;
        invalidate();
    }

    public synchronized void setShadow(int color) {
        this.mShadowColor = color;
        invalidate();
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean getHasShadow() {
        return mHasShadow;
    }
}
