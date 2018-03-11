package com.william.chocotvdemo.utils;

import android.util.Log;

public class HILog {


    private static final boolean EnableLog = true;
    private static final boolean EnableSimpleLog = true;

    public static void d(boolean realtimetracking, String tag, String... msgs) {
        if(EnableSimpleLog||EnableLog) {
            if(realtimetracking){
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("HILog:");
                for (String msg : msgs) {
                    strBuilder.append(msg);
                }
                Log.d(tag, strBuilder.toString());
            }
        }
    }

    public static void d(String tag, String... msgs) {
        if (true == EnableLog) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("HILog:");
            for (String msg : msgs) {
                strBuilder.append(msg);
            }
            Log.d(tag, strBuilder.toString());
        }
    }

    public static void w(String tag, String... msgs) {
        w(tag, null, msgs);
    }

    public static void w(String tag, Exception e, String... msgs) {
        if (true == EnableLog) {
            StringBuilder strBuilder = new StringBuilder();
            for (String msg : msgs) {
                strBuilder.append(msg);
            }
            Log.w(tag, strBuilder.toString(), e);
        }
    }

    public static void e(String tag, String... msgs) {
        e(tag, null, msgs);
    }

    public static void e(String tag, Exception e, String... msgs) {
        if (true == EnableLog) {
            StringBuilder strBuilder = new StringBuilder();
            for (String msg : msgs) {
                strBuilder.append(msg);
            }
            Log.e(tag, strBuilder.toString(), e);
        }
    }

}
