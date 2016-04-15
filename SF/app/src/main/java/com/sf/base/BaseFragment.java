package com.sf.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 片段-基类
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected BaseActivity activity;
    protected IApplication app;

    /**
     * 标示initData方法当前是否可运行，注意：不能标示initView方法是否执行过
     */
    private boolean initDataCanRun;
    /**
     * 标示initView方法是否执行过
     */
    private boolean isInit;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.activity = (BaseActivity) activity;
        }
        app = IApplication.getIApplication();
        initDataCanRun = false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        initListener();
        isInit = true;
        initDataCanRun = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            if (initDataCanRun) {
                initData();
                initDataCanRun = false;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            if (initDataCanRun) {
                initData();
                initDataCanRun = false;
            }
        }
    }

    protected void initView(View view, Bundle savedInstanceState) {
    }

    public void initData() {
    }

    protected void initListener() {
    }

    @Override
    public void onClick(View v) {

    }
}
