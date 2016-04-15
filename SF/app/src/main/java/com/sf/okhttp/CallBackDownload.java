package com.sf.okhttp;

/**
 * 文件下载回调
 *
 * @author wjh
 */
public abstract class CallBackDownload implements CallBack {

    @Override
    public void sendStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendEnd() {
        // TODO Auto-generated method stub

    }

    public void onSuccess() {
    }

    ;

    public void onFailure() {
    }

    ;

    public abstract void onProgressUpdate(int current, int total);
}
