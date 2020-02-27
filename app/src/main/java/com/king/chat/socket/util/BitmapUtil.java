package com.king.chat.socket.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.chat.socket.bean.CombineBitmapEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/10/16.
 */

public class BitmapUtil {

    private static class BitmapUtilHolder {
        private static final BitmapUtil INSTANCE = new BitmapUtil();
    }

    /**
     * 单一实例
     */
    public static final BitmapUtil getInstance() {
        return BitmapUtil.BitmapUtilHolder.INSTANCE;
    }

//    public Bitmap merge(List<Bitmap> bitmapArray, Context context, ImageView imageView) {
//        // 画布的宽
//        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
//        int tempWidth;
//        int tempHeight;
//        if (lp != null) {
//            tempWidth = DisplayUtil.dp2px(lp.width);
//            tempHeight = DisplayUtil.dp2px(lp.height);
//        } else {
//            //否则给一个默认的高度
//            tempWidth = DisplayUtil.dp2px(70);
//            tempHeight = DisplayUtil.dp2px(70);
//        }
//        return combimeBitmap(context, tempWidth, tempHeight, bitmapArray);
//    }

    public Bitmap combimeBitmap(Context context, int combineWidth, int combineHeight, List<Bitmap> bitmaps) {
        if (bitmaps == null || bitmaps.size() == 0)
            return null;
        if (bitmaps.size() >= 9) {
            bitmaps = bitmaps.subList(0, 9);
        }
        Bitmap resultBitmap = null;
        int len = bitmaps.size();
        // 绘制数据，这里记录所有的绘制坐标。
        List<CombineBitmapEntity> combineBitmapEntities = CombineNineRect.getInstance().generateCombineBitmapEntity(len);
        // 缩略图
        List<Bitmap> thumbnailBitmaps = new ArrayList<Bitmap>();
        for (int i = 0; i < len; i++) {
            thumbnailBitmaps.add(ThumbnailUtils.extractThumbnail(bitmaps.get(i),
                    (int) combineBitmapEntities.get(i).width,
                    (int) combineBitmapEntities.get(i).height));
        }
        // 合成
        resultBitmap = getCombineBitmaps(combineBitmapEntities,thumbnailBitmaps, combineWidth, combineHeight);
        return resultBitmap;
    }

    private Bitmap getCombineBitmaps(List<CombineBitmapEntity> mEntityList, List<Bitmap> bitmaps, int width, int height) {
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < mEntityList.size(); i++) {
            //合并图像
            newBitmap = mixtureBitmap(newBitmap, bitmaps.get(i), new PointF(mEntityList.get(i).x, mEntityList.get(i).y));
        }
        return newBitmap;
    }

    private Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
//        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.save();
        cv.restore();
        if (first != null) {
            first.recycle();
            first = null;
        }
        if (second != null) {
            second.recycle();
            second = null;
        }
        return newBitmap;
    }
}
