package com.sf.mvp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.mvp.helper.EventHelper;
import com.sf.mvp.persenter.IPresenter;

/**
 * Created by wjh on 15/11/30.
 */
public abstract class ViewImpl implements IView {

    /**
     * 根View
     */
    protected View rootView;

    /**
     * 主持者
     */
    protected IPresenter iPresenter;


    @Override
    public View create(LayoutInflater layoutInflater, ViewGroup container) {
        rootView = layoutInflater.inflate(getLayoutId(), container);
        return rootView;
    }

    @Override
    public <V extends View> V findViewById(int id) {
        return (V) rootView.findViewById(id);
    }


    @Override
    public void bindPresenter(IPresenter iPresenter) {
        this.iPresenter = iPresenter;
    }

    @Override
    public void bindEvent() {
        onInit();
    }

    @Override
    public abstract int getLayoutId();

    @Override
    public abstract void onInit();

    public void clickEvent(View... views) {
        EventHelper.click(iPresenter, views);
    }
}
