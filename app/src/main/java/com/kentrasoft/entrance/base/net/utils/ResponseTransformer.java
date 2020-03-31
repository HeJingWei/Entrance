package com.kentrasoft.entrance.base.net.utils;

import com.kentrasoft.entrance.base.net.ApiException;
import com.kentrasoft.entrance.base.net.BaseResponse;
import com.kentrasoft.entrance.base.net.CustomException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * created date: 2020/3/23 on 16:30
 * des:
 * author: HJW HP
 */
public class ResponseTransformer {
    public static <T> ObservableTransformer<BaseResponse<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends BaseResponse<T>>> {

        @Override
        public ObservableSource<? extends BaseResponse<T>> apply(Throwable throwable) throws Exception {
            Timber.e(throwable.getMessage());
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<BaseResponse<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(BaseResponse<T> tResponse) throws Exception {
            String code = tResponse.getCode();
            String message = tResponse.getMsg();
            if (code.equals(BaseResponse.SUCCESS)) {
                return Observable.just(tResponse.getResult());
            } else {
                return Observable.error(new ApiException(code, message));
            }
        }
    }
}
