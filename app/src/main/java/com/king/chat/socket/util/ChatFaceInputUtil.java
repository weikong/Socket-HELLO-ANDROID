package com.king.chat.socket.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.Expression;
import com.king.chat.socket.ui.activity.chat.MainChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/10/17.
 */

public class ChatFaceInputUtil {

    private static class ChatFaceInputUtilHolder{
        private static final ChatFaceInputUtil INSTANCE = new ChatFaceInputUtil();
    }

    /**
     * 单一实例
     */
    public static final ChatFaceInputUtil getInstance(){
        return ChatFaceInputUtil.ChatFaceInputUtilHolder.INSTANCE;
    }

    public String getAssetUri(String faceid){
        String assetUri = new StringBuffer().append("file:///android_asset/smiley/").append(faceid).append(".png").toString();
        return assetUri;
    }

    public List<String> subString(String old) {
        List<String> list = new ArrayList<String>();
        String strs[] = old.split("\\[!");// "[!smile],[!gz]"
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            if (TextUtils.isEmpty(str))
                continue;
            if (old.contains("[!" + str)) {
                str = "[!" + str;
            }
            String ends[] = str.split("\\]");
            for (int j = 0; j < ends.length; j++) {
                str = ends[j];
                if (TextUtils.isEmpty(str))
                    continue;
                if (old.contains(str + "]")) {
                    str = str + "]";
                }
                list.add(str);
            }
        }
        return list;
    }

    public void setExpressionTextView(final Context context, final String strContent, final TextView textView) {
        textView.setText("");
        if (!TextUtils.isEmpty(strContent)) {
            if (!strContent.contains("[!")){
                textView.setText(strContent);
                return;
            }
            List<String> list = subString(strContent);
            for (String strrel : list) {
                if (strrel.startsWith("[!") && strrel.endsWith("]")) {
                    final Expression expression = ExpressionHelper.getInstance().buildFaceFileNameList().getExpMap().get(strrel);
                    if (expression == null)
                        continue;
                    if (ExpressionHelper.getInstance().faceBitmapMap.containsKey(expression.getFileName())){
                        Bitmap bitmap = ExpressionHelper.getInstance().faceBitmapMap.get(expression.getFileName());
                        ImageSpan imageSpan = new ImageSpan(context, bitmap);
                        imageSpan.getDrawable().setBounds(0, 0, DisplayUtil.dp2px(18), DisplayUtil.dp2px(18));
                        SpannableString spannableText = new SpannableString(expression.getFlag());
                        spannableText.setSpan(imageSpan, 0, expression.getFlag().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textView.append(spannableText);
                    } else {
                        GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asBitmap().load(ExpressionHelper.getInstance().getAssetUri(expression.getFileName())).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                ImageSpan imageSpan = new ImageSpan(context, resource);
                                imageSpan.getDrawable().setBounds(0, 0, DisplayUtil.dp2px(18), DisplayUtil.dp2px(18));
                                SpannableString spannableText = new SpannableString(expression.getFlag());
                                spannableText.setSpan(imageSpan, 0, expression.getFlag().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                textView.append(spannableText);
                            }
                        });
                    }
                } else {
                    textView.append(strrel);
                }
            }
        }
    }
}
