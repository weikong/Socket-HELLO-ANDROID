package com.king.chat.socket.ui.view.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;

/**
 * @author wy
 * @version V1.0
 * @Title: ForwardItemView.java
 * @Package com.melink.android.ui.widget
 * @Description: TODO
 * @date 2015-5-13 下午8:20:24
 */
public class ForwardItemView extends RelativeLayout {

    private RelativeLayout layout_content;
    private TextView mtv_title;
    private TextView mtv_content;
    private String title_text;
    private int title_textcolor;
    private float title_textsize;
    private String content_text;
    private int content_textcolor;
    private float content_textsize;

    public ForwardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public ForwardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForwardItemView);

        title_text = a.getString(R.styleable.ForwardItemView_title_text);
        title_textcolor = a.getColor(R.styleable.ForwardItemView_title_textColor, Color.BLACK);
        title_textsize = a.getDimensionPixelSize(R.styleable.ForwardItemView_title_textSize, 16);

        content_text = a.getString(R.styleable.ForwardItemView_content_text);
        content_textcolor = a.getColor(R.styleable.ForwardItemView_content_textColor, Color.BLACK);
        content_textsize = a.getDimensionPixelSize(R.styleable.ForwardItemView_content_textSize, 16);

        a.recycle();
        initView(context);
    }

    public ForwardItemView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View rootLayout = LayoutInflater.from(context).inflate(R.layout.view_forward_item, this);
        layout_content = (RelativeLayout) rootLayout.findViewById(R.id.layout_content);
        mtv_title = (TextView) rootLayout.findViewById(R.id.tv_title);
        mtv_content = (TextView) rootLayout.findViewById(R.id.tv_content);
        mtv_title.setText(title_text);
        mtv_title.setTextColor(title_textcolor);
        mtv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, title_textsize);

        mtv_content.setText(content_text);
        mtv_content.setTextColor(content_textcolor);
        mtv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, content_textsize);
    }

    public String getContent_text() {
        return mtv_content.getText().toString();
    }

    public String getTitle_text() {
        return mtv_title.getText().toString();
    }

    public void setContent_text(CharSequence content) {
        mtv_content.setText(content);
    }

    public void setTitle_text(CharSequence title) {
        mtv_title.setText(title);
    }
}
