package com.king.chat.socket.ui.view.popup;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.king.chat.socket.App;
import com.king.chat.socket.util.DisplayUtil;


/**
 * CustomPopupWindow.java - 自定义PopupWindow（显示点击区域外关闭和响应Back键关闭 ）
 *
 * @author Kevin.Zhang
 *         <p>
 *         2014-4-16 下午1:53:37
 */
public class CustomPopupWindow extends PopupWindow {

    private View mPopupView;

    public CustomPopupWindow(View contentView) {
        this(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
    }

    CustomPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        mPopupView = contentView;
    }

    /**
     * 点击Back键关闭PopupWindow
     */
    public void enableKeyBackDismiss() {
        if (null != mPopupView) {
            mPopupView.setFocusableInTouchMode(true);
            mPopupView.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                        if (isShowing()) {
                            dismiss();
                        }
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 点击外部区域关闭PopupWindow
     */
    public void enablOutsideClickDismiss() {
        if (null != mPopupView) {
            mPopupView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowing()) {
                        dismiss();
                    }
                }
            });
        }
    }

    public void showDropDown(View parentView, View contentView, int count) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
        if (location[1] > DisplayUtil.screenHeight / 2){
            showUp(parentView,contentView,count);
        } else {
            showAsDropDown(parentView);
        }
    }

    /**
     * 显示下拉的PopupWindow
     *
     * @param parentView
     */
    @Override
    public void showAsDropDown(View parentView) {
        if (Build.VERSION.SDK_INT < 24) {
//            this.showAsDropDown(parentView, 0, 1);
            super.showAsDropDown(parentView);
        } else {
            Rect visibleFrame = new Rect();
            parentView.getGlobalVisibleRect(visibleFrame);
            int height = parentView.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            this.setHeight(height);
//            this.showAsDropDown(parentView, 0, 0);
            super.showAsDropDown(parentView);
        }
    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     * @param v
     */
    public void showUp(View v, View contentView, int count) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
//        showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - popupWidth / 2, location[1] - popupHeight);
//        int h = contentView.getMeasuredHeight();
//        if (h <= 0){
//            h = DisplayUtil.dp2px(180);
//        }
//        int h = DisplayUtil.dp2px(40) * count;
        int h2 = DisplayUtil.dp2px(180);
//        if (h > h2){
//            h = h2;
//        }
        showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1] - h2 - DisplayUtil.dp2px(10));
    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     * @param v
     */
    public void showUp(View v,View popView) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        if (popView != null){
            if (popView.getHeight() > 0){
                showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1] - popView.getHeight()-DisplayUtil.dp2px(10));
            } else {
                showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1]-DisplayUtil.dp2px(50));
            }
        }
    }

    /**
     * 设置显示在v上方（以v的中心位置为开始位置）
     * @param v
     */
    public void showUp2(View v,View popView) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popView.getWidth() / 2, location[1] - popView.getHeight());
    }

    /**
     * 显示全屏的PopupWindow
     *
     * @param parentView
     */
    public void showFullScreenPopupWindow(View parentView) {
        this.setClippingEnabled(false);
        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
//        Rect visibleFrame = new Rect();
//        parentView.getGlobalVisibleRect(visibleFrame);
        int navHeight = DisplayUtil.getBottomStatusHeight(parentView.getContext());
        this.showAtLocation(parentView, Gravity.BOTTOM, 0, navHeight);
    }

    /**
     * 获取手机屏幕高度
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕真实高度（包括虚拟键盘）
     *
     */
    public static int getRealHeight() {
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }
}
