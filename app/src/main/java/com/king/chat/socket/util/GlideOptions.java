/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.util;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.king.chat.socket.R;

/**
 * ImageLoaderOptions.java - ImageLoader config parameters
 *
 * @author Kevin.Zhang
 *         <p>
 *         2014-2-27 下午5:17:25
 */
public class GlideOptions {

//    File file =new File(Environment.getExternalStorageDirectory().getPath(),"test.jpg");
//    //本地
//    Glide.with(this).load(R.mipmap.ic_launcher).into(imageview);
//    //assets资产目录
//    Glide.with(this).load("file:///android_asset/test.jpg").into(imageview0);
//    //sd卡
//    Glide.with(this).load("file:///storage/emulated/0/test.jpg").into(imageview1);
//    Glide.with(this).load(file).into(imageview2);
//    Glide.with(this).load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg").into(imageview3);

   /* public void loadImage(Context context, ImageView imageView){
        Glide.with(context).load(R.mipmap.ic_launcher)
                //模糊
                .bitmapTransform(new BlurTransformation(this))
                //圆角
                .bitmapTransform(new RoundedCornersTransformation(this, 24, 0, RoundedCornersTransformation.CornerType.ALL))
                //遮盖
                .bitmapTransform(new MaskTransformation(this, R.mipmap.ic_launcher))
                //灰度
                .bitmapTransform(new GrayscaleTransformation(this))
                //圆形
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imageView);
    }*/

    public static RequestOptions optionDefaultHeader() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_user_defaultheader);
        requestOptions.circleCrop();
        return requestOptions;
    }

    public static RequestOptions optionDefaultHeader2() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionDefaultHeader3() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_change_herd_press);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionDefaultMineHeader() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon_cat);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionDefaultHeader4() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon_default_user);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionDefaultGroup() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.notice_icon_group);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionDefaultFriend() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.notice_icon_friend);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionsRoundedCorners() {
        RequestOptions requestOptions = new RequestOptions().transform(new GlideRoundTransform(6)).override(200,200);
        requestOptions.placeholder(R.mipmap.ic_launcher);
        return requestOptions;
    }

    public static RequestOptions optionsRoundedCorners(int radius) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.transform(new RoundedCorners(DisplayUtil.dp2px(radius)));
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionsGrayLight() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_item_gray_light);
        requestOptions.transform(new RoundedCorners(DisplayUtil.dp2px(10)));
        return requestOptions;
    }

    public static RequestOptions optionsTransparentCenterCrop() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_item_transparent);
        requestOptions.centerCrop();
        return requestOptions;
    }

    public static RequestOptions optionsTransparent() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_item_transparent);
        requestOptions.fitCenter();
        return requestOptions;
    }

    public static RequestOptions optionsGrayItem() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_item_gray);
        requestOptions.centerCrop();
        return requestOptions;
    }
}
