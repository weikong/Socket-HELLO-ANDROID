package com.king.chat.socket.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by xinzhendi-031 on 2017/9/26.
 */
public class AnimUtil {

    public static void startAnimOpen(View view, float distance, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, distance); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void startAnimClose(View view, float distance, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -distance, 0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void alphaAnimVisible(View view, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void alphaAnimInVisible(View view, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void scaleAnim(View view, long delay, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.1f, 1.0f); // 初始化动画，设置各个参数
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.1f, 1.0f); // 初始化动画，设置各个参数
        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大
        set.play(scaleX).with(scaleY);
        //都设置3s，也可以为每个单独设置
        set.setDuration(duration);// 设置动画持续时间
        set.setStartDelay(delay);// 设置动画延时播放
        set.start();// 开始运行动画
    }

    @SuppressLint("WrongConstant")
    public static AnimatorSet scaleAnim(View view, float start, float end, long delay, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", start, end); // 初始化动画，设置各个参数
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", start, end); // 初始化动画，设置各个参数
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f); // 初始化动画，设置各个参数
        scaleX.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        scaleX.setRepeatMode(ValueAnimator.INFINITE);//
        scaleY.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        scaleY.setRepeatMode(ValueAnimator.INFINITE);//
        alpha.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        alpha.setRepeatMode(ValueAnimator.INFINITE);//
        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大
        set.play(scaleX).with(scaleY).with(alpha);
        //都设置3s，也可以为每个单独设置
        set.setDuration(duration);// 设置动画持续时间
        set.setStartDelay(delay);// 设置动画延时播放
        set.start();// 开始运行动画
        return set;
    }

    public static void circularProgressAnimZero(View view, float startProgress, float endProgress, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", startProgress, endProgress); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void circularProgressAnim(View view, float startProgress, float endProgress, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", startProgress, 0, endProgress); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    /**
     * 输入框错误闪烁提示
     */
    public static void animInputError(View view, long delay, long duration) {
        if (duration == -1)
            duration = 3500;
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f); // 初始化动画，设置各个参数
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    @SuppressLint("WrongConstant")
    public static ObjectAnimator rotateAnim(View view, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0.0f, 360.0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        animator.setRepeatMode(ValueAnimator.INFINITE);//
        animator.start(); // 开始运行动画
        return animator;
    }

//    public static void changeHeightAnim(final View view, int startHeight, int endHeight, long delay, long duration) {
////        int height = view.getHeight();
//        ValueAnimator scaleY = ValueAnimator.ofInt(startHeight, endHeight); ////第二个高度 需要注意一下, 因为view默认是GONE  无法直接获取高度
//        scaleY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animatorValue = Integer.valueOf(animation.getAnimatedValue() + "");
//                Logger.e("changeHeightAnim", "animatorValue = " + animatorValue);
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
//                params.height = animatorValue;
//                view.setLayoutParams(params);
//            }
//        });
//        scaleY.setTarget(view);
//        scaleY.setDuration(duration);
//        scaleY.setStartDelay(delay);
//        scaleY.start();
//    }
}
