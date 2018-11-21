package com.devstories.anipointcompany.android.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseUtils {

    public static void updateRank(Context context, int _id, Integer[] newRank) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("page", newRank[0]);
        values.put("th", newRank[1]);

        long result = db.update("targets", values, "_id = " + _id, null);
        if (result != 1) {
            // Utils.alert(context, "대상을 저장하는데 실패하였습니다.");
            // System.out.println("대상을 저장하는데 실패하였습니다. : " + values);
        }

        db.close();
    }

}
