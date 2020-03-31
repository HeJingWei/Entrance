package com.kentrasoft.entrance.Test.model;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.kentrasoft.entrance.Test.contract.TestContract;
import com.kentrasoft.entrance.Test.listenerinterface.TestListener;
import com.kentrasoft.entrance.Test.service.TestApi;
import com.kentrasoft.entrance.Test.service.TestService;
import com.kentrasoft.entrance.base.callback.OnLoadDataListener;
import com.kentrasoft.entrance.base.net.ApiException;
import com.kentrasoft.entrance.base.net.BaseObserver;
import com.kentrasoft.entrance.base.net.RetrofitManager;
import com.kentrasoft.entrance.base.net.utils.ResponseTransformer;
import com.kentrasoft.entrance.base.net.utils.SchedulerProvider;
import com.kentrasoft.entrance.base.progressmanager.ProgressListener;
import com.kentrasoft.entrance.base.progressmanager.ProgressManager;
import com.kentrasoft.entrance.base.progressmanager.body.ProgressInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * created date: 2020/2/28 on 15:04
 * des:
 * author: HJW HP
 */
@SuppressWarnings("unchecked")
public class TestModel implements TestContract.Model {

    @Override
    public void login(String user, final OnLoadDataListener<List<String>> onLoadDataListener) {
        if (!TextUtils.isEmpty(user)) {
            loginFormTest(user, onLoadDataListener);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void sendCode(String user, TestListener.sendCodeRegister sendCodeRegister, OnLoadDataListener<String> onLoadDataListener) {
        int INTERVAL_TIME = 10;
        Observable.just(new Object())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onLoadDataListener.onSubscribe(disposable);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Object unit) throws Exception {
                        //判断手机号
                        if (TextUtils.isEmpty(user)) {
                            onLoadDataListener.onFailure("", "手机号不能为空");
                            return Observable.empty();
                        }
                        //调用发送验证码接口(省略)
                        return sendCodeTest(user);
//                        onLoadDataListener.onSuccess("验证码发送成功");
//                        return Observable.just(true);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        onLoadDataListener.onFinally();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                //将点击事件转换成倒计时
                .flatMap(new Function<Boolean, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Boolean aBoolean) throws Exception {
                        sendCodeRegister.sendCodeResEnabled(false);
                        sendCodeRegister.setSendCodeBtnText("剩余 " + INTERVAL_TIME + " 秒");
                        //如果使用Observable.interval(interval, TimeUnit)，会默认延迟interval执行
                        return Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                                .take(INTERVAL_TIME);
                    }
                })
                //将递增数字替换成递减的倒计时数字
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return INTERVAL_TIME - (aLong + 1);
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //显示剩余时长。当倒计时为 0 时，还原 btn 按钮.
                        if (aLong == 0) {
                            sendCodeRegister.sendCodeResEnabled(true);
                            sendCodeRegister.setSendCodeBtnText("发送验证码");
                        } else {
                            sendCodeRegister.setSendCodeBtnText("剩余 " + aLong + " 秒");
                        }
                    }
                });
    }

    private Observable sendCodeTest(String user) {
        Map<String, String> map = new HashMap<>();
        map.put("user", user);
        Observable observable = RetrofitManager.getInstance()
                .createService(TestApi.class)
                .sendCodeTest(RetrofitManager.toRequestBody(map))
                .compose(SchedulerProvider.getInstance().applySchedulers());
        return observable;
    }

    @SuppressLint("CheckResult")
    private void loginFormTest(String user, final OnLoadDataListener<List<String>> onLoadDataListener) {
        //嵌套请求test
        TestService.getInstance()
                .formPostTest(user)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //第一次请求成功
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onLoadDataListener.onSubscribe(disposable);
                    }
                })
                .flatMap(new Function<String, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(String s) throws Exception {
                        //s是第一次请求返回
                        return  TestService.getInstance().jsonPostTest(s);
                    }
                })
                .doFinally(onLoadDataListener::onFinally)
                .subscribe(new Consumer<List<String>>() {
                               @Override
                               public void accept(List<String> list) throws Exception {
                                    onLoadDataListener.onSuccess(list);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable e) throws Exception {
                                   ApiException a = (ApiException) e;
                                   onLoadDataListener.onFailure(a.getCode(),a.getDisplayMessage());
                               }
                           }
                );

    }

    private void loginJsonTest(String user, final OnLoadDataListener<List<String>> onLoadDataListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user", user);
        RetrofitManager.getInstance()
                .createService(TestApi.class)
                .jsonPostTest(RetrofitManager.toRequestBody(map))
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new BaseObserver<List<String>>() {
                    @Override
                    public void onRequestSuccess(String msg, List<String> s) {
                        onLoadDataListener.onSuccess(s);
                    }

                    @Override
                    public void onRequestError(String code, String errMessage) {
                        onLoadDataListener.onFailure(code, errMessage);
                    }
                });


    }

    /**
     * 监听文件上传、下载，glide加载进度
     */
    private void progressTestListener() {
        ProgressManager.getInstance().addResponseListener("url", new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {

            }

            @Override
            public void onError(long id, Exception e) {

            }
        });


    }
}
