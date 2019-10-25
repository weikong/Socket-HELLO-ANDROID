package com.king.chat.socket.ui.fragment.media;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.GlideOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ShowImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowImageFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.iv_image)
    ImageView iv_image;
    ChatRecordData chatRecordData;

    private Handler handler = new Handler();


    public ShowImageFragment() {
        // Required empty public constructor
    }

    public static ShowImageFragment newInstance(ChatRecordData chatRecordData) {
        ShowImageFragment fragment = new ShowImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, chatRecordData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.chatRecordData = (ChatRecordData) getArguments().get(ARG_PARAM1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_image, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        if (chatRecordData != null){
            String url = chatRecordData.getMessagecontent();
            if (!TextUtils.isEmpty(url) && url.toLowerCase().endsWith("gif"))
                GlideApp.with(getActivity()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(chatRecordData.getMessagecontent()).into(iv_image);
            else {
                GlideApp.with(getActivity()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(chatRecordData.getMessagecontent()).into(iv_image);
            }
        }
    }
}
