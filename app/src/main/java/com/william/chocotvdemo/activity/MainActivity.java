package com.william.chocotvdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.bumptech.glide.Glide;
import com.william.chocotvdemo.R;
import com.william.chocotvdemo.common.CommonDBUtils;
import com.william.chocotvdemo.common.Constants;
import com.william.chocotvdemo.common.DBA;
import com.william.chocotvdemo.common.WhVollyPost;
import com.william.chocotvdemo.model.Drama;
import com.william.chocotvdemo.utils.HILog;
import com.william.chocotvdemo.utils.StringUtil;
import com.william.chocotvdemo.vo.BaseVo;
import com.william.chocotvdemo.vo.CHOCOTV_DRAMA_LIST_ResponseVo;
import com.william.chocotvdemo.vo.CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.william.chocotvdemo.common.CommonDBUtils.queryDramaforLikeName;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static Activity mActivity;
    private Cursor mDramaCursor = null;
    DisplayMetrics metrics;
    private int m_Height_item, m_Width_item;
    private int m_Height, m_Width;
    final int listview_item_count = 3;
    ListView mDramaListView;
    DramaListViewCursorAdapter dramaListViewCursorAdapter;
    EditText mEtSearch;
    Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = MainActivity.this;

        //View
        metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        m_Height = metrics.heightPixels;
        m_Width = metrics.widthPixels;
        m_Height_item = (m_Height - 200) / listview_item_count;
        m_Width_item = m_Width;
        mDramaListView = (ListView) findViewById(R.id.lvDrama);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });
        mEtSearch = (EditText) findViewById(R.id.et_search);
        TextWatcher mTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                HILog.d(TAG, "beforeTextChanged : count = " + count + "; " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HILog.d(TAG, "onTextChanged : count = " + count + "; " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                HILog.d(TAG, "afterTextChanged: " + s.toString());
                mDramaCursor = queryDramaforLikeName(s.toString());
                HILog.d(TAG, "afterTextChanged : queryDramaforLikeName.getCount() = " + mDramaCursor.getCount());
                showDramaListView();
                setSearchText(s.toString());
            }
        };
        mEtSearch.addTextChangedListener(mTextWatcher);
        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                HILog.d(TAG, "hasFocus = " + hasFocus);
            }
        });

        //Model;
        if(isNetworkConnected()){
            HILog.d(TAG, "isNetworkConnected = true.");
            doWhVollyPost_DramaList();
        } else {
            mDramaCursor = CommonDBUtils.getDramaCursor();
            showDramaListView();

        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        mEtSearch.setText(getSearchText());
    }

    private void doWhVollyPost_DramaList(){
        WhVollyPost.EzCallBack<CHOCOTV_DRAMA_LIST_ResponseVo> drama_list_responseVo = new WhVollyPost.EzCallBack<CHOCOTV_DRAMA_LIST_ResponseVo>() {

            @Override
            public void onSuccess(CHOCOTV_DRAMA_LIST_ResponseVo dramaRespondVo) {
                HILog.d(TAG, "onSuccess:");
                saveDramaListIntoDb(dramaRespondVo);
                mDramaCursor = CommonDBUtils.getDramaCursor();
                showDramaListView();
                mEtSearch.setText(getSearchText());
            }

            @Override
            public void onFail(String error) {
                HILog.d(TAG, "onFail:");
                //ToDo: Show dialog or else...
            }
        };

        new WhVollyPost<BaseVo, CHOCOTV_DRAMA_LIST_ResponseVo>(this, drama_list_responseVo, true).executeChoco(CHOCOTV_DRAMA_LIST_ResponseVo.class);
    }
    private void saveDramaListIntoDb(CHOCOTV_DRAMA_LIST_ResponseVo dramaRespondVo) {
        HILog.d(TAG, "saveDramaListIntoDb:");
        Drama drama = new Drama();
//        new Delete().from(Drama.class).execute();
        List<CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo> drama_list = dramaRespondVo.getData();
        int len = drama_list.size();
        HILog.d(TAG, "saveDramaListIntoDb: drama_list_response_entityVo.size() = " + len);
        ActiveAndroid.beginTransaction();
        for(int i=0; i<len; i++){
            drama = new Drama();
            drama.drama_id = drama_list.get(i).getDrama_id();
            drama.name = drama_list.get(i).getName();
            drama.total_views = drama_list.get(i).getTotal_views();
            drama.created_at = drama_list.get(i).getCreated_at();
            drama.thumb = drama_list.get(i).getThumb();
            drama.rating = drama_list.get(i).getRating();
            drama.save();
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
    }

    private void showDramaListView(){
        int count = mDramaCursor.getCount();
        HILog.d(TAG, "showDramaListView: count = " + count);
        dramaListViewCursorAdapter = new DramaListViewCursorAdapter(mActivity, mDramaCursor);
        mDramaListView.setAdapter(dramaListViewCursorAdapter);
    }

    public static class ViewHolder {
        ImageView ivThumb;
        TextView tvName;
        TextView tvRating;
        TextView tvCreated_at;
        TextView tvTotal_views;
        LinearLayout llDramalistitem;
    }

    public class DramaListViewCursorAdapter extends CursorAdapter {
        LayoutInflater mInflater;
        public DramaListViewCursorAdapter(Context context, Cursor c) {
            super(context, c);
            HILog.d(TAG, "DramaListViewCursorAdapter:");
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            HILog.d(TAG, "DramaListViewCursorAdapter: newView: position = " + cursor.getPosition());
            ViewHolder holder = new ViewHolder();
            View v = mInflater.inflate(R.layout.listview_drama_row_layout, parent, false);
            holder.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvRating = (TextView) v.findViewById(R.id.tvRating);
            holder.tvCreated_at = (TextView) v.findViewById(R.id.tvCreated_at);
            holder.tvTotal_views = (TextView) v.findViewById(R.id.tvTotal_views);
            holder.llDramalistitem = (LinearLayout) v.findViewById(R.id.ll_dramalistitem);
            AbsListView.LayoutParams params=new AbsListView.LayoutParams(m_Width_item, m_Height_item);
            v.setLayoutParams(params);
            v.setTag(R.id.ll_dramalistitem, holder);
            return v;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            ViewHolder holder = null;
            holder = (ViewHolder) view.getTag(R.id.ll_dramalistitem);
            final int drama_id = cursor.getInt(cursor.getColumnIndex(DBA.Field.DRAMA_ID));
            String tvName_8 = cursor.getString(cursor.getColumnIndex(DBA.Field.NAME));
            String strnThumb = Uri.parse(cursor.getString(cursor.getColumnIndex(DBA.Field.THUMB))).toString();
            Glide.with(mActivity).load(strnThumb).into(holder.ivThumb);
            holder.tvName.setText(StringUtil.From8859toUtf8(tvName_8));
            String tvRating = Float.toString(cursor.getFloat(cursor.getColumnIndex(DBA.Field.RATING)));
            holder.tvRating.setText(tvRating);
            String tvCreated_at = cursor.getString(cursor.getColumnIndex(DBA.Field.CREATED_AT));
            holder.tvCreated_at.setText(tvCreated_at);
            String tvTotal_views = String.valueOf(cursor.getString(cursor.getColumnIndex(DBA.Field.TOTAL_VIEWS)));
            holder.tvTotal_views.setText(tvTotal_views);

            holder.llDramalistitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HILog.d(TAG, "DramaListViewCursorAdapter: bindView: llDramalistitem: clicked. drama_id = " + drama_id);
                    showDramaInfoDialog(drama_id);
                }
            });

            return;
        }

        @Override
        public int getItemViewType(int position) {
//            HILog.d(TAG, "DramaListViewCursorAdapter: getItemViewType:");
            return 1;
        }

        @Override
        public int getViewTypeCount() {
//            HILog.d(TAG, "DramaListViewCursorAdapter: getViewTypeCount:");
            return 1;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private String getSearchText(){
        SharedPreferences searchdata = getSharedPreferences(Constants.SEARCH, 0);
        String searchtext = searchdata.getString(Constants.SEARCHTEXT, Constants.EMPTY_STRING);
        HILog.d(TAG, "getSearchText: searchtext: " + searchtext);
        return searchtext;
    }

    private void setSearchText(String searchtext){
        HILog.d(TAG, "setSearchText: searchtext: " + searchtext);
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.SEARCH, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SEARCHTEXT, searchtext);
        editor.commit();
    }


    public void showDramaInfoDialog(int drama_id) {
        List<Drama> dramaList = CommonDBUtils.getDramaList(DBA.Field.DRAMA_ID, drama_id);
        HILog.d(TAG, "showDramaInfoDialog: dramaList.size(): " + dramaList.size());
        String strnThumb = Uri.parse(dramaList.get(0).thumb).toString();

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final ImageView ivThumb_dialog = alertLayout.findViewById(R.id.ivThumb_dialog);
        Glide.with(mActivity).load(strnThumb).into(ivThumb_dialog);
        final TextView tvRating_dialog = alertLayout.findViewById(R.id.tvRating_dialog);
        tvRating_dialog.setText(Float.toString(dramaList.get(0).rating));
        final TextView tvTotal_views_dialog = alertLayout.findViewById(R.id.tvTotal_views_dialog);
        tvTotal_views_dialog.setText(Integer.toString(dramaList.get(0).total_views));
        final TextView tvCreated_at_dialog = alertLayout.findViewById(R.id.tvCreated_at_dialog);
        tvCreated_at_dialog.setText(dramaList.get(0).created_at);

        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
        alert.setTitle(StringUtil.From8859toUtf8(dramaList.get(0).name));
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();
    }
}
