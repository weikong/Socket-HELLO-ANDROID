package com.king.chat.socket.ui.activity.ChooseImages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.FileItem;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.activity.camera.CameraActivity;
import com.king.chat.socket.ui.adapter.ChooseImagesAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.AppManager;
import com.king.chat.socket.util.MediaUtil;
import com.king.chat.socket.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseImagesActivity extends BaseDataActivity {

    private int actionCode = 0;
    private CommonActionBar actionBar;
    private GridView gridView;
    private ChooseImagesAdapter adapter;
    private int ChooseCount = 9;

    private boolean isMultiple = true; //是否支持多选

    public static void startActivityResult(Activity activity, int requestCode, int actionCode, int num, boolean isMultiple) {
        Intent intent = new Intent(activity, ChooseImagesActivity.class);
        intent.putExtra("num", num);
        intent.putExtra("isMultiple", isMultiple);
        intent.putExtra("actionCode", actionCode);
        activity.startActivityForResult(intent, REQUEST_ALBUM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images);
        int num = getIntent().getIntExtra("num", 0);
        isMultiple = getIntent().getBooleanExtra("isMultiple", isMultiple);
        actionCode = getIntent().getIntExtra("actionCode", 0);
        ChooseCount = ChooseCount - num;
        initActionBar();
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Log.e(TAG, "start = " + start);
                handler.sendEmptyMessage(2);
//                initLocalVideos();
                initLocalImages();
                handler.sendEmptyMessageDelayed(3, 50);
                Log.e(TAG, "耗时 = " + (System.currentTimeMillis() - start) + "  数量 = " + list.size());
            }
        }).start();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setIvBackVisiable(View.VISIBLE);
        actionBar.setRightToolVisiable(View.VISIBLE, "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<FileItem> chooseItems = adapter.getChooseItems();
                if (chooseItems != null && chooseItems.size() > 0) {
                    AppManager.getInstance().killActivity(CameraActivity.class);
                    int type = 0;
                    if (chooseItems.size() == 1 && chooseItems.get(0).getFileType().contains("video"))
                        type = 1;
                    if (actionCode > 0) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("images", chooseItems);
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showGridImgsView(list);
                    break;
                case 2:
                    showProgreessDialog();
                    break;
                case 3:
                    dismissProgressDialog();
                    break;
            }
        }
    };

    private List<FileItem> list = null;

    private void initLocalImages() {
        List<FileItem> images = MediaUtil.getAllImageVideo(this);
        if (images != null) {
            if (list == null)
                list = new ArrayList<>();
            list.addAll(images);
        }
//        Collections.sort(list, new CompareOrderUtil());
        Log.e(TAG, "initLocalImages size = " + list.size());
        handler.sendEmptyMessage(1);
    }

    private void initLocalVideos() {
//        List<FileItem> list = MediaUtil.getAllVideoImages(this);
//        Log.e(TAG, "initLocalVideos size = " + list.size());
//        List<FileItem> list2 = MediaUtil.getAllVideo(this);
//        Log.e(TAG, "initLocalVideos2 size = " + list2.size());
        List<FileItem> videos = MediaUtil.getAllVideos(this);
        if (videos != null) {
            if (list == null)
                list = new ArrayList<>();
            list.addAll(videos);
        }
        Log.e(TAG, "initLocalVideos3 size = " + list.size());
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ChooseImagesAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setImageListener(new ChooseImagesAdapter.ImageListenrt() {
            @Override
            public void doShowImage(int position, String url) {
                FileItem item = adapter.getItem(position);
//                if (!TextUtils.isEmpty(url) && url.endsWith(".mp4")) {
//                    ArrayList<FileItem> list = new ArrayList<>();
//                    list.add(item);
//                    ShowMediaPagerActivity.startActivity(ChooseImagesActivity.this, list, 0);
//                } else {
//                    ShowImagesActivity.startShowImageActivity(ChooseImagesActivity.this, url, "", 0);
//                }
            }

            @Override
            public void doChooseImage(int position, String url) {
                FileItem item = adapter.getItem(position);
                if (!isMultiple) {
                    ArrayList<String> imageList = new ArrayList<String>();
                    if (item != null && !TextUtils.isEmpty(item.getFilePath()))
                        imageList.add(item.getFilePath());
                    Intent intent = new Intent();
                    intent.putExtra("images", (Serializable) imageList);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                if (!item.isCheck()) {
                    if (adapter.getChooseItems().size() >= ChooseCount) {
                        ToastUtil.show("最多选择9张图片");
                        return;
                    }
                    if (adapter.getChooseItems().size() > 0 && item.getFileType().contains("video")) {
                        ToastUtil.show("图片中不能添加视频");
                        return;
                    }
                    if (ChooseCount < 9 && item.getFileType().contains("video")) {
                        ToastUtil.show("图片中不能添加视频");
                        return;
                    }
                    if (adapter.getChooseItems().size() > 0) {
                        boolean hasVideo = false;
                        for (FileItem fileItem : adapter.getChooseItems()) {
                            if (fileItem.getFileType().contains("video")) {
                                hasVideo = true;
                                break;
                            }
                        }
                        if (hasVideo) {
                            ToastUtil.show("只能发布一个视频");
                            return;
                        }
                    }
                    adapter.getChooseItems().add(item);
                } else {
                    adapter.getChooseItems().remove(item);
                }
                item.setCheck(!item.isCheck());
                adapter.notifyDataSetChanged();
            }
        });
        showGridImgsView(null);
    }

    private void showGridImgsView(List<FileItem> images) {
        adapter.setData(images);
        adapter.notifyDataSetChanged();
    }
}
