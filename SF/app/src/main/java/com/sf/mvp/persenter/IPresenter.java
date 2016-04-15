package com.sf.mvp.persenter;

import android.app.Activity;
import android.os.Bundle;

/**
 * 主持者根行为(Presenter层)
 */
public interface IPresenter<T> {

    /**
     * 获取主持者View类型
     */
    public Class<T> getViewClass();

    /**
     * 初始布局前行为
     *
     * @param saveInstance
     */
    public void createBefore(Bundle saveInstance);

    /**
     * 初始布局后行为
     *
     * @param saveInstance
     */
    public void createLate(Bundle saveInstance);

    /**
     * 获取ui
     *
     * @return
     */
    public Activity getActivity();
}
