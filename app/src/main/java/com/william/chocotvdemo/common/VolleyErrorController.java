package com.william.chocotvdemo.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.william.chocotvdemo.R;

public class VolleyErrorController {
	VolleyError volleyError;
	Context context;
	String msg;
	public VolleyErrorController(Context context, VolleyError volleyError) {
		this.volleyError = volleyError;
		this.context=context;
		if (volleyError instanceof NoConnectionError) {
//			this.msg="連不上伺服器,請開啟網路";
			this.msg = context.getString(R.string.volley_error_noconnection);
		} else if (volleyError instanceof TimeoutError) {
			this.msg = context.getString(R.string.volley_error_timeout);
		} else if (volleyError instanceof AuthFailureError) {
			this.msg = context.getString(R.string.volley_error_authfailure);
		} else if (volleyError instanceof ServerError) {
			this.msg = context.getString(R.string.volley_error_server);
		} else if (volleyError instanceof NetworkError) {
			this.msg = context.getString(R.string.volley_error_network);
		} else if (volleyError instanceof ParseError) {
			this.msg = context.getString(R.string.volley_error_parse);
		}
	}

	
	public void showMsg() {
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(context, getMsg(), Toast.LENGTH_SHORT).show();
				Looper.loop();
			};
		}.start();
	}

	public String getMsg(){
		return this.msg;
		
	}
}
