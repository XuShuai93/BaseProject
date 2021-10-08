package com.donews.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.donews.core.R;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 可以包含一组checkbox,用于互斥,实现RadioGroup的功能，但是可以随意摆放位置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/8/3 17:44
 */
public class CheckGroup extends View {

	protected int[] mIds = new int[32];
	protected int mCount;
	protected String mReferenceIds;
	private final HashMap<Integer, String> mMap = new HashMap<>();

	private int mCheckedId = -1;
	// when true, mOnCheckedChangeListener discards events
	private boolean mProtectFromCheckedChange = false;
	private final CompoundButton.OnCheckedChangeListener mChildCheckedChangeListener;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	public CheckGroup(Context context) {
		this(context, null);
	}

	public CheckGroup(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CheckGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mChildCheckedChangeListener = new CheckedStateTracker();
		init(attrs);
	}

	protected void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.CheckGroup);
			int N = a.getIndexCount();
			for (int i = 0; i < N; ++i) {
				int attr = a.getIndex(i);
				if (attr == R.styleable.CheckGroup_checkIds) {
					this.mReferenceIds = a.getString(attr);
					this.setIds(this.mReferenceIds);
				} else if (attr == R.styleable.CheckGroup_checkedButton) {
					mCheckedId = a.getResourceId(R.styleable.CheckGroup_checkedButton, -1);
				}
			}
			a.recycle();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setListener();
		if (mCheckedId != -1) {
			mProtectFromCheckedChange = true;
			setCheckedStateForView(mCheckedId, true);
			mProtectFromCheckedChange = false;
			setCheckedId(mCheckedId);
		}
	}

	protected void setListener() {
		ViewGroup parent = null;
		if (this.getParent() instanceof ViewGroup) {
			parent = (ViewGroup) this.getParent();
		}
		if (parent != null) {
			for (int i = 0; i < mCount; i++) {
				int id = mIds[i];
				View view = parent.findViewById(id);
				if (view instanceof CheckBox) {
					((CheckBox) view).setChecked(false);
					((CheckBox) view).setOnCheckedChangeListener(mChildCheckedChangeListener);
				}
			}
		}
	}


	protected void setIds(String idList) {
		this.mReferenceIds = idList;
		if (idList != null) {
			int begin = 0;
			this.mCount = 0;
			while (true) {
				int end = idList.indexOf(44, begin);
				if (end == -1) {
					this.addID(idList.substring(begin));
					return;
				}

				this.addID(idList.substring(begin, end));
				begin = end + 1;
			}
		}
	}

	private void addID(String idString) {
		if (idString != null && idString.length() != 0) {
			if (getContext() != null) {
				idString = idString.trim();
				int rscId = this.findId(idString);
				if (rscId != 0) {
					this.mMap.put(rscId, idString);
					this.addRscID(rscId);
				} else {
					Log.w("CheckGroup", "Could not find id of \"" + idString + "\"");
				}
			}
		}
	}

	private int findId(String referenceId) {
		int rscId;
		rscId = this.getResources().getIdentifier(referenceId, "id", this.getContext().getPackageName());
		return rscId;
	}


	private void addRscID(int id) {
		if (id != this.getId()) {
			if (this.mCount + 1 > this.mIds.length) {
				this.mIds = Arrays.copyOf(this.mIds, this.mIds.length * 2);
			}
			this.mIds[this.mCount] = id;
			++this.mCount;
		}
	}

	private void setCheckedStateForView(int viewId, boolean checked) {

		ViewParent viewParent = getParent();
		if (viewParent instanceof ViewGroup) {
			View checkedView = ((ViewGroup) viewParent).findViewById(viewId);
			if (checkedView instanceof CheckBox) {
				((CheckBox) checkedView).setChecked(checked);
			}
		}
	}

	private void setCheckedId(@IdRes int id) {
		mCheckedId = id;
		if (mOnCheckedChangeListener != null) {
			mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
		}
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// checks the appropriate radio button as requested in the XML file
	}

	private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// prevents from infinite recursion
			if (mProtectFromCheckedChange) {
				return;
			}
			mProtectFromCheckedChange = true;
			int id = buttonView.getId();
			if (mCheckedId != -1) {
				setCheckedStateForView(mCheckedId, mCheckedId == id);
			}
			mProtectFromCheckedChange = false;
			if (mCheckedId != id) {
				setCheckedId(id);
			}
		}
	}

	public interface OnCheckedChangeListener {
		void onCheckedChanged(CheckGroup checkGroup, @IdRes int checkId);
	}

	public OnCheckedChangeListener getOnCheckedChangeListener() {
		return mOnCheckedChangeListener;
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
		mOnCheckedChangeListener = onCheckedChangeListener;
	}
}
