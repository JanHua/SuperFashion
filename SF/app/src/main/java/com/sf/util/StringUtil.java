package com.sf.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * String 工具包
 *
 * @author wjh
 */
public class StringUtil {
    /**
     * 单符号截取字符串
     *
     * @param str
     * @param split ","
     * @return
     */
    public static String[] getStringSplit(String str, String split) {
        if (str == null || split == null) {
            return null;
        }
        return str.split(split);
    }

    /**
     * 多符号截取字符串
     *
     * @param str   数组
     * @param split ",;/*"
     * @return
     */
    public static String[] getStringSplits(String str, String split) {
        if (str == null || split == null) {
            return null;
        }

        StringTokenizer st = new StringTokenizer(str, split);
        int length = st.countTokens();
        if (length < 1) {
            return null;
        } else {
            String[] strs = new String[length];
            int count = 0;
            while (st.hasMoreTokens()) {
                strs[count++] = st.nextToken();
            }
            return strs;
        }
    }

    /**
     * 多符号截取后拼接字符串
     *
     * @param str   字符串
     * @param split ",;/*"
     * @return
     */
    public static String getSplit(String str, String split) {
        if (str == null || split == null) {
            return null;
        }

        StringTokenizer st = new StringTokenizer(str, split);
        int length = st.countTokens();
        if (length < 1) {
            return null;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (st.hasMoreTokens()) {
                buffer.append(st.nextToken());
            }
            return buffer.toString();
        }
    }

    /**
     * 字符串单符号 截取List数组
     *
     * @param str
     * @param split
     * @return
     * @author wjh
     * @update 2015-6-30 下午12:02:00
     */
    public static List<String> stringToList(String str, String split) {
        if (str == null || split == null) {
            return null;
        }

        return Arrays.asList(str.split(split));
    }

    /**
     * List数组单符号拼接字符串
     *
     * @param str
     * @param split ","
     * @return
     */
    public static String listToString(List<String> str, String split) {
        if (str == null || split == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String s : str) {
            if (count != 0) {
                builder.append(split);
            }
            builder.append(s);
            count = -1;
        }
        return builder.toString();
    }

}
