package com.william.chocotvdemo.common;

import android.content.Context;
import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.william.chocotvdemo.common.DBA.Field;
import com.william.chocotvdemo.model.Drama;
import com.william.chocotvdemo.utils.HILog;
import com.william.chocotvdemo.utils.StringUtil;

import java.util.List;

public class CommonDBUtils {
    private static final String TAG = CommonDBUtils.class.getSimpleName();
    private static Context m_ctx;

    public static void setContext(Context ctx) {
        CommonDBUtils.m_ctx = ctx;
    }
    

    public static Cursor delDramaCursor() {
        if (new Select().from(Drama.class).exists()) {
            Cursor resultCursor = Cache.openDatabase().rawQuery(new Delete().from(Drama.class).toSql(), null);
            HILog.d(TAG, "delDramaCursor: count = " + resultCursor.getCount());
            return resultCursor;
        }
        return null;
    }

    public static List<Drama> getDramaList(String fieldname, int fieldvalue){
        Select select = new Select();
        String whereclause =  fieldname + " LIKE " + StringUtil.addquote("%" + fieldvalue + "%");
//        String whereclause =  fieldname + " EQUALS  " + StringUtil.addquote(String.valueOf(fieldvalue));
        List<Drama> DramaList = select.from(Drama.class)
                .where(whereclause)
                .execute();
        return  DramaList;
    }


    public static Cursor getDramaCursor() {
        Cursor resultCursor=null;

        Select select = new Select();
        String whereclause =  Field.DRAMA_ID + " IS NOT NULL AND " +  Field.DRAMA_ID + " != \"\"";
        String sqlcommand = select.from(Drama.class)
                .where(whereclause)
                .orderBy(DBA.Field.DRAMA_ID + " ASC")
                .toSql();
        resultCursor = Cache.openDatabase().rawQuery(sqlcommand, null);
        HILog.d(TAG, "getDramaCursor: count = " + resultCursor.getCount());
        return resultCursor;
    }


    public static String getDramaStringValue(int position, String column) {
        HILog.d(TAG, "getDramaStringValue: column = " + column);
        String result = "";
        Cursor cursor = getDramaCursor();
        cursor.moveToPosition(position);
        switch (column){
            case Field.CREATED_AT:
                result = cursor.getString(cursor.getColumnIndex(Field.CREATED_AT));
                break;
            case Field.NAME:
                result = cursor.getString(cursor.getColumnIndex(Field.NAME));
                break;
            case Field.THUMB:
                result = cursor.getString(cursor.getColumnIndex(Field.THUMB));
                break;
        }
        HILog.d(TAG, "getDramaStringValue: result = " + result);
        cursor.close();
        return result;
    }


    public static int getDramaIntValue(int position, String column) {
        HILog.d(TAG, "getDramaIntValue: column = " + column);
        int result = -1;
        Cursor cursor = getDramaCursor();
        cursor.moveToPosition(position);
        switch (column){
            case Field.DRAMA_ID:
                result = cursor.getInt(cursor.getColumnIndex(Field.DRAMA_ID));
                break;
            case Field.TOTAL_VIEWS:
                result = cursor.getInt(cursor.getColumnIndex(Field.TOTAL_VIEWS));
                break;
        }
        HILog.d(TAG, "getDramaIntValue: result = " + result);
        return result;
    }

    public static Cursor queryDramaforLikeName(String name){
        Cursor resultCursor=null;
        HILog.d(TAG, "queryDramaforLikeName:");

        Select select = new Select();
        String whereclause =  DBA.Field.NAME + " LIKE '%" + name + "%'";
        String sqlcommand = select.from(Drama.class)
                .where(whereclause)
                .toSql();
        HILog.d(TAG, "queryDramaforLikeName: sqlcommand = " + sqlcommand);
        resultCursor = Cache.openDatabase().rawQuery(sqlcommand, null);
        HILog.d(TAG, "queryDramaforLikeName: count = " + resultCursor.getCount());
        return resultCursor;
    }

}
