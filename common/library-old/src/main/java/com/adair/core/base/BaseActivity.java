package com.adair.core.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adair.core.utils.StatusBarUtils;

/**
 * 基础Activity,不涉及具体业务
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public abstract class BaseActivity extends AppCompatActivity {
    private boolean isResume = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeSuperOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        beforeSetContentView(savedInstanceState);
        if (getContentLayoutId() != 0) {
            setContentView(getContentLayoutId());
        } else if (getContentView() != null) {
            setContentView(getContentView());
        }
        if (isLandscape()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        afterSetContentView(savedInstanceState);
        initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 是否在Resume显示期间
     *
     * @return true 在Resume期间
     */
    public boolean isResume() {
        return isResume;
    }

    /**
     * 设置横屏
     *
     * @return true 横屏显示,false 默认竖屏显示
     */
    public boolean isLandscape() {
        return false;
    }

    /**
     * 执行在super.onCreate(savedInstanceState)之前
     */
    public void beforeSuperOnCreate(@Nullable Bundle savedInstanceState) {
    }


    /**
     * 在setContentView之前执行
     */
    public void beforeSetContentView(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 返回布局Layout
     */
    @LayoutRes
    public abstract int getContentLayoutId();

    /**
     * 返回ContentView
     */
    public abstract View getContentView();

    /**
     * 在setContentView之后执行
     */
    public void afterSetContentView(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化View
     */
    public abstract void initView(@Nullable Bundle savedInstanceState);
}
