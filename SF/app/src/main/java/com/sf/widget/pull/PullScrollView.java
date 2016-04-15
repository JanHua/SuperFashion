package com.sf.widget.pull;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 支持上下滑动的ScrollView 配合XPullSlipLayout使用
 *
 * @author wjh
 */
public class PullScrollView extends ScrollView implements PullSlipLayout.Pullable {

    public PullScrollView(Context context) {
        super(context);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}
