package com.sf.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sf.R;
import com.sf.base.PBaseActivity;
import com.sf.test.moudle.MVPTestPrinting;
import com.sf.test.view.MvpTestView;

/**
 * Created by wjh on 16/2/17.
 */
public class MvpTestActivity extends PBaseActivity<MvpTestView> {

    private MVPTestPrinting mvpTestPrinting;
    //protected MvpTestView mView;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt) {
            mvpTestPrinting.toastContent(activity);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void createBefore(Bundle saveInstance) {
        super.createBefore(saveInstance);
        mvpTestPrinting = new MVPTestPrinting("Hello world!");

    }

    @Override
    public void createLate(Bundle saveInstance) {

    }




}
