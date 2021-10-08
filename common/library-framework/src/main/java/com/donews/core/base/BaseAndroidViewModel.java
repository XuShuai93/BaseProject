package com.donews.core.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * 基础AndroidViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class BaseAndroidViewModel extends AndroidViewModel {
    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
    }
}
