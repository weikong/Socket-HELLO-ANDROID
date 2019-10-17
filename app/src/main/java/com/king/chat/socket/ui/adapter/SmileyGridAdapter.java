package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.Expression;
import com.king.chat.socket.util.ExpressionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class SmileyGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<Expression> expList = new ArrayList<>();

    public SmileyGridAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public List<Expression> getExpList() {
        return expList;
    }

    public void setExpList(List<Expression> expList) {
        this.expList.clear();
        if (expList != null && expList.size() > 0)
            this.expList.addAll(expList);
    }

    @Override
    public int getCount() {
        return expList == null ? 0 : expList.size();
    }

    @Override
    public Expression getItem(int position) {
        return expList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_smiley_grid, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Expression expression = getItem(position);
        ExpressionHelper.getInstance().loadSmilyFile(mContext,expression.getFileName(),viewHolder.iv_smiley);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_smiley;

        public ViewHolder(View view) {
            this.iv_smiley = (ImageView) view.findViewById(R.id.iv_smiley);
        }
    }
}
