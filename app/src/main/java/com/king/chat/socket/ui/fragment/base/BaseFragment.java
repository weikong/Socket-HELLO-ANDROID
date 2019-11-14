package com.king.chat.socket.ui.fragment.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.king.chat.socket.App;
import com.king.chat.socket.ui.view.dialog.LoadingDialog;
import com.king.chat.socket.util.SDCardUtil;
import com.king.chat.socket.util.TimeFormatUtils;

import java.io.File;
import java.io.Serializable;

public class BaseFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();
    protected static final int REQUEST_CAMERA = 10001;
    protected static final int REQUEST_ALBUM = 10002;
    protected String takeImagePath = "";
    protected boolean isOncreate = false;
    protected boolean isVisibleToUser = false;

    protected void intent2Activity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    protected void intent2Activity(Class c, Bundle b) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("DATA", b);
        startActivity(intent);
    }

    protected void intent2Activity(Class c, Serializable data) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("DATA", data);
        startActivity(intent);
    }

    protected CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        void actionSearch(int myTab);

        void actionAdd(int myTab);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    @Override
    public void onDestroyView() {
        isOncreate = false;
        dismissProgressDialog();
        super.onDestroyView();
    }

    public Context getContextActivity() {
        if (getActivity() != null)
            return getActivity();
        else
            return App.getInstance();
    }

    /**
     * 关闭输入法
     *
     * @param view
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打开输入法
     */
    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private LoadingDialog pDialog;

    /**
     * 显示等待对话框 当点击返回键取消对话框并停留在该界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showProgreessDialog() {
        if (pDialog == null) {
            pDialog = new LoadingDialog(getActivity());
            pDialog.setCanceledOnTouchOutside(false);
        }
        if (pDialog.isShowing())
            pDialog.dismiss();
        pDialog.show();
        pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    try {
                        dismissProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 销毁对话框
     */
    public void dismissProgressDialog() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String takeCarema() {
        takeImagePath = "";
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return "";
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeImagePath = getTakePhotoPicpath();
        File imageFile = new File(takeImagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(imageFile));
        startActivityForResult(intent, REQUEST_CAMERA);
        return takeImagePath;
    }

    protected String getTakePhotoPicpath() {
        StringBuffer sb = new StringBuffer();
        String imageName = "image_" + TimeFormatUtils.getRecordFormatDate(System.currentTimeMillis());
        sb.append(SDCardUtil.getImgDir()).append("/").append(imageName).append(".jpg");
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                takeCarema();
            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(getActivity(), "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void intent2Calendar(){
        /*Intent i = new Intent();
        ComponentName cn = null;
        if(Integer.parseInt (Build.VERSION.SDK ) >=8){
            cn = new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity");
        } else {
            cn = new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");
        }
        i.setComponent(cn);
        getActivity().startActivity(i);*/

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("content://com.android.calendar/"), "time/epoch");
            getActivity().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "暂未适配您的机型，如果想要添加日历提醒，请手动添加", Toast.LENGTH_SHORT).show();
        }
    }
}
