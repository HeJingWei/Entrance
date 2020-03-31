package com.kentrasoft.entrance.Test.contract;

import android.widget.Button;

import com.kentrasoft.entrance.Test.listenerinterface.TestListener;
import com.kentrasoft.entrance.base.IBaseModel;
import com.kentrasoft.entrance.base.IBaseView;
import com.kentrasoft.entrance.base.callback.OnLoadDataListener;

import java.util.List;

import io.reactivex.Observable;
import kotlin.Unit;

/**
 * created date: 2020/2/28 on 15:02
 * des:
 * author: HJW HP
 */
public class TestContract {
    public interface Model extends IBaseModel {

        /**
         * 逻辑层的登录
         *
         */
        void login(String user,OnLoadDataListener<List<String>> onLoadDataListener);

        //发送验证码
        void sendCode(String user, TestListener.sendCodeRegister sendCodeRegister, OnLoadDataListener<String> onLoadDataListener);
    }

    public interface View extends IBaseView {


        /**
         * 返回用户信息
         */
        String getUserInfo();
        /**
         * 登录成功
         */
        void loginSuccess(String user);

        void sendCodeSuccess(String msg);

        void setSendCodeBtnText(String btnText);
        void setSendCodeBtnEnabled(boolean enabled);

    }

    public interface Presenter {

        /**
         * 调度层的登录
         */
        void login();

        void sendCode();
    }
}
