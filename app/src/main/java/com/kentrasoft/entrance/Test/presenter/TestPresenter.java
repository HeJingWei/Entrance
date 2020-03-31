package com.kentrasoft.entrance.Test.presenter;

import com.kentrasoft.entrance.Test.listenerinterface.TestListener;
import com.kentrasoft.entrance.base.BasePresenter;
import com.kentrasoft.entrance.Test.contract.TestContract;
import com.kentrasoft.entrance.Test.model.TestModel;
import com.kentrasoft.entrance.base.callback.OnLoadDataListener;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * created date: 2020/2/28 on 15:04
 * des:
 * author: HJW HP
 */
public class TestPresenter extends BasePresenter<TestContract.Model, TestContract.View> implements TestContract.Presenter {
    @Override
    protected TestContract.Model createModule() {
        return new TestModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void login() {
        if (isViewAttached()) {
            showLoading();
            getModule().login(getView().getUserInfo(), new OnLoadDataListener<List<String>>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDispose(disposable);
                }

                @Override
                public void onSuccess(List<String> s) {
                    getView().loginSuccess("userInfo");
                }

                @Override
                public void onFailure(String code, String errorMsg) {
                    getView().onError(code, errorMsg);
                }

                @Override
                public void onFinally() {
                    dismissLoading();
                }
            });
        }
    }

    @Override
    public void sendCode() {
        if (isViewAttached()){
            getModule().sendCode(getView().getUserInfo(), new TestListener.sendCodeRegister() {
                @Override
                public void sendCodeResEnabled(boolean enabled) {
                    getView().setSendCodeBtnEnabled(enabled);
                }

                @Override
                public void setSendCodeBtnText(String btnText) {
                    getView().setSendCodeBtnText(btnText);
                }
            }, new OnLoadDataListener<String>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDispose(disposable);
                }

                @Override
                public void onSuccess(String msg) {
                    getView().sendCodeSuccess(msg);
                }

                @Override
                public void onFailure(String code, String errorMsg) {
                    getView().onError("", errorMsg);
                }

                @Override
                public void onFinally() {
                    dismissLoading();
                }
            });
        }
    }
}
