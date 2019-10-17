package com.king.chat.socket.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BasePagerAdapter extends PagerAdapter {

	public List<View> mListViews = new ArrayList<View>();

	public void addView(View view) {
		if (view == null)
			return;
		mListViews.add(view);
		notifyDataSetChanged();
	}

	public void setData(List<View> list) {
		mListViews.clear();
		if (list != null && list.size() > 0)
			mListViews.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mListViews.get(position), 0);
		return mListViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return view == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
