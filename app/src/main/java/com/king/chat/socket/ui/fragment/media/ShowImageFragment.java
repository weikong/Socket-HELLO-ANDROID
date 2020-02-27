package com.king.chat.socket.ui.fragment.media;

import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.util.GlideCacheUtil;
import com.king.chat.socket.util.GlideOptions;

import java.io.IOException;

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
    public void onDestroyView() {
        super.onDestroyView();
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
                GlideApp.with(getActivity()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(url).addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        GlideCacheUtil.getInstance().clearImageMemoryCache(getActivity());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(iv_image);
            else {
                GlideApp.with(getActivity()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(url).addListener(new RequestListener<Drawable>() {
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

//            String url = "http://video.melinked.com/6c0f4ec6-7336-41c2-b762-6fe5bb1c4e14.jpg";
//            OkHttpClientManager.downloadAsyn(url, SDCardUtil.getImgDir(), new OkHttpClientManager.StringCallback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(String response) {
//                    int degress = readPictureDegree(response);
//                    ToastUtil.show(""+degress);
//                }
//            });
//            ImageLoader.getInstance().displayImage(url,iv_image);
        }
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
