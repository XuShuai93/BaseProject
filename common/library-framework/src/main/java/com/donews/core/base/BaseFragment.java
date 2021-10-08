package com.donews.core.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;
import com.trello.rxlifecycle4.RxLifecycle;
import com.trello.rxlifecycle4.android.FragmentEvent;
import com.trello.rxlifecycle4.android.RxLifecycleAndroid;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

/**
 * 基础Fragment,不涉及具体业务
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class BaseFragment extends Fragment implements LifecycleProvider<FragmentEvent> {
	private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
	private final Handler mUiHandler = new Handler(Looper.getMainLooper());

	@Override
	public Observable<FragmentEvent> lifecycle() {
		return lifecycleSubject.hide();
	}

	@Override
	@NonNull
	@CheckResult
	public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
		return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
	}

	@Override
	@NonNull
	@CheckResult
	public final <T> LifecycleTransformer<T> bindToLifecycle() {
		return RxLifecycleAndroid.bindFragment(lifecycleSubject);
	}


	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		lifecycleSubject.onNext(FragmentEvent.ATTACH);
	}

	@Override
	@CallSuper
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lifecycleSubject.onNext(FragmentEvent.CREATE);
	}

	@Override
	@CallSuper
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
	}

	@Override
	@CallSuper
	public void onStart() {
		super.onStart();
		lifecycleSubject.onNext(FragmentEvent.START);
	}

	@Override
	@CallSuper
	public void onResume() {
		super.onResume();
		lifecycleSubject.onNext(FragmentEvent.RESUME);
	}

	@Override
	@CallSuper
	public void onPause() {
		lifecycleSubject.onNext(FragmentEvent.PAUSE);
		super.onPause();
	}

	@Override
	@CallSuper
	public void onStop() {
		lifecycleSubject.onNext(FragmentEvent.STOP);
		super.onStop();
	}

	@Override
	@CallSuper
	public void onDestroyView() {
		lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
		super.onDestroyView();
	}

	@Override
	@CallSuper
	public void onDestroy() {
		lifecycleSubject.onNext(FragmentEvent.DESTROY);
		super.onDestroy();
	}

	@Override
	@CallSuper
	public void onDetach() {
		lifecycleSubject.onNext(FragmentEvent.DETACH);
		super.onDetach();
	}

	protected void toast(final String message) {
		toast(message, Toast.LENGTH_SHORT);
	}

	protected void toast(final String message, final int duration) {
		mUiHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(requireContext(), message, duration).show();
			}
		});
	}

	protected void toast(@StringRes int resId) {
		String Message = getString(resId);
		toast(Message);
	}

	protected void hideKeyboardFrom(Context context, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
				Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
