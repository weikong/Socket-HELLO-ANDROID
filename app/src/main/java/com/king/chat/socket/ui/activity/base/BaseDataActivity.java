package com.king.chat.socket.ui.activity.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;


import com.king.chat.socket.App;
import com.king.chat.socket.ui.view.dialog.ProgressDialogMyBg;
import com.king.chat.socket.util.AppManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseDataActivity extends FragmentActivity {

    public static final int INTENT_REQUEST_PHOTO = 101;
    public static final int REQUEST_ALBUM = 102;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    /**
     * 调试代码TAG
     */
    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
        AppManager.getInstance().killActivity(this);
    }


    @TargetApi(23)
    protected void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            // 读写权限
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
//            // 读写权限
//            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
//            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allGranted = true;
        for (int result : grantResults){
            if (result != PackageManager.PERMISSION_GRANTED){
                allGranted = false;
            }
        }
        resultPermissions(allGranted);
    }

    public void resultPermissions(boolean result){

    }


    protected String[] permissionLocation =  new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    protected String[] permissionCamera =  new String[]{Manifest.permission.CAMERA};
    protected String[] permissionStorage =  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected String[] permissionStorageCamera =  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    protected String[] permissionAll =  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    public boolean checkInitPermissions(String[] permission) {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(permission);
        return isAllGranted;
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    public static final int PERMISSION_REQUEST_CODE = 101;


    /**
     * 点击按钮，将通讯录备份保存到外部存储器备。
     *
     * 需要3个权限(都是危险权限):
     *      1. 读取通讯录权限;
     *      2. 读取外部存储器权限;
     *      3. 写入外部存储器权限.
     */
    public void setExternalStoragePermissions(String[] permission){
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(permission);
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                permission,
                PERMISSION_REQUEST_CODE
        );
    }

    private ProgressDialogMyBg pDialog;

    /**
     * 显示等待对话框 当点击返回键取消对话框并停留在该界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showProgreessDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialogMyBg(this);
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

    protected void intent2Activity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    protected void intent2Activity(Class c, Bundle b) {
        Intent intent = new Intent(this, c);
        intent.putExtra("DATA", b);
        startActivity(intent);
    }

    protected void intent2Activity(Class c, Object o) {
        Intent intent = new Intent(this, c);
        intent.putExtra("DATA", (Serializable) o);
        startActivity(intent);
    }

    protected void intentForResult2Activity(Class c, int requestCode) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, requestCode);
    }

    protected void intentForResult2Activity(Class c, int requestCode, boolean choose) {
        Intent intent = new Intent(this, c);
        intent.putExtra("DATA", choose);
        startActivityForResult(intent, requestCode);
    }
}
