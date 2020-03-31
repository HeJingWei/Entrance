package com.kentrasoft.entrance.base.net;

/**
 * created date: 2020/3/3 on 16:15
 * des:
 * author: HJW HP
 */
public class BaseResponse<T> {
    public static final String SUCCESS = "200";

    private String code;
    private String msg;
    private T result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
