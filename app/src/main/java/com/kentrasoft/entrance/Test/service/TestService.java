package com.kentrasoft.entrance.Test.service;

import com.kentrasoft.entrance.base.net.BaseResponse;
import com.kentrasoft.entrance.base.net.RetrofitManager;
import com.kentrasoft.entrance.base.net.utils.ResponseTransformer;
import com.kentrasoft.entrance.base.net.utils.SchedulerProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * created date: 2020/3/24 on 10:04
 * des:
 * author: HJW HP
 */
public class TestService {
    private TestApi iTestApi;
    private static TestService INSTANCE;
    private TestService(){
        iTestApi = RetrofitManager.getInstance().createService(TestApi.class);
    }
    public static TestService getInstance(){
        if (INSTANCE == null) INSTANCE = new TestService();
        return INSTANCE;
    }

    public Observable<String> formPostTest(String user){
        Map<String, String> map = new HashMap<>();
        map.put("user", user);
        Observable<String> compose = iTestApi.formPostTest(map)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers());
        return compose;
    }

    public Observable<List<String>> jsonPostTest(String user){
        Map<String, String> map = new HashMap<>();
        map.put("user", user);
        return iTestApi.jsonPostTest(RetrofitManager.toRequestBody(map))
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers());
    }
}
