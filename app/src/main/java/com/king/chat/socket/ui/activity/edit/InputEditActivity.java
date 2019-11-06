package com.king.chat.socket.ui.activity.edit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputEditActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.et_input)
    EditText et_input;

    String strInput;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_input_edit);
        ButterKnife.bind(this);
        strInput = getIntent().getStringExtra("DATA");
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar.setTitle("");
        actionBar.setIvBackVisiable(View.VISIBLE);
        actionBar.setRightToolVisiable(View.VISIBLE, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString();
                if (TextUtils.isEmpty(content))
                    return;
                Intent intent = new Intent();
                intent.putExtra("DATA", content);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initView() {
        et_input.setText(strInput);
        et_input.setSelection(et_input.getText().length());
    }

}
