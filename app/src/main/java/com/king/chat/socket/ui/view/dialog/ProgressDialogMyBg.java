package com.king.chat.socket.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.king.chat.socket.R;

/**
 * Created by caojian on 16/10/24.
 */
public class ProgressDialogMyBg extends Dialog {
    public ProgressDialogMyBg(Context context) {
        super(context, R.style.Dialog_loading_noDim);
        init();
    }

    private void init() {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_loading,null);
        setContentView(view);
        this.setCanceledOnTouchOutside(false);
    }
}
