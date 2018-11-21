package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theclub on 1/9/15.
 */
public class EllipsizingTextView extends TextView {

    private static final String ELLIPSIS = "...더보기";
    private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();
    private Context context;
    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private String fullText;
    private int ellipsizingMaxLines = -1;
    private float lineSpacingMultiplier = 1.0f;
    private float lineAdditionalVerticalPadding = 0.0f;

    public boolean isProgrammaticChange() {
        return programmaticChange;
    }

    public int getEllipsizingMaxLines() {
        return ellipsizingMaxLines;
    }

    public void setEllipsizingMaxLines(int ellipsizingMaxLines) {
        this.ellipsizingMaxLines = ellipsizingMaxLines;
    }

    public EllipsizingTextView(Context context) {
        super(context);
        this.context = context;
    }

    public EllipsizingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void addEllipsizeListener(EllipsizeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }

        ellipsizeListeners.add(listener);
    }

    public void removeEllipsizeListener(EllipsizeListener listener) {
        ellipsizeListeners.remove(listener);
    }

    public boolean isEllipsized() {
        return isEllipsized;
    }

    public int getMaxLines() {
        return ellipsizingMaxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        // super.setMaxLines(maxLines);
        this.ellipsizingMaxLines = maxLines;

        isStale = true;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        this.lineAdditionalVerticalPadding = add;
        this.lineSpacingMultiplier = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            fullText = text.toString();
            isStale = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStale) {
            super.setEllipsize(null);
            resetText();
        }
        super.onDraw(canvas);
    }

    private void resetText() {
        int maxLines = getMaxLines();
        String workingText = fullText;
        boolean ellipsized = false;
        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workingText);
            if (layout.getLineCount() > maxLines) {
                workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim();
                while (createWorkingLayout(workingText).getLineCount() > maxLines) {
                    workingText = workingText.substring(0, workingText.length() - 2);
                }
                workingText = workingText + ELLIPSIS;
                ellipsized = true;
            }
        }

        if (!workingText.equals(fullText)) {
            programmaticChange = true;
            try {
                //use a loop to change text color
                Object[] re = Utils.convertTag(context, workingText, true);

                workingText = (String) re[0];
                Spannable WordtoSpan = (Spannable) re[1];

                WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), workingText.length() - ELLIPSIS.length(), workingText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // textView.setText(WordtoSpan);

                setText(WordtoSpan);

            } finally {
                programmaticChange = false;
            }
        } else {
            //use a loop to change text color
            Object[] re = Utils.convertTag(context, workingText, true);

            workingText = (String) re[0];
            Spannable WordtoSpan = (Spannable) re[1];

            setText(WordtoSpan);
        }

        isStale = false;
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized;
            for (EllipsizeListener listener : ellipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized, workingText);
            }
        }
    }

    private Layout createWorkingLayout(String workingText) {
        return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(), Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
    }

    @Override
    public void setEllipsize(TextUtils.TruncateAt where) {
        // Ellipsize settings are not respected
    }

    public interface EllipsizeListener {
        void ellipsizeStateChanged(boolean ellipsized, String workingText);
    }
}
