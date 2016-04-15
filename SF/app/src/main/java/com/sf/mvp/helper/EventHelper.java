package com.sf.mvp.helper;

import android.view.View;

import com.sf.mvp.persenter.IPresenter;

/**
 * 监听事件帮助类
 */
public class EventHelper {

    /**
     * 通过主持者添加Views点击事件
     *
     * @param iPresenter
     * @param views
     */
    public static void click(IPresenter iPresenter, View... views) {
        if (!(iPresenter instanceof View.OnClickListener)) return;
        click((View.OnClickListener) iPresenter, views);
    }

    /**
     * 通过主持者添加Views长按事件
     *
     * @param iPresenter
     * @param views
     */
    public static void longClick(IPresenter iPresenter, View... views) {
        if (!(iPresenter instanceof View.OnLongClickListener)) return;
        longClick((View.OnLongClickListener) iPresenter, views);
    }

    /**
     * 点击事件
     *
     * @param onClickListener
     * @param views
     */
    public static void click(View.OnClickListener onClickListener, View... views) {
        if (views == null || views.length == 0) return;
        for (View view : views) view.setOnClickListener(onClickListener);
    }

    /**
     * 长按事件
     *
     * @param longClickListener
     * @param views
     */
    public static void longClick(View.OnLongClickListener longClickListener, View... views) {
        if (views == null || views.length == 0) return;
        for (View view : views) view.setOnLongClickListener(longClickListener);
    }

}
