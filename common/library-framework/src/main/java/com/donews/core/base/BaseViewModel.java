package com.donews.core.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.donews.core.base.viewmodel.IViewModelLifecycle;

/**
 * 基础ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class BaseViewModel extends ViewModel implements IViewModelLifecycle {

    private Application mApplication;
    private LifecycleOwner mLifecycleOwner;

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
        this.mLifecycleOwner = owner;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    public static <T extends BaseViewModel> ViewModelFactory createViewModelFactory(T viewModel) {
        return new ViewModelFactory(viewModel);
    }

    public Application getApplication() {
        return mApplication;
    }

    public void setApplication(Application application) {
        mApplication = application;
    }

    static class ViewModelFactory implements ViewModelProvider.Factory {

        private BaseViewModel mBaseViewModel;

        ViewModelFactory(@NonNull BaseViewModel viewModel) {
            this.mBaseViewModel = viewModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) mBaseViewModel;
        }
    }
}
