package com.kentrasoft.entrance.Test.listenerinterface;

/**
 * created date: 2020/3/19 on 14:04
 * des:
 * author: HJW HP
 */
public class TestListener {
    public interface sendCodeRegister{
        void sendCodeResEnabled(boolean enabled);
        void setSendCodeBtnText(String btnText);
    }
}
