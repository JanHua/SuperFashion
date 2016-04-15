package com.sf.okhttp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Json工具包
 */
public class JsonUtil {

    private final static Gson gson = new Gson();

    /**
     * object to Json
     *
     * @param object Bean对象
     * @return
     */
    public static String object2Json(Object object) {
        return gson.toJson(object);
    }

    /**
     * Json to Object
     *
     * @param jsonString json字符串
     * @param clazz      要转换类的声明
     * @return
     */
    public static <T> T Json2Object(String jsonString, Class<T> clazz) {
        T gsonBean = null;
        try {
            gsonBean = gson.fromJson(jsonString, clazz);
        } catch (Exception e) {
        }
        return gsonBean;
    }

    /**
     * Json to Object
     *
     * @param jsonString json字符串
     * @param token      类型封装类
     * @return
     */
    public static <T> T Json2Object(String jsonString, TypeToken<T> token) {
        T gsonBean = null;
        try {
            gsonBean = gson.fromJson(jsonString, token.getType());
        } catch (Exception e) {
        }
        return gsonBean;
    }

    /**
     * Json to ListObject
     *
     * @param jsonString json字符串
     * @param token      列表token
     * @return
     */
    public static <T> List<T> Json2ListObject(String jsonString, TypeToken<List<T>> token) {
        List<T> list = null;
        try {
            list = gson.fromJson(jsonString, token.getType());
        } catch (Exception e) {
        }
        if (list == null)
            list = new ArrayList<T>();
        return list;
    }

    /**
     * response to ConnFormat
     *
     * @param code     后台响应code
     * @param response 后台响应数据字符串
     * @return ConnFormat数据封装类
     */
    public static ConnFormat formatResp(int code, String response) {
        if (response == null)
            return new ConnFormat(String.valueOf(code), "The request failed - Unexpected code");

        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (Exception e) {
            return null;
        }

        if (json == null) {
            return null;
        }

        boolean status = json.optBoolean(ConnFormat.JSON_STATUS);
        if (status) {
            return new ConnFormat(String.valueOf(code), json.optString(ConnFormat.JSON_STATUS_RESULT));
        } else {
            return new ConnFormat(String.valueOf(code), json.optString(ConnFormat.JSON_STATUS_ERR));
        }
    }

    /**
     * Json to String
     *
     * @param response 字符串
     * @param key      json键 值
     * @return
     */
    public static String jsonToString(String response, String key) {
        if (response == null)
            return null;

        JSONObject jsono = null;
        try {
            jsono = new JSONObject(response);
        } catch (Exception e) {
        }

        if (jsono == null) {
            return null;
        }

        return jsono.optString(key);
    }

}
