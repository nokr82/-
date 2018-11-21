package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.devstories.anipointcompany.android.R;


/**
 * Created by dev1 on 2018-04-02.
 */

public class RationalRelativeLayout extends RelativeLayout {
    private float ratio1 = -1.0f;
    private float ratio2 = -1.0f;

    public RationalRelativeLayout(Context context) {
        super(context);
    }

    public RationalRelativeLayout(Context context, AttributeSet attrs) {
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

        } finally {
            ta.recycle();
        }

    }

    public RationalRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//
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

        } finally {
            ta.recycle();
        }

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(this.ratio1 > 0 && this.ratio2 > 0) {
            int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
            finalHeight = (int) (this.ratio2 * originalWidth / this.ratio1);
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }

}
