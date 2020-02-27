package com.king.chat.socket.ui.view.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.view.dialog.ProgressMyDialog;
import com.king.chat.socket.ui.view.toast.CustomToast;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.SDCardUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Title: ControlWindow.java
 * @Package com.melink.android.ui.activity._chat.widget
 * @Description
 * @date 2015-7-17 下午3:51:26
 * @author shun
 * @version V1.0
 */
public class CustomChatPopWindow extends PopupWindow implements View.OnClickListener {

	private static final int WIN_HEIGHT = 40;

	private Context mContext;
	@BindView(R.id.layout_root)
	LinearLayout layout_root;
	@BindView(R.id.layout_pop)
	LinearLayout layout_pop;
	@BindView(R.id.tv_copy)
	TextView tv_copy;
	@BindView(R.id.tv_forword)
	TextView tv_forword;
	@BindView(R.id.tv_save)
	TextView tv_save;
	@BindView(R.id.tv_collect)
	TextView tv_collect;
	@BindView(R.id.tv_del)
	TextView tv_del;
	private ChatRecordData bean;


	public CustomChatPopWindow(Context context) {
		super(context);
		this.mContext = context;
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_pop_chat, null);
		ButterKnife.bind(this, view);
		tv_copy.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		tv_forword.setOnClickListener(this);
		tv_del.setOnClickListener(this);
		tv_collect.setOnClickListener(this);

		setContentView(view);
		setBackgroundDrawable(new ColorDrawable());
		setOutsideTouchable(true);
		setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		setHeight(DisplayUtil.dp2px(WIN_HEIGHT));
	}

	@Override
	public void showAsDropDown(View anchor) {
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		int y = location[1];
//		int status = DisplayUtil.getStatusBarHeight(mContext);
		int actionBarHeight = DisplayUtil.dp2px(40);
		int viewWidth = anchor.getWidth();
		int viewHeight = anchor.getHeight();
		int winHeight = DisplayUtil.dp2px(WIN_HEIGHT);
//		if (y >= status + actionBarHeight + winHeight){
		if (y >= actionBarHeight + winHeight){
//			super.showAsDropDown(anchor, 0, -(anchor.getHeight() + winHeight) * 3 / 4);
			super.showAsDropDown(anchor, 0, -(anchor.getHeight() + winHeight));
		} else {
			layout_pop.setBackgroundResource(R.drawable.bg_chat_edit_down);
//			super.showAsDropDown(anchor, 0, -(anchor.getHeight()/2));
			super.showAsDropDown(anchor, 0, 0);
		}
	}

	public void setData(ChatRecordData bean) {
		this.bean = bean;
		if (bean == null)
			return;
		if (Config.userId.equalsIgnoreCase(bean.getSourcesenderid())) {
			//自己发送的消息
			layout_root.setGravity(Gravity.RIGHT);
			layout_pop.setBackgroundResource(R.drawable.bg_chat_edit_pop_right);
//			LayoutParams params = (LayoutParams) layout_pop.getLayoutParams();
//			params.rightMargin = DisplayUtil.dp2px(6);
		} else {
			//接收到的消息
			layout_root.setGravity(Gravity.LEFT);
			layout_pop.setBackgroundResource(R.drawable.bg_chat_edit);
			LayoutParams params = (LayoutParams) layout_pop.getLayoutParams();
			params.leftMargin = DisplayUtil.dp2px(6);
		}
		int messageChatType = bean.getMessagechattype();
		switch (messageChatType) {
			case MessageChatType.TYPE_TEXT:
				tv_copy.setVisibility(View.VISIBLE);
				tv_save.setVisibility(View.GONE);
				tv_collect.setVisibility(View.GONE);
				break;
			case MessageChatType.TYPE_IMG:
				tv_copy.setVisibility(View.GONE);
				tv_save.setVisibility(View.VISIBLE);
				tv_collect.setVisibility(View.VISIBLE);
				break;
			case MessageChatType.TYPE_VIDEO:
				tv_copy.setVisibility(View.GONE);
				tv_save.setVisibility(View.VISIBLE);
				tv_collect.setVisibility(View.VISIBLE);
				break;
			case MessageChatType.TYPE_VOICE:
				tv_copy.setVisibility(View.GONE);
				tv_save.setVisibility(View.GONE);
				tv_collect.setVisibility(View.VISIBLE);
				break;
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
			case R.id.tv_copy:
				boolean isCopy = DisplayUtil.copy(mContext, bean.getMessagecontent());
				CustomToast.showResultToast(mContext,isCopy);
				callBack.actionCopy(bean);
				break;
			case R.id.tv_forword:
				callBack.actionForword(bean);
				break;
			case R.id.tv_save:
				final ProgressMyDialog myDialog = loadDialog();
				OkHttpClientManager.downloadAsyn(bean.getMessagecontent(), SDCardUtil.getImgDir(), new OkHttpClientManager.StringProgressCallback() {
					@Override
					public void onProgress(float progress) {
						myDialog.setProgress((int) (progress * 100));
					}

					@Override
					public void onFailure(Request request, IOException e) {
						myDialog.setProgressState(false);
					}

					@Override
					public void onResponse(String response) {
						myDialog.setProgressState(true);
					}
				});
				callBack.actionSave(bean);
				break;
			case R.id.tv_collect:
				callBack.actionCollect(bean);
				break;
			case R.id.tv_del:
				callBack.actionDel(bean);
				break;
		}
	}

	CallBack callBack;

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		void actionCopy(ChatRecordData bean);

		void actionForword(ChatRecordData bean);

		void actionSave(ChatRecordData bean);

		void actionCollect(ChatRecordData bean);

		void actionDel(ChatRecordData bean);
	}

	private ProgressMyDialog loadDialog() {
		ProgressMyDialog myDialog = new ProgressMyDialog(mContext);
		myDialog.show();
		return myDialog;
	}
}
