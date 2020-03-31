package com.kentrasoft.entrance.Test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxCompoundButton;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.kentrasoft.entrance.R;
import com.kentrasoft.entrance.Test.contract.TestContract;
import com.kentrasoft.entrance.Test.presenter.TestPresenter;
import com.kentrasoft.entrance.base.BaseMvpActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import kotlin.Unit;

/**
 * created date: 2020/2/28 on 15:13
 * des:
 * author: HJW HP
 */
public class TestActivity extends BaseMvpActivity<TestPresenter> implements TestContract.View {
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.pwd)
    EditText mPwd;
    @BindView(R.id.checked)
    CheckBox mChecked;
    @BindView(R.id.login)
    Button mLoginBtn;
    @BindView(R.id.code)
    Button mCodeBtn;

    @Override
    protected boolean regEvent() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        mLoginBtn.setEnabled(false);
        Observable<CharSequence> name = RxTextView.textChanges(mName).skip(1);
        Observable<CharSequence> pwd = RxTextView.textChanges(mPwd).skip(1);
        InitialValueObservable<Boolean> check = RxCompoundButton.checkedChanges(mChecked);

        Observable.combineLatest(name, pwd, check, (charSequence, charSequence2, aBoolean) -> {
            boolean isName = !TextUtils.isEmpty(charSequence) && charSequence.toString().length() == 11;
            boolean isPwd = !TextUtils.isEmpty(charSequence2);
            return isName && isPwd && aBoolean;
        }).subscribe(aBoolean -> mLoginBtn.setEnabled(aBoolean));

    }

    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter();
    }


    @Override
    public String getUserInfo() {
        //返回界面上填的用户信息
        return mName.getText().toString();
    }

    @Override
    public void loginSuccess(String user) {
        //登录成功回调
    }

    @Override
    public void sendCodeSuccess(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSendCodeBtnText(String btnText) {
        mCodeBtn.setText(btnText);
    }

    @Override
    public void setSendCodeBtnEnabled(boolean enabled) {
        mCodeBtn.setEnabled(enabled);
    }

    @Override
    public void onError(Object tag, String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.login
            , R.id.code
    })
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.login:
                presenter.login();
                break;
            case R.id.code:
                presenter.sendCode();

                break;
        }
    }
}
