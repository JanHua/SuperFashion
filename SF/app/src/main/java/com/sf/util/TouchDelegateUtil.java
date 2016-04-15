package com.sf.util;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * 改变view的点击范围<br/>
 * 1.若View的自定义触摸范围超出Parent的大小，则超出的那部分无效。
 * 2.一个Parent只能设置一个View的TouchDelegate，设置多个时只有最后设置的生效。
 */
public class TouchDelegateUtil {

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {
        view.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(new TouchDelegate(bounds, view));
                }
            }
        });
    }

    /**
     * 还原View的触摸和点击响应范围,最小不小于View自身范围
     *
     * @param view
     */
    public static void restoreViewTouchDelegate(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(new TouchDelegate(new Rect(), view));
                }
            }
        });
    }

}
