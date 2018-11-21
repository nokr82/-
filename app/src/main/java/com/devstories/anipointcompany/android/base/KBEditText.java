package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by theclub on 6/3/15.
 */
public class KBEditText extends EditText {

    private KBEditTextChangeListener kbEditTextChangeListener;

    public KBEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            kbEditTextChangeListener.keyboardClosed();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setChangeListener(KBEditTextChangeListener kbEditTextChangeListener) {
        this.kbEditTextChangeListener = kbEditTextChangeListener;
    }
}
