package com.devstories.anipointcompany.android.base;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Dialog that allows to stay open even if the buttons are clicked
 * 
 * @author plahoda
 * 
 */
public class OpenAlertDialog extends AlertDialog {

    private boolean stayOpen = true;

    public OpenAlertDialog(Context context) {
        super(context);
    }

    @Override
    public void dismiss() {
        // eat the original dismiss code if the stay open flag is set to true
        if (stayOpen == false) {
            super.dismiss();
        }
        // Original code (2.2)
        // if (Thread.currentThread() != mUiThread) {
        // mHandler.post(mDismissAction);
        // } else {
        // mDismissAction.run();
        // }
    }

    /**
     * @param stayOpen
     *            the stayOpen to set
     */
    public void setStayOpen(boolean stayOpen) {
        this.stayOpen = stayOpen;
    }

    /**
     * @return the stayOpen
     */
    public boolean isStayOpen() {
        return stayOpen;
    }
}
