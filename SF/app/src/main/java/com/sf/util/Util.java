package com.sf.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 零碎工具包
 */
public class Util {

    /**
     * 初始化一个TTF(文本风格)
     *
     * @param context
     * @param path    路径:assets/font/roboto_medium.ttf
     * @return Roboto-Medium.TTF
     */
    public static Typeface getFontMedium(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return Typeface.createFromAsset(context.getResources().getAssets(),
                path);
    }

    /**
     * 手机号验证 ^4, 非4 \\D 所有的数 与非合用
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否为邮箱格式
     *
     * @param str
     * @return true:是
     * @author wjh
     * @update 2015-6-10 下午6:25:55
     */
    public static boolean isEmail(String str) {
        Pattern p = Pattern
                .compile("^([A-Za-z0-9+_]|\\-|\\.)+@(([A-Za-z0-9_]|\\-)+\\.)+[A-Za-z]{2,6}");
        return p.matcher(str).matches();
    }

    /**
     * 密码验证 6-18位
     *
     * @param mobiles
     * @return
     */
    public static boolean isPassWord(String mobiles) {
        Pattern p = Pattern
                .compile("^[a-zA-Z0-9]{6,18}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * App版本号转换有效可比较数字
     *
     * @param versionName
     * @return
     */
    public static float versionNameToNumber(String versionName) {
        if (versionName == null) {
            return 0;
        }

        String number = versionName.replaceAll("\\.", "");
        return Float.valueOf(new StringBuilder(number.substring(0, 1)).append(".").append(number.substring(1)).toString());
    }
}
