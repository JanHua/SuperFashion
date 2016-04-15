package com.sf.mvp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.mvp.persenter.IPresenter;

/**
 * View根层
 */
public interface IView {

    /**
     * 创建提供View
     */
    public View create(LayoutInflater layoutInflater, ViewGroup container);

    /**
     * 提供布局的ID
     */
    public int getLayoutId();

    /**
     * id 转 View
     */
    public <V extends View> V findViewById(int id);

    /**
     * 绑定Presenter层
     */
    public void bindPresenter(IPresenter iPresenter);

    /**
     * 绑定控件事件
     */
    public void bindEvent();

    /**
     * 初始化方法
     */
    public void onInit();

}
