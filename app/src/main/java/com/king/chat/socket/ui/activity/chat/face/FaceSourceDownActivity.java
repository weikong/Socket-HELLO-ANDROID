package com.king.chat.socket.ui.activity.chat.face;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.FaceSourceBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.activity.base.BaseUIActivity;
import com.king.chat.socket.ui.adapter.FaceSourceDownAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.FileUtil;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.SDCardUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceSourceDownActivity extends BaseUIActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.listview)
    ListView listview;

    FaceSourceDownAdapter adapter;

    private Handler handler = new Handler();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FaceSourceDownActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_source_down);
        ButterKnife.bind(this);
        initActionBar();
        initView();
        gifZipFilesTask();
    }

    private void initActionBar() {
        actionBar.setFillStatusBar(true);
        actionBar.setTitle("动图");
        actionBar.setIvBackVisiable(View.VISIBLE);
    }

    private void initView() {
        adapter = new FaceSourceDownAdapter(this);
        listview.setAdapter(adapter);
        adapter.setCallBack(new FaceSourceDownAdapter.CallBack() {
            @Override
            public void downLoadGifZip(TextView tv_down, String url) {
                downLoadGifSource(tv_down,url);
            }
        });
    }

    private void gifZipFilesTask() {
        Map<String, String> params = new HashMap<>();
        params.put("id", ""+UserInfoManager.getInstance().getAccountId());
        showProgreessDialog();
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_GIF_ZIP_FILES, params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        String data = baseTaskBean.getData();
                        List<FaceSourceBean> list = JSONObject.parseArray(data,FaceSourceBean.class);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dismissProgressDialog();
                }
            }
        });
    }

    private void downLoadGifSource(final TextView tv_down, String gifZipUrlPath){
        final String destDir = SDCardUtil.getDiskCacheDir(this,"gif");
        OkHttpClientManager.downloadAsyn(gifZipUrlPath, destDir, new OkHttpClientManager.StringProgressCallback() {
            @Override
            public void onProgress(float progress) {
                int progressInt = (int) (progress * 100);
                tv_down.setText(progressInt+"%");
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e("biaoqing","onFailure biaoqing = "+e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Logger.e("biaoqing","onResponse biaoqing = "+response);
                if (!TextUtils.isEmpty(response)){
                    try {
                        File file = new File(response);
                        if (file.exists()){
                            boolean isUnZip = FileUtil.getInstance().unZip(file,destDir);
                            if (isUnZip){
                                file.delete();
                            } else {
                                file.delete();
                            }
                            adapter.notifyDataSetChanged();
                        }
                        BroadCastUtil.sendActionBroadCast(FaceSourceDownActivity.this,BroadCastUtil.ACTION_GIF_UPDATE);
                    } catch (Exception e){
                        e.printStackTrace();
                        tv_down.setText("下载");
                    }
                }
            }
        });
    }
}
