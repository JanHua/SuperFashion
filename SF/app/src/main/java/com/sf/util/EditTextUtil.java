package com.sf.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditText 常用的一些属性值设置
 */
public class EditTextUtil {

    /**
     * 光标设置最后
     *
     * @param editText
     */
    public static void setSelectionEnd(EditText editText) {
        if (editText == null) {
            return;
        }

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Editable edit = editText.getText();
        Selection.setSelection(edit, edit.length());
    }

    /**
     * 全选
     *
     * @param editText
     */
    public static void setSelectAllOnFocus(EditText editText) {
        if (editText == null) {
            return;
        }

        editText.setSelectAllOnFocus(true);
    }

    /**
     * 关键字高亮
     *
     * @param tv
     * @param content
     * @param keyWord
     * @param linkColor
     */
    public static void setKeyWordLink(TextView tv, String content, String keyWord, int linkColor) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(keyWord)) {
            return;
        }

        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(keyWord, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(linkColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(s);
    }

    /**
     * 关闭指定的EditText软键盘 带状态
     *
     * @param context
     * @param editText
     */
    public static void hideSoftInputFromWindow(Context context, EditText editText, int state) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(),
                    state);
        }
    }

    /**
     * 关闭指定的EditText软键盘
     *
     * @author wjh
     * @update 2015年4月2日 下午3:13:45
     */
    public static void hideSoftInputFromWindow(Context context,
                                               EditText editText) {
        hideSoftInputFromWindow(context, editText, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示指定EditText键盘
     *
     * @param context
     * @param editText
     */
    public static void showSoftInputFromWindow(Context context,
                                               EditText editText) {
        showSoftInputFromWindow(context, editText, 0);
    }

    /**
     * 显示指定EditText键盘 带状态
     *
     * @param context
     * @param editText
     */
    public static void showSoftInputFromWindow(Context context, EditText editText, int state) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.requestFocus();
        imm.showSoftInput(editText, state);
    }

    /**
     * 关闭activity软键盘
     *
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }

        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置编辑状态
     *
     * @param textView
     * @param isState  true:可编辑,false:不可编辑
     */
    public static void setFocusable(TextView textView, boolean isState) {
        if (textView == null) {
            return;
        }

        if (isState) {
            textView.setFocusableInTouchMode(true);
            textView.setFocusable(true);
            textView.setCursorVisible(true);
            textView.requestFocus();
        } else {
            textView.setFocusable(false);
            textView.setFocusableInTouchMode(false);
            textView.setCursorVisible(false);
        }
    }

    /**
     * 获取行高
     *
     * @param textView
     * @return
     */
    public static float getLineHeight(TextView textView) {
        if (textView == null) {
            return 0;
        }

        Paint.FontMetrics fontMetrics = textView.getPaint().getFontMetrics();
        float lineSpacing = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            lineSpacing = textView.getLineSpacingExtra();
        }

        return Math.abs(fontMetrics.bottom) + Math.abs(fontMetrics.top) + lineSpacing;
    }

    /**
     * 设置几行的高度
     *
     * @param textView
     * @param lines
     */
    public static void setTextViewHeight(TextView textView, int lines) {
        if (lines < 1 || textView == null) {
            return;
        }

//        float lineHeight = getLineHeight(textView);
//        int linesHeight = (int) (lineHeight * lines);

        int linesHeight = textView.getLineHeight() * lines;
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.height = linesHeight + textView.getPaddingTop() + textView.getPaddingBottom();
        textView.setLayoutParams(layoutParams);
    }
}
