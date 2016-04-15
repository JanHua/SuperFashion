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
public abstract class IBaseActivity extends BaseActivity implements
        View.OnClickListener {

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        init();
    }

    private void init() {
        initView(); // 初始化布局
        initListener();// 初始化监听事件
        initData(); // 初始化数据
    }

}
