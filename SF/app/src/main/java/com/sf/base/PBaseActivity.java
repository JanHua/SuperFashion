package com.sf.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sf.mvp.helper.GenericHelper;
import com.sf.mvp.persenter.IPresenter;
import com.sf.mvp.view.IView;

/**
 * Activity主持者(presenter层)
 */
public abstract class PBaseActivity<T extends IView> extends BaseActivity implements IPresenter<T>, View.OnClickListener, View.OnLongClickListener {

    protected T mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createBefore(savedInstanceState);
        try {
            mView = getViewClass().newInstance();
            mView.bindPresenter(this);
            setContentView(mView.create(getLayoutInflater(), null));
            mView.bindEvent();
            createLate(savedInstanceState);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }

    @Override
    public void createBefore(Bundle saveInstance) {

    }

    @Override
    public abstract void createLate(Bundle saveInstance);

    @Override
    public Activity getActivity() {
        return activity;
    }
}
