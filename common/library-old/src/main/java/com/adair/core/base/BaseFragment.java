package com.adair.core.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

/**
 * 基础Fragment,不涉及具体业务
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class BaseFragment extends Fragment {
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
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


}
