package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.devstories.anipointcompany.android.R;


/**
 * Created by theclub on 1/3/17.
 */

public class RationalLinearLayout extends LinearLayout {

    private boolean adjustHeight = true;
    private float ratio1 = -1.0f;
    private float ratio2 = -1.0f;

    public RationalLinearLayout(Context context) {
        super(context);
    }

    public RationalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.app, 0, 0);
        try {
            String ratioStr = ta.getString(R.styleable.app_ratio);
            if(ratioStr != null && ratioStr.indexOf(":") > 0) {

                // 6.5 : 3 = width : height
                // height = 3 * width / 6.5
                String[] arr = ratioStr.split(":");
                if (arr != null && arr.length == 2) {
                    this.ratio1 = Float.parseFloat(arr[0]);
                    this.ratio2 = Float.parseFloat(arr[1]);
                }

            }

            adjustHeight = ta.getBoolean(R.styleable.app_adjustHeight, true);

        } finally {
            ta.recycle();
        }

    }

    public RationalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.app, 0, 0);
        try {
            String ratioStr = ta.getString(R.styleable.app_ratio);
            if(ratioStr != null && ratioStr.indexOf(":") > 0) {

                // 6.5 : 3 = width : height
                // height = 3 * width / 6.5
                String[] arr = ratioStr.split(":");
                if (arr != null && arr.length == 2) {
                    this.ratio1 = Float.parseFloat(arr[0]);
                    this.ratio2 = Float.parseFloat(arr[1]);
                }

                adjustHeight = ta.getBoolean(R.styleable.app_adjustHeight, true);

            }

        } finally {
            ta.recycle();
        }

    }

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(adjustHeight) {
            int finalHeight = MeasureSpec.getSize(heightMeasureSpec);
            if (this.ratio1 > 0 && this.ratio2 > 0) {
                int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
                finalHeight = (int) (this.ratio2 * originalWidth / this.ratio1);
            }
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
        } else {
            int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (this.ratio1 > 0 && this.ratio2 > 0) {
                int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
                finalWidth = (int) (this.ratio1 * originalHeight / this.ratio2);
            }
            super.onMeasure(MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
    }
}
