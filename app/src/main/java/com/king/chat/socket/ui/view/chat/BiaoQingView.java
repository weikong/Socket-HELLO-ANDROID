package com.king.chat.socket.ui.view.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.Expression;
import com.king.chat.socket.bean.ExpressionList;
import com.king.chat.socket.ui.adapter.BasePagerAdapter;
import com.king.chat.socket.ui.adapter.RecyclerGifAdapter;
import com.king.chat.socket.ui.view.dialog.ProgressDialogMyBg;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.ExpressionHelper;
import com.king.chat.socket.util.FileUtil;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.SDCardUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class BiaoQingView extends RelativeLayout {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.layout_dot)
    LinearLayout layout_dot;

    @BindView(R.id.iv_dot1)
    ImageView iv_dot1;
    @BindView(R.id.iv_dot2)
    ImageView iv_dot2;
    @BindView(R.id.iv_dot3)
    ImageView iv_dot3;
    @BindView(R.id.iv_dot4)
    ImageView iv_dot4;

    @BindView(R.id.tv_more_add)
    TextView tv_more_add;
    @BindView(R.id.tv_more_biaoqing)
    TextView tv_biaoqing;
    @BindView(R.id.tv_more_gif)
    TextView tv_gif;
    @BindView(R.id.tv_more_shoucang)
    TextView tv_shoucang;

    BasePagerAdapter adapter;
    List<View> viewList = new ArrayList<>();

    RecyclerGifAdapter gifAdapter;

    int dotNomalWidth = 6;
    int dotSelectWidth = 12;

    String gifPath = "";

    public BiaoQingView(Context context) {
        super(context);
        initView(context);
    }

    public BiaoQingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BiaoQingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_biao_qing, this);
        ButterKnife.bind(this,view);
        dotNomalWidth = DisplayUtil.dp2px(6);
        dotSelectWidth = DisplayUtil.dp2px(12);
        initGifRecyclerView();
        adapter = new BasePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
//        loadSmileyData();
        tv_more_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgreessDialog();
                String gifZipUrlPath = "https://deepkeep.top/gif/zip/rabbit.zip";
                final String destDir = SDCardUtil.getDiskCacheDir(getContext(),"gif");
                OkHttpClientManager.downloadAsyn(gifZipUrlPath, destDir, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Logger.e("biaoqing","onFailure biaoqing = "+e.getMessage());
                        dismissProgressDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("biaoqing","onResponse biaoqing = "+response);
                        if (!TextUtils.isEmpty(response)){
                            try {
                                File file = new File(response);
                                if (file.exists()){
                                    boolean isUnZip = FileUtil.getInstance().unZip(file,destDir);
                                    if (isUnZip){
                                        file.delete();
                                    }
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        dismissProgressDialog();
                    }
                });
            }
        });
        tv_biaoqing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.VISIBLE);
                layout_dot.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.INVISIBLE);
                tv_biaoqing.setBackgroundColor(getResources().getColor(R.color.color_line));
                tv_gif.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                tv_shoucang.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
            }
        });
        tv_gif.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.INVISIBLE);
                layout_dot.setVisibility(View.INVISIBLE);
                recyclerview.setVisibility(View.VISIBLE);
                tv_gif.setBackgroundColor(getResources().getColor(R.color.color_line));
                tv_biaoqing.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                tv_shoucang.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
//                if (gifAdapter.getItemCount() == 0){
                    String GIFDir = SDCardUtil.getDiskCacheDir(getContext(),"gif");
                    File GifDirFile = new File(GIFDir);
                    if (GifDirFile.exists()){
                        File[] childFiles = GifDirFile.listFiles();
                        if (childFiles.length > 0){
                            File child = childFiles[tabIndex];
                            File[] gifFiles = child.listFiles();
                            gifAdapter.setFileDatas(Arrays.asList(gifFiles));
                            gifAdapter.notifyDataSetChanged();
                            tabIndex++;
                            if (tabIndex >= childFiles.length){
                                tabIndex = 0;
                            }
                        }
                    }
//                }
            }
        });
        tv_shoucang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.INVISIBLE);
                layout_dot.setVisibility(View.INVISIBLE);
                recyclerview.setVisibility(View.INVISIBLE);
                tv_shoucang.setBackgroundColor(getResources().getColor(R.color.color_line));
                tv_gif.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                tv_biaoqing.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
            }
        });
    }

    int tabIndex = 0;

    private void initGifRecyclerView(){
        //创建LinearLayoutManager 对象 这里使用 <span style="font-family:'Source Code Pro';">LinearLayoutManager 是线性布局的意思</span>
//        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
//        //设置为水平布局，这也是默认的
//        layoutmanager.setOrientation(OrientationHelper.HORIZONTAL);

        GridLayoutManager layoutmanager = new GridLayoutManager(getContext(),5,RecyclerView.VERTICAL,false);
        //设置RecyclerView 布局
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.addItemDecoration(new EvenItemDecoration());
        //设置Adapter
        gifAdapter = new RecyclerGifAdapter(getContext());
        recyclerview.setAdapter(gifAdapter);
        gifAdapter.setCallBack(new RecyclerGifAdapter.CallBack() {
            @Override
            public void clickGif(String url) {
                if (callBack != null){
                    callBack.clickGif(url);
                }
            }
        });
//        gifPath = SDCardUtil.getGifDir()+ File.separator+"rabbit";
//        File gifDir = new File(gifPath);
//        if (gifDir.exists()){
//            gifFiles = gifDir.listFiles();
//        }
    }

    class EvenItemDecoration extends RecyclerView.ItemDecoration {
        int space = DisplayUtil.dp2px(4);
        int column = 5;
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            // 每个span分配的间隔大小
            int spanSpace = space * (column + 1) / column;
            // 列索引
            int colIndex = position % column;
            // 列左、右间隙
            outRect.left = space * (colIndex + 1) - spanSpace * colIndex;
            outRect.right = spanSpace * (colIndex + 1) - space * (colIndex + 1);
            // 行间距
            if (position >= column) {
                outRect.top = space;
            }
        }
    }

    public void loadSmileyData(){
        if (adapter.getCount() > 0)
            return;
        viewList.clear();
        ExpressionList list = ExpressionHelper.getInstance().buildFaceFileNameList();
//        int pagerCount = list.getExpList().size() / 24;
//        int pagerYu = (list.getExpList().size() % 24) > 0 ? 1 : 0;
        int pagerCount = list.getExpList().size() / 23;
        int pagerYu = (list.getExpList().size() % 23) > 0 ? 1 : 0;
        int count = pagerCount+pagerYu;
        for (int i=0;i<count;i++){
            SmileyGridView smileyGridView = new SmileyGridView(getContext());
            smileyGridView.setCallBack(new SmileyGridView.CallBack() {
                @Override
                public void delSmiley() {
                    if (callBack != null){
                        callBack.delSmiley();
                    }
                }

                @Override
                public void clickSmiley(Expression expression) {
                    if (callBack != null){
                        callBack.clickSmiley(expression);
                    }
                }
            });
            int from = i * 23;
            int to = i*23+23;
            if (to > list.getExpList().size())
                to = list.getExpList().size();
            List<Expression> expList = new ArrayList<>();
            expList.addAll(list.getExpList().subList(from,to));
            expList.add(ExpressionHelper.getInstance().getDelExpression());
            smileyGridView.loadData(expList);
            viewList.add(smileyGridView);
        }
        adapter.setData(viewList);
        adapter.notifyDataSetChanged();
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotNomal();
            dotSelect(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void dotNomal(){
        iv_dot1.getLayoutParams().width = dotNomalWidth;
        iv_dot1.setBackgroundResource(R.drawable.bg_circle_dot_nomal);

        iv_dot2.getLayoutParams().width = dotNomalWidth;
        iv_dot2.setBackgroundResource(R.drawable.bg_circle_dot_nomal);

        iv_dot3.getLayoutParams().width = dotNomalWidth;
        iv_dot3.setBackgroundResource(R.drawable.bg_circle_dot_nomal);

        iv_dot4.getLayoutParams().width = dotNomalWidth;
        iv_dot4.setBackgroundResource(R.drawable.bg_circle_dot_nomal);
    }

    private void dotSelect(int index){
        LinearLayout.LayoutParams params;
        switch (index){
            case 0:
                params = (LinearLayout.LayoutParams) iv_dot1.getLayoutParams();
                params.width = dotSelectWidth;
                iv_dot1.setLayoutParams(params);
                iv_dot1.setBackgroundResource(R.drawable.bg_circle_dot_select);
                break;
            case 1:
                params = (LinearLayout.LayoutParams) iv_dot2.getLayoutParams();
                params.width = dotSelectWidth;
                iv_dot2.setLayoutParams(params);
                iv_dot2.setBackgroundResource(R.drawable.bg_circle_dot_select);
                break;
            case 2:
                params = (LinearLayout.LayoutParams) iv_dot3.getLayoutParams();
                params.width = dotSelectWidth;
                iv_dot3.setLayoutParams(params);
                iv_dot3.setBackgroundResource(R.drawable.bg_circle_dot_select);
                break;
            case 3:
                params = (LinearLayout.LayoutParams) iv_dot4.getLayoutParams();
                params.width = dotSelectWidth;
                iv_dot4.setLayoutParams(params);
                iv_dot4.setBackgroundResource(R.drawable.bg_circle_dot_select);
                break;
        }
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void delSmiley();
        public void clickSmiley(Expression expression);
        public void clickGif(String url);
    }

    private ProgressDialogMyBg pDialog;

    /**
     * 显示等待对话框 当点击返回键取消对话框并停留在该界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showProgreessDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialogMyBg(getContext());
            pDialog.setCanceledOnTouchOutside(false);
        }
        if (pDialog.isShowing())
            pDialog.dismiss();
        pDialog.show();
        pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    try {
                        dismissProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 销毁对话框
     */
    public void dismissProgressDialog() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
