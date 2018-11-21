package com.devstories.anipointcompany.android.base;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by hooni on 16. 3. 27..
 */
public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed(int message) {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide(message);
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide(int message) {
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}