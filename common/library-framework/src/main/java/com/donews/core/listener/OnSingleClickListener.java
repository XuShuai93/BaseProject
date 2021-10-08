package com.donews.core.listener;

import android.view.View;

/**
 * 点击时间，带间隔，防止多次重复点击
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/7/28 14:36
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

	private static final int DEFAULT_DURATION = 500;

	private long mClickTime = 0L;
	private int mDuration = DEFAULT_DURATION;

	public OnSingleClickListener() {
	}

	public OnSingleClickListener(int duration) {
		mDuration = duration;
	}

	@Override
	public void onClick(View v) {
		if (System.currentTimeMillis() - mClickTime >= mDuration) {
			mClickTime = System.currentTimeMillis();
			onSingleClick(v);
		}
	}

	public abstract void onSingleClick(View v);
}
