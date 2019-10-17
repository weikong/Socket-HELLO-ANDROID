package com.king.chat.socket.ui.view.chat.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.adapter.GridMembersAdapter;
import com.king.chat.socket.ui.view.gridview.CustomGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class MembersGridView extends RelativeLayout {

    @BindView(R.id.grid_members)
    CustomGridView grid_members;
    @BindView(R.id.tv_more_members)
    TextView tv_more_members;

    GridMembersAdapter adapter;

    GroupInfo groupInfo;

    public MembersGridView(Context context) {
        super(context);
        initView(context);
    }

    public MembersGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MembersGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_members_grid, this);
        ButterKnife.bind(this,view);
        adapter = new GridMembersAdapter(context);
        grid_members.setAdapter(adapter);
    }

    public void loadData(GroupInfo groupInfo){
        this.groupInfo = groupInfo;
        if (groupInfo == null)
            return;
        List<ContactBean> members = groupInfo.getMembers();
        List<ContactBean> membersShow = null;
        if (members.size() >= 10){
            membersShow = members.subList(0,9);
            tv_more_members.setVisibility(View.VISIBLE);
        } else {
            membersShow = members;
            tv_more_members.setVisibility(View.GONE);
        }
        ContactBean addBean = new ContactBean();
        addBean.setName("ADD");
        membersShow.add(addBean);

        adapter.setData(membersShow);
        adapter.notifyDataSetChanged();
    }
}
