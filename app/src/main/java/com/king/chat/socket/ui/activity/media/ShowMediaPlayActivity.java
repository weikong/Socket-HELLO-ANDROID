package com.king.chat.socket.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.activity.base.BaseUIActivity;
import com.king.chat.socket.ui.fragment.media.ShowImageFragment;
import com.king.chat.socket.ui.fragment.media.ShowVideoFragment;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.DisplayUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMediaPlayActivity extends BaseUIActivity {

    @BindView(R.id.action_bar)
    CommonActionBar action_bar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    List<ChatRecordData> list;
    ChatRecordData chatRecordData;
    private int position = 0;

    public static void startActivity(Context context,ChatRecordData chatRecordData) {
        Intent intent = new Intent(context, ShowMediaPlayActivity.class);
        intent.putExtra("DATA",chatRecordData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_media_play);
        ButterKnife.bind(this);
        loadData();
        initView();
    }

    private void initView(){
        action_bar.setFillStatusBar(true);
        action_bar.setBackgroundAlpha(true);
        action_bar.setIvBackVisiable(View.VISIBLE);
        viewPager.setPageMargin(DisplayUtil.dp2px(6));
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(adapter);
        if (list != null && position <= (list.size() - 1))
            viewPager.setCurrentItem(position,false);
    }

    private void loadData(){
        chatRecordData = (ChatRecordData) getIntent().getSerializableExtra("DATA");
        if (chatRecordData != null && chatRecordData.getGroupdata() == 1){
            list = DBChatRecordImpl.getInstance().queryGroupChatRecordImageAndVideo();
        } else {
            list = DBChatRecordImpl.getInstance().queryChatRecordImageAndVideo();
        }
        if (chatRecordData == null || list == null || list.size() == 0)
            return;
        for (int i=list.size()-1;i>=0;i--){
            ChatRecordData item = list.get(i);
            if (item != null && item.getMessageid().equals(chatRecordData.getMessageid())){
                position = i;
                break;
            }
        }
    }

    /**
     * 初始化FragmentPagerAdapter
     */
    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            ChatRecordData chatRecordData = list.get(position);
            switch (chatRecordData.getMessagechattype()) {
                case MessageChatType.TYPE_IMG:
                    return ShowImageFragment.newInstance(chatRecordData);
                case MessageChatType.TYPE_VIDEO:
                    return ShowVideoFragment.newInstance(chatRecordData);
                default:
                    return ShowImageFragment.newInstance(chatRecordData);
            }
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            ChatRecordData chatRecordData = list.get(position);
            switch (chatRecordData.getMessagechattype()) {
                case MessageChatType.TYPE_IMG:
                    break;
                case MessageChatType.TYPE_VIDEO:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
