package com.king.chat.socket.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 16/10/24.
 */
public class ProgressMyDialog extends Dialog {

    @BindView(R.id.layout_pb)
    RelativeLayout layout_pb;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.tv_pb)
    TextView tv_pb;
    @BindView(R.id.iv_pb_state)
    ImageView iv_pb_state;
    Handler handler = new Handler();

    public ProgressMyDialog(Context context) {
        super(context, R.style.Dialog_loading_noDim);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_my_progress, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        this.setCanceledOnTouchOutside(true);
    }

    public void setProgress(int progress) {
        pb.setVisibility(View.INVISIBLE);
        tv_pb.setVisibility(View.VISIBLE);
        iv_pb_state.setVisibility(View.INVISIBLE);
        tv_pb.setText(progress + "%");
    }

    public void setProgressState(boolean state) {
        pb.setVisibility(View.INVISIBLE);
        tv_pb.setVisibility(View.INVISIBLE);
        iv_pb_state.setVisibility(View.VISIBLE);
        if (state) {
            iv_pb_state.setImageResource(R.drawable.icon_toast_ok);
        } else {
            iv_pb_state.setImageResource(R.drawable.icon_toast_fail);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 500);
    }
}
