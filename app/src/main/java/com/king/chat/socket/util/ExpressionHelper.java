/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.king.chat.socket.*;
import com.king.chat.socket.bean.Expression;
import com.king.chat.socket.bean.ExpressionList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ExpressionFactory.java - 生成本地聊天对象
 * 
 * @author jiengyh
 * 
 *         2014年6月25日 上午9:54:26
 */
public class ExpressionHelper {

	public static final String FACE_IMAGE_DEL = "del_btn_nor";
	private static final String FACE_IMAGE_PRIFEX = "smiley_";
	private static final int FACE_IMAGE_COUNT = 90;

	private static ExpressionList mExpressionList;
	private static String expressionNames[] = { "[!wx]", "[!pz]", "[!se]", "[!fd]", "[!dy]", "[!ll]", "[!hx]", "[!bz]", "[!shui]", "[!dk]", "[!gg]", "[!fn]", "[!tp]", "[!cy]", "[!jy]", "[!ng]",
			"[!kuk]", "[!lengh]", "[!zk]", "[!tuu]", "[!tx]", "[!ka]", "[!baiy]", "[!am]", "[!jie]", "[!kun]", "[!jk]", "[!lh]", "[!hanx]", "[!db]", "[!fendou]", "[!zhm]", "[!yiw]", "[!xu]",
			"[!yun]", "[!zhem]", "[!shuai]", "[!kl]", "[!qiao]", "[!zj]", "[!ch]", "[!kb]", "[!gz]", "[!qd]", "[!huaix]", "[!zhh]", "[!yhh]", "[!hq]", "[!bs]", "[!wq]", "[!kk]", "[!yx]", "[!qq]",
			"[!xia]", "[!kel]", "[!cd]", "[!xig]", "[!pj]", "[!lq]", "[!pp]", "[!kf]", "[!fan]", "[!zt]", "[!mg]", "[!dx]", "[!sa]", "[!xin]", "[!xs]", "[!dg]", "[!shd]", "[!zhd]", "[!dao]", "[!zq]",
			"[!pch]", "[!bb]", "[!yl]", "[!ty]", "[!lw]", "[!yb]", "[!qiang]", "[!ruo]", "[!ws]", "[!shl]", "[!bq]", "[!gy]", "[!qt]", "[!cj]", "[!aini]", "[!bu]", "[!hd]", };

	public Map<String,Bitmap> faceBitmapMap = new HashMap<>();

	private static class ExpressionHelperHolder{
		private static final ExpressionHelper INSTANCE = new ExpressionHelper();
	}

	/**
	 * 单一实例
	 */
	public static final ExpressionHelper getInstance(){
		return ExpressionHelper.ExpressionHelperHolder.INSTANCE;
	}

	public Expression getDelExpression(){
		Expression expression = new Expression();
		expression.setFileName(FACE_IMAGE_DEL);
		expression.setFlag("[!del]");
		return expression;
	}

	/**
	 * 生成在在资源中的映射地址
	 *
	 * @return
	 */
	public ExpressionList buildFaceFileNameList() {
		if (null == mExpressionList || mExpressionList.getExpList() == null || mExpressionList.getExpList().size() == 0) {
			List<Expression> mExpsList = new ArrayList<Expression>();
			Map<String,Expression> expMap = new HashMap<>();
			for (int index = 0; index < FACE_IMAGE_COUNT; index++) {
				try {
					String assetName = new StringBuffer(FACE_IMAGE_PRIFEX).append(index).toString();
					Expression exp = new Expression();
					if (index < expressionNames.length) {
						exp.setFlag(expressionNames[index]);
					} else {
						exp.setFlag("[!xxx]");
					}
					exp.setFileName(assetName);
					mExpsList.add(exp);
					expMap.put(exp.getFlag(),exp);
					loadSmilyFile(App.getInstance(),assetName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ExpressionList expressionList = new ExpressionList();
			expressionList.setExpList(mExpsList);
			expressionList.setExpMap(expMap);
			expressionList.setName("system");
			return mExpressionList=expressionList;
		}
		return mExpressionList;
	}

	public void loadSmilyFile(Context context, String faceid, final ImageView imageView){
		String assetUri = getAssetUri(faceid);
		GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(assetUri).into(imageView);
	}

	public void loadSmilyFile(Context context, final String faceid){
		String assetUri = getAssetUri(faceid);
		try {
			Bitmap bitmap = GlideApp.with(context).asBitmap().load(assetUri).fitCenter().into(DisplayUtil.dp2px(18),DisplayUtil.dp2px(18)).get();
			faceBitmapMap.put(faceid,bitmap);
			Logger.e("faceid = "+faceid);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.e("Exception faceid = "+faceid);
		}
	}

	public String getAssetUri(String faceid){
		String assetUri = new StringBuffer().append("file:///android_asset/smiley/").append(faceid).append(".png").toString();
		return assetUri;
	}

}
