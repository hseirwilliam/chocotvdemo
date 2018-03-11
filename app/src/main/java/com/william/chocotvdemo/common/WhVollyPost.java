package com.william.chocotvdemo.common;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.william.chocotvdemo.application.WilliamApplication;
import com.william.chocotvdemo.utils.HILog;
import com.william.chocotvdemo.utils.StringUtil;

public class WhVollyPost<REQ,REP> {
    private static final String TAG = WhVollyPost.class.getSimpleName();
    
    private static final String URL = Constants.CHOCOTVURL;
    
    private static boolean OPEN_ERROR_DIALOG = false;
    
    private final String url;
    
    public interface EzCallBack<REP2>{
        void onSuccess(REP2 userRespondVo);
        void onFail(String error);
    }
    
    private Context mContext;
    private EzCallBack<REP> callback;
    
    private boolean isOpenErrorDialog;
    private int mTimeOut;
    
    public WhVollyPost(Context context , EzCallBack<REP> c, String serverUrl, boolean isOpen, int timeOut) {
    	mContext = context;
    	callback = c;
    	url = serverUrl;
    	isOpenErrorDialog = isOpen;
    	mTimeOut = timeOut;
    }
    
    public WhVollyPost(Context context , EzCallBack<REP> c){
        this(context, c, URL, OPEN_ERROR_DIALOG, Constants.TIMEOUT_DEFAULT);
    }
    
    public WhVollyPost(Context context , EzCallBack<REP> c, String serverUrl) {
    	    this(context, c, serverUrl, OPEN_ERROR_DIALOG, Constants.TIMEOUT_DEFAULT);
    }
    
    public WhVollyPost(Context context , EzCallBack<REP> c, String serverUrl, boolean isOpen) {
        this(context, c, serverUrl, isOpen, Constants.TIMEOUT_DEFAULT);
    }

    public WhVollyPost(Context context , EzCallBack<REP> c, String serverUrl, int timeOut) {
        this(context, c, serverUrl, OPEN_ERROR_DIALOG, timeOut);
    }

    public WhVollyPost(Context context , EzCallBack<REP> c, boolean isOpen) {
        this(context, c, URL, isOpen, Constants.TIMEOUT_DEFAULT);
    }
    
    public WhVollyPost(Context context , EzCallBack<REP> c, boolean isOpen, int timeOut) {
        this(context, c, URL, isOpen, timeOut);
    }
    
    public WhVollyPost(Context context , EzCallBack<REP> c, int timeOut) {
    	    this(context, c, URL, OPEN_ERROR_DIALOG, timeOut);
    }

    public void executeChoco(final Class<?> VOclass) {
        HILog.d(TAG, "executeChoco: url = " + url);

        RequestQueue requestQueue = WilliamApplication.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Method.GET,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                HILog.d(TAG, "executeChoco: stringRequest: onResponse = " + response);
                REP userRespondVo = null;
                try {
                    String respondJsonString = response.toString();
//                    HILog.d(TAG, "RespondVo : " + respondJsonString);
                    userRespondVo = (REP) StringUtil.fromJson(respondJsonString, VOclass);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(null);
                    return;
                }
                if(userRespondVo!=null)
                    HILog.d(TAG, "onSuccess obj type:" + userRespondVo.getClass());
                callback.onSuccess(userRespondVo);
                return;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                HILog.d(TAG, "executeString: onErrorResponse: " + error.getMessage().toString());
                new VolleyErrorController(mContext, error).showMsg();
                callback.onFail(error.getMessage());
                return;
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                return params;
//            }
        };

        WilliamApplication.getInstance().addToRequestQueue(stringRequest, "chocotv", mTimeOut);

    }

}
