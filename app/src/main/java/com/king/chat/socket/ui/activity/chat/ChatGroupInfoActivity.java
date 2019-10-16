package com.king.chat.socket.ui.activity.chat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.adapter.ChatGroupInfoAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.ui.view.chat.group.InfoItemView;
import com.king.chat.socket.ui.view.chat.group.MembersGridView;
import com.king.chat.socket.ui.view.chat.group.RedTextItemView;
import com.king.chat.socket.util.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatGroupInfoActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.layout_content)
    LinearLayout layout_content;

    MembersGridView membersGridView;
    InfoItemView groupNameView;
    InfoItemView groupQRView;
    InfoItemView groupPublicView;
    InfoItemView groupChatHistoryView;
    InfoItemView groupMianDaRaoView;
    InfoItemView groupZhiDingView;
    InfoItemView groupNickNameView;
    InfoItemView groupBackgroundView;

    RedTextItemView clearHistoryView;
    RedTextItemView delGroupView;

    private GroupInfo groupInfo;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_info);
        ButterKnife.bind(this);
        groupInfo = (GroupInfo) getIntent().getSerializableExtra("DATA");
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar.setTitle("群聊信息（" + groupInfo.getMembers().size() + "）");
        actionBar.setIvBackVisiable(View.VISIBLE);
    }

    private void initView() {
        membersGridView = new MembersGridView(this);
        membersGridView.loadData(groupInfo);
        layout_content.addView(membersGridView);
        //群聊名称
        groupNameView = new InfoItemView(this);
        groupNameView.loadData("群聊名称", groupInfo.getGroupname(), false);
        layout_content.addView(groupNameView);
        marginTop(groupNameView, 10);
        //群二维码
        groupQRView = new InfoItemView(this);
        groupQRView.loadData("群二维码", "", false);
        layout_content.addView(groupQRView);
        marginTop(groupQRView, 1);
        //聊天历史
        groupChatHistoryView = new InfoItemView(this);
        groupChatHistoryView.loadData("查找聊天内容", "", false);
        layout_content.addView(groupChatHistoryView);
        marginTop(groupChatHistoryView, 1);
        //消息免打扰
        groupMianDaRaoView = new InfoItemView(this);
        groupMianDaRaoView.loadData("消息免打扰", "", true);
        layout_content.addView(groupMianDaRaoView);
        marginTop(groupMianDaRaoView, 10);
        //置顶聊天
        groupZhiDingView = new InfoItemView(this);
        groupZhiDingView.loadData("置顶聊天", "", true);
        layout_content.addView(groupZhiDingView);
        marginTop(groupZhiDingView, 1);
        //我在本群的昵称
        groupNickNameView = new InfoItemView(this);
        groupNickNameView.loadData("我在本群的昵称", Config.userName, false);
        layout_content.addView(groupNickNameView);
        marginTop(groupNickNameView, 10);
        //设置当前聊天背景
        groupBackgroundView = new InfoItemView(this);
        groupBackgroundView.loadData("设置当前聊天背景", "", false);
        layout_content.addView(groupBackgroundView);
        marginTop(groupBackgroundView, 1);

        //清空聊天记录
        clearHistoryView = new RedTextItemView(this);
        clearHistoryView.loadData("清空聊天记录");
        layout_content.addView(clearHistoryView);
        marginTop(clearHistoryView, 20);
        //删除并退出
        delGroupView = new RedTextItemView(this);
        delGroupView.loadData("删除并退出");
        layout_content.addView(delGroupView);
        marginTop(delGroupView, 1);
        marginBottom(delGroupView, 40);
    }

    public void marginTop(View view, int top) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = DisplayUtil.dp2px(top);
    }

    public void marginBottom(View view, int bottom) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.bottomMargin = DisplayUtil.dp2px(bottom);
    }
}
