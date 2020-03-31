package com.kentrasoft.entrance.Test.service;

import com.kentrasoft.entrance.base.net.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * created date: 2020/3/4 on 16:27
 * des:
 * author: HJW HP
 */
public interface TestApi {

    /**
     * Json格式的Post请求（application/json）
     */
    @POST("test")
    Observable<BaseResponse<List<String>>> jsonPostTest(@Body RequestBody body);

    /**
     * Form格式的Post请求（application/x-www-form-urlencoded）
     */
    @FormUrlEncoded
    @POST("test")
    Observable<BaseResponse<String>> formPostTest(@QueryMap Map<String, String> params);

    @POST("sendCode")
    Observable<BaseResponse<Void>> sendCodeTest(@Body RequestBody boday);
}
