package com.sf.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.sf.R;
import com.sf.util.AppInfo;
import com.sf.util.EditTextUtil;
import com.sf.widget.IProgressBar;

/**
 * Activity-基类
 */
public class BaseActivity extends AppCompatActivity {
    protected IApplication app;
    protected Activity activity;

    /**
     * 缓冲框
     */
    private IProgressBar progressbar;
    /**
     * 返回键回调接口
     */
    public KeyBack keyBack;
    /**
     * UI线程
     */
    private Handler handler;
    /**
     * 记录前后台切换时间
     */
    private long tempTime;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        app = IApplication.getIApplication();
        app.addActivity(this);   // 增加Activity

        activity = this;
        if (getIntent() != null) {
            onIntentEvent(getIntent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从后台切换到了前台（注意：如果调用了相机等第三方app，需要在onActivityResult方法中对应的返回情况下
        // 设置AppInfo.isActive为true，这样onResume方法就不会响应从后台到前台切换的方法了）
        if (AppInfo.isForeground(activity) && !AppInfo.isActive) {
            AppInfo.isActive = true;
            background2Foreground();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 从前台切换到了后台
        if (!AppInfo.isForeground(activity)) {
            AppInfo.isActive = false;
            foreground2Background();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeActivity(this); // 删掉此Activity
    }

    @Override
    public void finish() {
        // 关闭本页的输入框
        EditTextUtil.hideSoftInputFromWindow(this);
        super.finish();
    }

    /**
     * 页面之间Intent传递时调用
     *
     * @param intent
     */
    protected void onIntentEvent(Intent intent) {
    }

    /**
     * Activity启动状态再次调用时执行，使用场景Task模式
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent() != null) {
            setIntent(intent);
            onIntentEvent(getIntent());
        }
    }

    /**
     * 展示progressbar
     */
    public void showProgressBar() {
        showProgressBar(getTopHeight(), getBottomHeight());
    }

    /**
     * 隐藏progressbar
     */
    public void hideProgressBar() {
        if (progressbar != null) {
            progressbar.setVisibility(View.GONE);
        }
    }

    /**
     * 展示progressbar
     *
     * @param topSpace    progressbar与顶部的距离
     * @param bottomSpace progressbar与底部的距离
     */
    public void showProgressBar(int topSpace, int bottomSpace) {
        if (progressbar != null) {
            if (!progressbar.isShown()) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) progressbar.getLayoutParams();
                if (layoutParams.topMargin != topSpace || layoutParams.bottomMargin != bottomSpace) {
                    layoutParams.topMargin = topSpace;
                    layoutParams.bottomMargin = bottomSpace;
                    progressbar.setLayoutParams(layoutParams);
                }
                progressbar.setVisibility(View.VISIBLE);
            }
        } else {
            progressbar = new IProgressBar(activity);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.topMargin = topSpace;
            layoutParams.bottomMargin = bottomSpace;
            ((FrameLayout) getWindow().findViewById(Window.ID_ANDROID_CONTENT)).addView(progressbar, layoutParams);
        }
    }

    /**
     * 获取顶部的高度，包含标题栏、横向字母滑动栏、tab栏等<br/>
     * 用于确定progressbar显示的高度
     *
     * @return
     */
    public int getTopHeight() {
        View appBarLayout = findViewById(R.id.pb_appBarLayout);
        if (appBarLayout != null) {
            return appBarLayout.getMeasuredHeight();
        }
        View toolbar = findViewById(R.id.pb_toolbar);
        if (toolbar != null) {
            return toolbar.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * 获取底部栏的高度<br/>
     * 用于确定progressbar显示的高度
     *
     * @return
     */
    public int getBottomHeight() {
        return 0;
    }

    public interface KeyBack {
        void onBack();
    }

    /**
     * 返回键监听回调
     *
     * @param KeyBack
     */
    protected void onKeyBack(KeyBack KeyBack) {
        this.keyBack = KeyBack;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (progressbar != null && progressbar.isShown()) {
                progressbar.setVisibility(View.GONE);
                onProgressbarHide4BackPressed();
                return true;
            } else if (keyBack != null) {
                keyBack.onBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 用户按back键取消progressbar显示时执行此方法
     */
    protected void onProgressbarHide4BackPressed() {
    }

    /**
     * UI线程方便使用
     *
     * @return
     */
    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    /**
     * 切回前台操作
     */
    protected void background2Foreground() {
    }

    /**
     * 切到后台操作
     */
    protected void foreground2Background() {
        // 记录切后台时间
        tempTime = System.currentTimeMillis();
    }

    /**
     * 计算前后台时间差
     *
     * @param ms 毫秒
     * @return
     */
    protected boolean isTimeBetweenState(long ms) {
        return tempTime == 0 ? false : System.currentTimeMillis() - tempTime > ms;
    }
}
