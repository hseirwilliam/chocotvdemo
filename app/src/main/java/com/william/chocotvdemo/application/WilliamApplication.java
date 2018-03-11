package com.william.chocotvdemo.application;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Application;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration.Builder;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.william.chocotvdemo.model.Drama;
import com.william.chocotvdemo.utils.HILog;

import java.net.Socket;


public class WilliamApplication extends Application {
    private static String TAG = WilliamApplication.class.getSimpleName();
    private static int TIMEOUT_DEFAULT = 30000;
    public static Bus bus;
    private static WilliamApplication mInstance;
    private static RequestQueue mRequestQueue;
    private Socket mSocket;

    public static synchronized WilliamApplication getInstance() {
        WilliamApplication WilliamApplication;
        synchronized (WilliamApplication.class) {
            WilliamApplication = mInstance;
        }
        return WilliamApplication;
    }

    public void onCreate() {
        HILog.d(TAG, "onCreate:");
        super.onCreate();
        mInstance = this;
        bus = new Bus(ThreadEnforcer.ANY);
        initializeDB();
    }

    protected void initializeDB() {
        Builder configurationBuilder = new Builder(this);
        configurationBuilder.addModelClasses(Drama.class);
        ActiveAndroid.initialize(configurationBuilder.create());
    }

    public void onLowMemory() {
        HILog.d(TAG, "On Low Memory!!!!");
        MemoryInfo mi = new MemoryInfo();
        ((ActivityManager) getSystemService("activity")).getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576;
        long thresholdMegs = mi.threshold / 1048576;
        HILog.d(TAG, "availableMegs:" + availableMegs + " thresholdMegs:" + thresholdMegs);
        super.onLowMemory();
    }

    public static RequestQueue getRequestQueue() {
        HILog.d(TAG, "getRequestQueue:");
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mInstance);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        addToRequestQueue(req, tag, TIMEOUT_DEFAULT);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag, int timeout) {
        HILog.d(TAG, "addToRequestQueue:");
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        req.setTag(tag);
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        HILog.d(TAG, "cancelPendingRequests:");
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
