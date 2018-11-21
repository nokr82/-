package com.devstories.anipointcompany.android.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

//import kr.co.threemeals.android.actions.MemberAction;

/**
 * Created by theclub on 5/5/15.
 */
public class LocationPushService extends Service implements OnLocationUpdatedListener {

    private Context context = null;

    private PowerManager.WakeLock mWakeLock;

    private Handler locUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SmartLocation.with(context).location().stop();

            SmartLocation smartLocation = new SmartLocation.Builder(context).logging(true).build();
            smartLocation.location().start(LocationPushService.this);
        }
    };

    public LocationPushService() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        retrieveLocation();

        return START_STICKY;
    }

    private void retrieveLocation() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long locationUpdatedAt = PrefUtils.getLongPreference(context, "location_updated_at");
                int locationUpdatePeriod = PrefUtils.getIntPreference(context, "location_update_period", 3 * 60 * 1000);

                if (System.currentTimeMillis() - locationUpdatedAt > locationUpdatePeriod) {
                    locUpdateHandler.sendEmptyMessage(0);
                }
            }
        }, 0, 1 * 60 * 1000);

    }

//    @Override
//    public void onLocationUpdated(Location location) {
//        SmartLocation.with(context).location().stop();
//
//        saveLocation(location.getLatitude(), location.getLongitude());
//    }

//    private void saveLocation(final double lat, final double lng) {
//        int memberId = PrefUtils.getIntPreference(context, "id");
//
//        MemberAction.saveLocation(memberId, lat, lng, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                boolean result = false;
//                try {
//                    result = response.getBoolean("result");
//                    if (result) {
//                        PrefUtils.setPreference(context, "location_updated_at", System.currentTimeMillis());
//
//                        PrefUtils.setPreference(context, "lat", lat);
//                        PrefUtils.setPreference(context, "lng", lng);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//        });
//    }

    protected void monitor() {
        /*
        int icon = getApplicationInfo().icon; // Notification 발생시 출력할 아이콘
        String title = "HIKEYWORD Service"; // Notification 발생시 아래로 내렸을 때 첫번째 줄
        String subtitle = ""; // Notification 발생시 아래로 내렸을 때 두번째 줄
        String ticker = ""; // 이벤트 발생시 위에 출력될 내용
        Notification notification;

        if(Build.VERSION.SDK_INT >= 16) {
            notification = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(subtitle)
                    .setTicker(ticker)
                    .setSmallIcon(icon)
                    .setContentIntent(null)
                    .build();
        } else {
            notification = new Notification(icon, ticker, System.currentTimeMillis());
            notification.setLatestEventInfo(this, title, subtitle, null);
        }

        startForeground(1, notification);

        // int myPid = Utils.getMyPid();
        long lastActionStartTime = PrefUtils.getLongPreference(this, "LAST_ACTION_START_TIME", -1);
        long interval = System.currentTimeMillis() - lastActionStartTime;

        // register alarmService
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.cancel(restartServicePendingIntent);
        alarmService.set(AlarmManager.RTC, System.currentTimeMillis() + (1000 * 3), restartServicePendingIntent);
        // alarmService.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), (1000 * 1), restartServicePendingIntent);

        // Optional: Screen Always On Mode!
        // Screen will never switch off this way
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "a_tag");
        mWakeLock.acquire();
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        rerurn();
    }

    private void rerurn() {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.cancel(restartServicePendingIntent);
        alarmService.set(AlarmManager.RTC, System.currentTimeMillis() + (1000 * 3), restartServicePendingIntent);
    }

    @Override
    public void onLocationUpdated(Location location) {

    }
}
