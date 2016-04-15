package com.sf.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.BaseActivity;
import com.sf.base.BaseFragment;
import com.sf.mvp.helper.GenericHelper;
import com.sf.mvp.persenter.IPresenter;
import com.sf.mvp.view.IView;

/**
 * Fragment主持者(presenter层)
 */
public abstract class PBaseFragment<T extends IView> extends Fragment implements IPresenter<T> {

    protected T mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        createBefore(savedInstanceState);
        try {
            mView = getViewClass().newInstance();
            View view = mView.create(inflater, container);
            mView.bindPresenter(this);
            mView.bindEvent();
            createLate(savedInstanceState);
            return view;
        } catch(Exception e) {
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
    public void createLate(Bundle saveInstance) {

    }
}
