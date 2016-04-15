package com.sf.okhttp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 下载文本文件
 *
 * @author wjh
 */
public class OkHttpDownloader {
    /**
     * 下载速度100kb
     */
    private final int BYTESIZE = 100 * 1024;

    private final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    private final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; // 20s
    private final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s

    // 文件下载回调对象
    private ShutDownLoad shutDownLoad;

    /**
     * 初始化请求器
     *
     * @return
     */
    private OkHttpClient defaultOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return client;
    }

    /**
     * 下载准备
     *
     * @param uri
     * @param saveFile
     * @param callBackDownload
     */
    public void downLoad(String uri, File saveFile, CallBackDownload callBackDownload) {
        Request.Builder builder = new Request.Builder().url(uri);
        executeSync(defaultOkHttpClient().newCall(builder.build()), saveFile, callBackDownload, new ShutDownLoad());
    }

    /**
     * 请求数据
     *
     * @param call
     * @param saveFile
     * @param callBackDownload
     */
    private void executeSync(Call call, final File saveFile, final CallBackDownload callBackDownload, final ShutDownLoad shutDownLoad) {
        new HttpAsyncTask<Call, String, Boolean>() {

            @Override
            protected Boolean doInBackground(Call... params) {
                try {
                    Response response = params[0].execute();
                    if (response.body() == null) {
                        return false;
                    }
                    // 回调停止下载
                    if (shutDownLoad != null) {
                        shutDownLoad.setDownLoadParameter(params[0], saveFile);
                        OkHttpDownloader.this.shutDownLoad = shutDownLoad;
                    }

                    ResponseBody responseBody = response.body();
                    return download(params[0], responseBody.byteStream(), saveFile, responseBody.contentLength(), callBackDownload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    callBackDownload.onSuccess();
                } else {
                    callBackDownload.onFailure();
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                callBackDownload.sendEnd();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callBackDownload.sendStart();
            }

        }.executeRunnable(call);
    }

    /**
     * 开始读数据
     *
     * @param inputStream
     * @param saveFile
     * @param contentLength
     * @param callBackDownload
     * @return
     */
    private boolean download(Call call, InputStream inputStream, File saveFile, long contentLength, CallBackDownload callBackDownload) {
        if (inputStream == null) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            // 初始化本地存储文件流
            fileOutputStream = new FileOutputStream(saveFile);
            byte[] buffer = new byte[BYTESIZE];
            int offset = 0;
            int downLength = 0;// 下载进度大小
            while (inputStream != null && (offset = inputStream.read(buffer, 0, buffer.length)) != -1) {
                fileOutputStream.write(buffer, 0, offset);
                downLength += offset;
                callBackDownload.onProgressUpdate(downLength, (int) contentLength);
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 停止现在
     *
     * @author wjh
     * @update 2015年5月21日 上午11:23:32
     */
    public void stop() {
        if (shutDownLoad != null) {
            shutDownLoad.close();
        }
    }

    /**
     * 下载器对象
     *
     * @author wjh
     */
    private class ShutDownLoad {
        private Call call;
        private File saveFile;

        public void setDownLoadParameter(Call call, File saveFile) {
            this.call = call;
            this.saveFile = saveFile;
        }

        /**
         * 关闭流
         *
         * @author wjh
         * @update 2015年5月21日 上午11:24:56
         */
        public void close() {
            if (call != null) {
                call.cancel();
            }
            if (saveFile != null && saveFile.isFile()) {
                saveFile.delete();
                saveFile = null;
            }
        }
    }
}
