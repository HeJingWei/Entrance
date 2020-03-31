package com.kentrasoft.entrance.base.callback;

import io.reactivex.disposables.Disposable;

/**
 * created date: 2020/3/4 on 16:18
 * des:网络数据请求返回接口
 * author: HJW HP
 */
public interface OnLoadDataListener<T> {
    /**
     * 订阅
     *
     * */
    void onSubscribe(Disposable disposable);
    /**
     * 成功
     * @param t 数据
     */
    void onSuccess(T t);

    /**
     * 失败
     * @param errorMsg 错误信息
     */
    void onFailure(String code,String errorMsg);

    /**
     * 订阅终止
     * */
    void onFinally();
}
