package com.sf.okhttp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.sf.base.IApplication;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Http加载工具包
 *
 * @author wjh
 */
public class HttpUtil {

    /**
     * 通信加载器
     */
    private static OkHttpClient client;
    private static final int DEFAULT_READ_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 10 * 1000; // 10s
    private static final long CACHE_MAXSIZE = 1024 * 1024;// 高速缓存的大小限制1M
    private static final String CACHE_DIRECTORY = "";// 高速缓存存放目录
    public static boolean isCookies = true; // 是否使用cookies值，true:开启

    /* 通信文本格式 json* */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /* 文件流* */
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    /* 通信编码格式* */
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * 初始化数据交互器
     *
     * @return
     */
    public static OkHttpClient getInstance() {
        if (client == null) {
            client = InnerClass.SINGLETON;
            defaultOkHttpClient(client);
            if (isCookies)
                setCooksHandler(IApplication.getIApplication(), client);
        }

        return client;
    }

    /**
     * 单例实体
     */
    private static class InnerClass {
        private static final OkHttpClient SINGLETON = new OkHttpClient();
    }

    /**
     * 初始化请求器
     *
     * @return
     */
    private static void defaultOkHttpClient(OkHttpClient client) {
        client.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        if (!TextUtils.isEmpty(CACHE_DIRECTORY)) {
            client.setCache(new Cache(new File(CACHE_DIRECTORY), CACHE_MAXSIZE));
        }
    }

    /**
     * 设置cookie
     */
    @SuppressLint("NewApi")
    private static void setCooksHandler(Context context, OkHttpClient client) {
        client.setCookieHandler(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));
    }

    /**
     * 获取cookie
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static CookieStore getCooksHandler() {
        CookieManager cookieHandler = (CookieManager) client.getCookieHandler();
        if (cookieHandler == null) {
            return null;
        }

        CookieStore cookieJar = cookieHandler.getCookieStore();
        List<HttpCookie> cookies = cookieJar.getCookies();
        for (HttpCookie cookie : cookies) {
            System.out.println("cookie:" + cookie.toString());
        }
        return cookieJar;
    }

    /**
     * get 请求
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static Call httpGet(String url, Headers headers, CallBackResult callback) throws IOException {
        final Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).build();
        } else {
            request = new Request.Builder().url(url).headers(headers).build();
        }

        Call call = getInstance().newCall(request);
        executeSync(call, callback);
        return call;
    }

    /**
     * get 请求
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static Call httpGet(String url, String name, String value, Headers headers, CallBackResult callback) throws IOException {
        final Request request;
        if (headers == null) {
            request = new Request.Builder().url(attachHttpGetParam(url, name, value)).build();
        } else {
            request = new Request.Builder().url(attachHttpGetParam(url, name, value)).headers(headers).build();
        }

        Call call = getInstance().newCall(request);
        executeSync(call, callback);
        return call;
    }

    /**
     * get 请求
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static Call httpGet(String url, Map<String, Object> params, Headers headers, CallBackResult callback) throws IOException {
        final Request request;
        if (headers == null) {
            request = new Request.Builder().url(attachHttpGetParams(url, params)).build();
        } else {
            request = new Request.Builder().url(attachHttpGetParams(url, params)).headers(headers).build();
        }

        Call call = getInstance().newCall(request);
        executeSync(call, callback);
        return call;
    }

    /**
     * post JSON格式请求
     *
     * @param url
     * @param json
     * @param headers
     * @return
     * @throws IOException
     */
    public static Call httpPost(String url, String json, Headers headers, CallBackResult callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        final Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).post(body).build();
        } else {
            request = new Request.Builder().url(url).post(body).headers(headers).build();
        }

        Call call = getInstance().newCall(request);
        executeSync(call, callback);
        return call;
    }

    /**
     * post 表单格式请求 map
     *
     * @param url
     * @param valueMap
     * @param callback
     * @return
     * @throws IOException
     */
    public static Call httpPost(String url, Map<String, String> valueMap, CallBackResult callback) {
        return httpPost(url, valueMap, null, callback);
    }

    /**
     * post 表单格式请求 map
     *
     * @param url
     * @param valueMap
     * @param headers
     * @param callback
     * @return
     * @throws IOException
     */
    public static Call httpPost(String url, Map<String, String> valueMap, Headers headers, CallBackResult callback) {
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        StringBuffer suffer = new StringBuffer();
        if (valueMap != null) {
            for (Entry<String, String> entry : valueMap.entrySet()) {
                formBody.add(entry.getKey(), entry.getValue());
                suffer.append(entry.getKey() + "=");
                if (entry.getValue() == null) {
                    suffer.append("null,");
                } else {
                    suffer.append(entry.getValue() + ",");
                }
            }
        }
        System.out.println("httpPost - " + suffer.toString());
        Request.Builder builder = new Request.Builder().url(url).post(formBody.build());
        if (headers != null) {
            builder.headers(headers);
        }
        Call call = getInstance().newCall(builder.build());
        executeSync(call, callback);
        return call;
    }

    /**
     * post 表单格式请求 object
     *
     * @param url
     * @param object
     * @param headers
     * @param callback
     * @return
     */
    public static Call httpPostObject(String url, Object object, Headers headers, CallBackResult callback) {
        return httpPost(url, classToMapValues(object), headers, callback);
    }

    /**
     * 发送一个文件
     *
     * @param url
     * @param file
     * @param headers
     * @param callback
     * @return
     */
    public static Call httpPostFile(String url, File file, Headers headers, CallBackResult callback) {
        final Request request;
        if (headers == null) {
            request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).build();
        } else {
            request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).headers(headers).build();
        }

        Call call = getInstance().newCall(request);
        executeSync(call, callback);
        return call;
    }

    /**
     * 多文件上传（ 图文混合）
     *
     * @param url
     * @param valueMap
     * @param files
     * @param headers
     * @param callback
     * @return
     */
    public static Call httpPostFiles(String url, Map<String, String> valueMap, List<File> files, Headers headers, CallBackResult callback) {
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM);
        // 文字信息加载
        StringBuffer sbffer = new StringBuffer();
        if (valueMap != null) {
            for (Entry<String, String> entry : valueMap.entrySet()) {
                multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                sbffer.append(entry.getKey() + "=");
                if (entry.getValue() == null) {
                    sbffer.append("null,");
                } else {
                    sbffer.append(entry.getValue() + ",");
                }
            }
        }
        // 图片信息加载
        int cont = 0;
        for (File file : files) {
            if (file == null) {
                continue;
            }

            ++cont;
            sbffer.append("File" + cont + file.getName());
            multipartBuilder.addPart(RequestBody.create(MultipartBuilder.FORM, file));
        }

        System.out.println("httpPost - " + sbffer.toString());
        Request.Builder builder = new Request.Builder().url(url).post(multipartBuilder.build());
        if (headers != null) {
            builder.headers(headers);
        }
        Call call = getInstance().newCall(builder.build());
        executeSync(call, callback);
        return call;
    }

    /**
     * 下载文件
     *
     * @param uri
     * @param saveFile
     * @param callBackDownload
     * @return
     * @author wjh
     * @update 2015年5月21日 上午11:28:07
     */
    public static OkHttpDownloader httpDownloader(String uri, File saveFile, CallBackDownload callBackDownload) {
        OkHttpDownloader httpDownloader = new OkHttpDownloader();
        httpDownloader.downLoad(uri, saveFile, callBackDownload);
        return httpDownloader;
    }

    /**
     * 异步加载
     *
     * @param call
     * @param callback
     */
    private static void executeSync(Call call, final CallBackResult callback) {
        new HttpAsyncTask<Call, String, Response>() {

            @Override
            protected Response doInBackground(Call... params) {
                Response response = null;
                try {
                    response = params[0].execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(Response result) {
                super.onPostExecute(result);
                callback.sendCall(result);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.sendStart();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                callback.sendEnd();
            }

        }.executeRunnable(call);
    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     *
     * @param url
     * @param name
     * @param value
     * @return
     */
    private static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url
     * @param mapValues
     * @return
     */
    private static String attachHttpGetParams(String url, Map<String, Object> mapValues) {
        if (mapValues == null || mapValues.size() == 0)
            return url;

        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append('?');

        Iterator<Entry<String, Object>> entrys = mapValues.entrySet().iterator();
        int i = 0;
        while (entrys.hasNext()) {
            Entry<String, Object> entry = entrys.next();
            if (i != 0) {
                sb.append('&');
            }
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            i = 1;
        }

        return sb.toString();
    }

    /**
     * 对象通过映射转  键值Map
     *
     * @param o object extends Serializable
     * @return
     */
    private static Map<String, String> classToMapValues(Object o) {
        if (o == null || !(o instanceof Serializable)) {
            return null;
        }

        Map<String, String> mapValues = new HashMap<String, String>();
        @SuppressWarnings("rawtypes")
        Class clazz;
        try {
            clazz = Class.forName(o.getClass().getName());
            // 根据Class对象获得属性私有的也可以获得
            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

            for (java.lang.reflect.Field f : fields) {
                String pName = f.getName();
                Object object = getFieldValueByName(pName, o);
                if (object != null) {
                    if (object instanceof Integer) { // 参数为0时忽略为空值
                        if ((Integer) object == 0) {
                            continue;
                        }
                    }
                    mapValues.put(pName, String.valueOf(object));
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return mapValues;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param o
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * map to headers
     *
     * @param headerMap
     * @return
     */
    public static Headers mapOfHeaders(Map<String, String> headerMap) {
        return Headers.of(headerMap);
    }

    /**
     * 默认请求头
     *
     * @return
     */
    public static Headers getDefaultHeader() {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("User-Agent", "android");
        return Headers.of(headerMap);
    }
}
