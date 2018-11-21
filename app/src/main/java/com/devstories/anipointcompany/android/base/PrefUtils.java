package com.devstories.anipointcompany.android.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    static String masterKey = "member_id";

    public static void setPreference(Context context, String key, boolean value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(getStringPreference(context, masterKey) + "_" + key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, int value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(getStringPreference(context, masterKey) + "_" + key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, long value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(getStringPreference(context, masterKey) + "_" + key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, float value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putFloat(getStringPreference(context, masterKey) + "_" + key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, double value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(getStringPreference(context, masterKey) + "_" + key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public static void setPreference(Context context, String key, String value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();

        if (masterKey.equals(key)) {
            editor.putString(key, value);
        } else {
            editor.putString(getStringPreference(context, masterKey) + "_" + key, value);
        }

        editor.commit();
    }

    public static boolean getBooleanPreference(Context context, String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(getStringPreference(context, masterKey) + "_" + key, false);
    }

    public static boolean getBooleanPreference(Context context, String key, boolean def) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(getStringPreference(context, masterKey) + "_" + key, def);
    }

    public static boolean getBooleanPreference(Context context, String key, boolean def, boolean raw) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (raw) {
            return mPrefs.getBoolean(key, def);
        } else {
            return mPrefs.getBoolean(getStringPreference(context, masterKey) + "_" + key, def);
        }
    }

    public static String getStringPreference(Context context, String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (masterKey.equals(key)) {
            return mPrefs.getString(key, null);
        } else {
            return mPrefs.getString(getStringPreference(context, masterKey) + "_" + key, null);
        }

    }

    public static String getStringPreference(Context context, String key, String defvalue) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(getStringPreference(context, masterKey) + "_" + key, defvalue);
    }

    public static int getIntPreference(Context context, String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getInt(getStringPreference(context, masterKey) + "_" + key, 0);
    }

    public static int getIntPreference(Context context, String key, int defvalue) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getInt(getStringPreference(context, masterKey) + "_" + key, defvalue);
    }

    public static long getLongPreference(Context context, String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getLong(getStringPreference(context, masterKey) + "_" + key, 0);
    }

    public static long getLongPreference(Context context, String key, long defvalue) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getLong(getStringPreference(context, masterKey) + "_" + key, defvalue);
    }

    public static float getFloatPreference(Context context, String key, float defvalue) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getFloat(getStringPreference(context, masterKey) + "_" + key, defvalue);
    }

    public static double getDoublePreference(Context context, String key) {
        return getDoublePreference(context, key, -1.0);
    }

    public static double getDoublePreference(Context context, String key, double defvalue) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.longBitsToDouble(mPrefs.getLong(getStringPreference(context, masterKey) + "_" + key, Double.doubleToRawLongBits(defvalue)));
    }

    public static void removePreference(Context context, String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();

        if (masterKey.equals(key)) {
            editor.remove(key);
        } else {
            editor.remove(getStringPreference(context, masterKey) + "_" + key);
        }

        editor.commit();
    }

    public static boolean isLogin(Context context) {
        return getStringPreference(context, masterKey) != null;
    }

    public static String email(Context context) {
        return getStringPreference(context, masterKey);
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.contains(getStringPreference(context, masterKey) + "_" + key);
    }

    public static String uuid(Context dialogContext) {
        return PrefUtils.getStringPreference(dialogContext, "UUID");
    }

    public static String nickname(Context dialogContext) {
        return PrefUtils.getStringPreference(dialogContext, "nickname");
    }

    public static void clear(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.commit();
    }
}
