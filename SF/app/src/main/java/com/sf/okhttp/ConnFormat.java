package com.sf.okhttp;

import java.io.Serializable;

/**
 * 响应数据封装类
 *
 * @author wjh
 */
public class ConnFormat implements Serializable {

    public static final String JSON_STATUS = "status";
    public static final String JSON_STATUS_ERR = "errMsg";
    public static final String JSON_STATUS_RESULT = "data";

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public boolean status;
    public String code;
    public String result;

    public ConnFormat(boolean status, String result) {
        super();
        this.status = status;
        this.result = result;
    }

    public ConnFormat(String code, String result) {
        super();
        this.code = code;
        this.result = result;
    }

    public ConnFormat(String code, boolean status, String result) {
        super();
        this.code = code;
        this.status = status;
        this.result = result;
    }

}
