package com.sf.widget.pull;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;


/**
 * 支持上下滑动的WebView 配合XPullSlipLayout使用
 *
 * @author wjh
 */
public class PullWebView extends WebView implements PullSlipLayout.Pullable {

	public PullWebView(Context context) {
		super(context);
	}

	public PullWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canPullUp() {
		if (getScrollY() >= getContentHeight() * getScale()
				- getMeasuredHeight())
			return true;
		else
			return false;
	}
}
