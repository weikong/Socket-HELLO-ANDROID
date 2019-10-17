package com.king.chat.socket.ui.view.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.Expression;
import com.king.chat.socket.ui.activity.ChooseImages.ChooseImagesActivity;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.activity.camera.CameraActivity;
import com.king.chat.socket.ui.adapter.GridMoreAdapter;
import com.king.chat.socket.ui.adapter.SmileyGridAdapter;
import com.king.chat.socket.ui.view.gridview.CustomGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class SmileyGridView extends RelativeLayout {

    @BindView(R.id.gridview)
    CustomGridView gridView;
    SmileyGridAdapter adapter;

    List<Expression> expList;

    public SmileyGridView(Context context) {
        super(context);
        initView(context);
    }

    public SmileyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SmileyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_smiley_grid, this);
        ButterKnife.bind(this, view);
        adapter = new SmileyGridAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expression expression = adapter.getItem(position);
                if (callBack != null){
                    if (position == expList.size() - 1)
                        callBack.delSmiley();
                    else
                        callBack.clickSmiley(expression);
                }
            }
        });
    }

    public void loadData(List<Expression> expList){
        this.expList = expList;
        adapter.setExpList(expList);
        adapter.notifyDataSetChanged();
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void delSmiley();
        public void clickSmiley(Expression expression);
    }
}
