package com.sf.util;

import android.content.Context;
import android.widget.Toast;

import com.sf.base.IApplication;

/**
 * Toast弹出工具
 *
 * @author wjh
 */
public class ToastUtil {

    private static Context activity = IApplication.getIApplication();
    private static Toast toast = null;

    /**
     * 吐司显示信息
     *
     * @param text
     */
    public static void showText(Object text) {
        if (text == null)
            text = "";
        if (toast != null) {
            toast.cancel();
        }
        if (text instanceof Integer) {
            toast = Toast
                    .makeText(activity, (Integer) text, Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(activity, text.toString(),
                    Toast.LENGTH_SHORT);
        }

        toast.show();
    }

    /**
     * 吐司显示信息 自定义位置
     *
     * @param text
     * @param gravity
     */
    public static void showText(Object text, int gravity) {
        if (text == null)
            text = "";
        if (toast != null) {
            toast.cancel();
        }
        if (text instanceof Integer) {
            toast = Toast
                    .makeText(activity, (Integer) text, Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(activity, text.toString(),
                    Toast.LENGTH_SHORT);
        }
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

}
