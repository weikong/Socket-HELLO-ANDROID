
package com.king.chat.socket.ui.view.ImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.king.chat.socket.R;
import com.king.chat.socket.util.DisplayUtil;


/**
 * 实现圆角image
 *
 * @author Administrator
 */
@SuppressLint("AppCompatCustomView")
public class RoundAngleImageView extends ImageView {

    private Paint paint;
    /**
     * 个人理解是
     * <p>
     * 这两个都是画圆的半径
     */
    private int roundWidth = 10;
    private int roundHeight = 10;
    private Paint paint2;
    private boolean roundLeftTop = true, roundRightTop = true, roundLeftBottom = true, roundRightBottom = true;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = null;
        if (attrs != null) {
            a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            roundWidth = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, roundWidth);
            roundHeight = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, roundHeight);
            roundLeftTop = a.getBoolean(R.styleable.RoundAngleImageView_roundLeftTop, roundLeftTop);
            roundRightTop = a.getBoolean(R.styleable.RoundAngleImageView_roundRightTop, roundRightTop);
            roundLeftBottom = a.getBoolean(R.styleable.RoundAngleImageView_roundLeftBottom, roundLeftBottom);
            roundRightBottom = a.getBoolean(R.styleable.RoundAngleImageView_roundRightBottom, roundRightBottom);
            Log.e("RoundAngleImageView", "  roundWidth = " + roundWidth);
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = DisplayUtil.dp2px(roundWidth);
            roundHeight = DisplayUtil.dp2px(roundHeight);
            Log.e("RoundAngleImageView", "density = " + density + "  roundWidth = " + roundWidth);
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
        if (a != null)
            a.recycle();
    }

    public void setRoundWidthHeight(int roundWidth, int roundHeight) {
        this.roundWidth = roundWidth;
        this.roundHeight = roundHeight;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        if (roundLeftTop)
            drawLiftUp(canvas2);
        if (roundLeftBottom)
            drawLiftDown(canvas2);
        if (roundRightTop)
            drawRightUp(canvas2);
        if (roundRightBottom)
            drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(0, getHeight() - roundHeight * 2, roundWidth * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeight);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight() - roundHeight * 2, getWidth(), getHeight()), -0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundWidth, 0);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(), 0 + roundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

}