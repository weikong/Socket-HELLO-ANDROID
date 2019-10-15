/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.util;

import android.annotation.SuppressLint;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
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
        return requestOptions;
    }

    public static RequestOptions optionsGrayItem() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_item_gray);
        requestOptions.centerCrop();
        return requestOptions;
    }
}
