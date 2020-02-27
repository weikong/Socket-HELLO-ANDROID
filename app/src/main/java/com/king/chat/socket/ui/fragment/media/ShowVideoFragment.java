package com.king.chat.socket.ui.fragment.media;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.util.AnimUtil;
import com.king.chat.socket.util.GlideOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ShowVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowVideoFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.videoview)
    VideoView videoview;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    ChatRecordData chatRecordData;
    private boolean videoPrepared = false;

    private Handler handler = new Handler();


    public ShowVideoFragment() {
        // Required empty public constructor
    }

    public static ShowVideoFragment newInstance(ChatRecordData chatRecordData) {
        ShowVideoFragment fragment = new ShowVideoFragment();
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
        View view = inflater.inflate(R.layout.fragment_show_videoview, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        videoPrepared = false;
        if (videoview != null) {
            videoview.suspend();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && videoview != null && videoview.isPlaying()){
            pauseVideo();
        }
    }

    private void initView() {
        if (chatRecordData != null){
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoPrepared = true;
                    if (iv_image.getAlpha() > 0){
                        AnimUtil.alphaAnimInVisible(iv_image,0,100);
                    }
                    progressbar.setVisibility(View.INVISIBLE);
                }
            });
            videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    iv_play.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.INVISIBLE);
                    return false;
                }
            });
            videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    iv_play.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.INVISIBLE);
                }
            });
//            videoview.setVideoPath(chatRecordData.getMessagecontent());
            GlideApp.with(getActivity()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(chatRecordData.getMessagecontent()).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(iv_image);
        }
    }

    @OnClick({R.id.iv_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                playVideo();
                break;
        }
    }

    private void playVideo(){
        if (!videoPrepared){
            progressbar.setVisibility(View.VISIBLE);
            videoview.setVideoPath(chatRecordData.getMessagecontent());
        }
        if (videoview != null && !videoview.isPlaying()) {
            videoview.start();
            iv_play.setVisibility(View.INVISIBLE);
            if (videoPrepared && iv_image.getAlpha() > 0){
                AnimUtil.alphaAnimInVisible(iv_image,0,100);
            }
        }
    }

    private void pauseVideo(){
        if (videoview != null && videoview.isPlaying()) {
            videoview.pause();
            iv_play.setVisibility(View.VISIBLE);
        }
    }
}
