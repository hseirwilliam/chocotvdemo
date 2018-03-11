package com.william.chocotvdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.william.chocotvdemo.R;
import com.william.chocotvdemo.common.CommonDBUtils;
import com.william.chocotvdemo.common.Constants;
import com.william.chocotvdemo.common.DBA;
import com.william.chocotvdemo.common.WhVollyPost;
import com.william.chocotvdemo.model.Drama;
import com.william.chocotvdemo.utils.HILog;
import com.william.chocotvdemo.vo.BaseVo;
import com.william.chocotvdemo.vo.CHOCOTV_DRAMA_LIST_ResponseVo;
import com.william.chocotvdemo.vo.CHOCOTV_DRAMA_LIST_drama_list_item_EntityVo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static Activity mActivity;
    private Cursor mDramaCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = MainActivity.this;
        //Model;View;
        doWhVollyPost_DramaList();
    }

    private void doWhVollyPost_DramaList(){
        WhVollyPost.EzCallBack<CHOCOTV_DRAMA_LIST_ResponseVo> drama_list_responseVo = new WhVollyPost.EzCallBack<CHOCOTV_DRAMA_LIST_ResponseVo>() {

            @Override
            public void onSuccess(CHOCOTV_DRAMA_LIST_ResponseVo dramaRespondVo) {
                HILog.d(TAG, "onSuccess:");
                saveDramaListIntoDb(dramaRespondVo);
                showDramaListView();
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
        mDramaCursor = CommonDBUtils.getDramaCursor();
        int count = mDramaCursor.getCount();
        HILog.d(TAG, "showDramaListView: count = " + count);

    }

}
