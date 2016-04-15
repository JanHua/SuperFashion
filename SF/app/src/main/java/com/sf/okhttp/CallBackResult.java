package com.sf.okhttp;

import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * 后台响应数据回调及处理
 *
 * @author wjh
 */
public abstract class CallBackResult implements CallBack {

    // 请求开始
    @Override
    public void sendStart() {
        // TODO Auto-generated method stub
    }

    // 请求结束
    @Override
    public void sendEnd() {
        // TODO Auto-generated method stub
    }

    // 请求成功
    public abstract void sendSuccess(ConnFormat connFormat);

    // 请求失败
    public abstract void sendFailure(ConnFormat connFormat);

    // 返回数据
    public void onProgressUpdate(long current, long total, boolean isComplete) {
    }
    ;

    /**
     * 返回请求结果
     *
     * @param response
     */
    public void sendCall(Response response) {
        try {

            String request = isResponseSuccessful(response);
            if (request == null) {
                sendFailure(JsonUtil.formatResp(response.code(), request));
            } else {
                sendSuccess(JsonUtil.formatResp(response.code(), request));
            }
        } catch (IOException e) {
            System.out.print("The server does not exist - " + e.getMessage());
            sendSuccess(null);
            sendFailure(null);
        }
    }

    /**
     * 请求结果判断
     *
     * @param response
     * @return
     * @throws IOException
     */
    private String isResponseSuccessful(Response response) throws IOException {
        if (response == null) {
            throw new IOException("Unexpected code " + response);
        }
        if (response.isSuccessful() && response.body() != null) {
            try {
                return new ResponseProgress().handleEntity(response.body(), HttpUtil.CHARSET_NAME, this);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
