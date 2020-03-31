package com.kentrasoft.entrance.base.net;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.reactivestreams.Subscriber;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import timber.log.Timber;


/**
 * created date: 2020/3/4 on 14:13
 * des:
 * author: HJW HP
 */
@SuppressLint("TimberArgCount")
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
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

    @Override
    public void onError(Throwable e) {
        Timber.e(e);
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            onRequestError(PARSE_ERROR, "数据解析错误");
        } else if (e instanceof ConnectException) {
            onRequestError(NETWORK_ERROR, "网络连接错误");
        } else if (e instanceof HttpException || e instanceof UnknownHostException ||  e instanceof SocketTimeoutException){
            onRequestError(HTTP_ERROR, "网络异常");
        } else {
            onRequestError(UNKNOWN, "未知错误");
        }
    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        Timber.d("Response",new Gson().toJson(tBaseResponse));
        if (tBaseResponse.getCode().equals(BaseResponse.SUCCESS)) {
            onRequestSuccess(tBaseResponse.getMsg(), tBaseResponse.getResult());
        } else {
            onRequestError(tBaseResponse.getCode(), tBaseResponse.getMsg());
        }
    }

    //具体实现下面两个方法，便可从中得到更直接详细的信息
    public abstract void onRequestSuccess(String msg, T t);

    public abstract void onRequestError(String code, String errMessage);

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
