package com.kentrasoft.entrance.base.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import timber.log.Timber;

/**
 * created date: 2020/3/23 on 16:41
 * des:
 * author: HJW HP
 */
public class CustomException {
    /**
     * 未知错误
     */
    public static final String UNKNOWN = "UNKNOWN";

    /**
     * 解析错误
     */
    public static final String PARSE_ERROR = "PARSE_ERROR";

    /**
     * 网络错误
     */
    public static final String NETWORK_ERROR = "NETWORK_ERROR";

    /**
     * 协议错误
     */
    public static final String HTTP_ERROR = "HTTP_ERROR";

        public static ApiException handleException(Throwable e) {
            ApiException ex;
            if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {
                //解析错误
                ex = new ApiException(PARSE_ERROR, "数据解析出错");
                return ex;
            } else if (e instanceof ConnectException) {
                //网络错误
                ex = new ApiException(NETWORK_ERROR, "网络连接错误");
                return ex;
            } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                //连接错误
                ex = new ApiException(HTTP_ERROR, "网络异常");
                return ex;
            } else {
                //未知错误
                ex = new ApiException(UNKNOWN, "未知错误");
                return ex;
            }
        }

}
