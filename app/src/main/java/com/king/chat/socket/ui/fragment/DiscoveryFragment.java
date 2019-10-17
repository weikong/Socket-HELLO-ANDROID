package com.king.chat.socket.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.BitmapUtil;
import com.king.chat.socket.util.CombineNineRect;
import com.king.chat.socket.util.ContactManager;
import com.king.chat.socket.util.GlideOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends BaseFragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.iv_test)
    ImageView iv_test;
    @BindView(R.id.tv_hecheng)
    TextView tv_hecheng;

    private Handler handler = new Handler();


    public DiscoveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompaniesFragment.
     */
    public static DiscoveryFragment newInstance(String param1, String param2) {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static DiscoveryFragment newInstance() {
        DiscoveryFragment fragment = new DiscoveryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, view);
        initActionBar();
        initView(view);
        return view;
    }

    private void initActionBar() {
        actionBar.setTitle("发现");
    }

    private void initView(View view) {
        tv_hecheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapCount++;
                if (BitmapCount > 9)
                    BitmapCount = 3;
                bitmaps.clear();
                List<ContactBean> contactBeanList = ContactManager.getInstance().getContactList();
                if (contactBeanList.size() > BitmapCount){
                    showProgreessDialog();
                    for (int i=contactBeanList.size() - 1;i>contactBeanList.size()-BitmapCount-1;i--){
                        ContactBean contactBean = contactBeanList.get(i);
//                        GlideApp.with(getActivity()).applyDefaultRequestOptions(GlideOptions.optionDefaultFriend()).load(contactBean.getHeadPortrait()).dontAnimate().into(viewHolder.iv_header);
                        loadImageSimpleTarget(contactBean.getHeadPortrait());
                    }
                }
            }
        });
    }

    int BitmapCount = 2;
    List<Bitmap> bitmaps = new ArrayList<>();

    private void loadImageSimpleTarget(String url) {
        GlideApp.with(getActivity()).asBitmap().load(url).into(new SimpleTarget<Bitmap>(CombineNineRect.imgWidth,CombineNineRect.imgHeight) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (resource != null){
                    bitmaps.add(resource);
                }
                if (bitmaps.size() == BitmapCount){
                    Bitmap bitmap = BitmapUtil.getInstance().combimeBitmap(getActivity(), CombineNineRect.imgWidth,CombineNineRect.imgHeight,bitmaps);
                    iv_test.setImageBitmap(bitmap);
                    dismissProgressDialog();
                }
            }
        });
    }
}
