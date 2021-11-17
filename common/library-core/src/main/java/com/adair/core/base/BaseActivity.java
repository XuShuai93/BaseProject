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
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
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

        if (isFullScreen()) {
            fullScreen();
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
        if (isHideNavigationBar()) {
            hideNavigationBar();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isHideNavigationBar()) {
            hideNavigationBar();
        }
    }

    protected void toast(final String message) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void toast(final int resId) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void toast(final String message, final int duration) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, duration).show();
            }
        });
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
     * 全屏，隐藏状态栏
     *
     * @return true 隐藏状态栏,false 显示状态栏
     */
    public boolean isFullScreen() {
        return false;
    }

    /**
     * 全屏，隐藏导航栏
     *
     * @return true 隐藏导航栏,false 显示导航栏
     */
    public boolean isHideNavigationBar() {
        return false;
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
     * 此方法需要在setContentView()之后调用
     */
    @SuppressWarnings("deprecation")
    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void hideNavigationBar() {
        StatusBarUtils.hideNavigationBar(this);
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
        if (isFullScreen()) {
            //隐藏标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
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
