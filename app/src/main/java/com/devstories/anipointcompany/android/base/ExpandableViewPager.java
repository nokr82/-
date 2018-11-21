package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by theclub on 6/7/15.
 */
public class ExpandableViewPager extends ViewPager {

    private Context context;

    public ExpandableViewPager (Context context) {
        super(context);

        this.context = context;
    }

    public ExpandableViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

        final View tab = getChildAt(0);
        int width = getMeasuredWidth();
        int tabHeight = tab.getMeasuredHeight();

        if (wrapHeight) {
            // Keep the current measured width.
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        float screenHeight = Utils.getScreenHeight(context);

        int fragmentHeight = measureFragment(((Fragment) getAdapter().instantiateItem(this, getCurrentItem())).getView());
        if(fragmentHeight < screenHeight) {
            fragmentHeight = (int) screenHeight;
        }

        // heightMeasureSpec = MeasureSpec.makeMeasureSpec(tabHeight + fragmentHeight + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()), MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(tabHeight + fragmentHeight, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }
}
