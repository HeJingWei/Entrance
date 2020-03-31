package com.kentrasoft.entrance.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.kentrasoft.entrance.R;
import com.kentrasoft.entrance.base.utils.dialogtiplib.dialog_tip.TipLoadDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created date: 2020/2/27 on 17:31
 * des:
 * author: HJW HP
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    protected P presenter;
    private Unbinder unbinder;
    protected Context mContext;
    private TipLoadDialog mLoadDialog;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建present
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        mContext = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(getLayoutId());
        //初始化ButterKnife
        unbinder = ButterKnife.bind(this);
        if (regEvent()) {
            BusManager.getInstance().register(this);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView();
    }

    /**
     * 需要接收事件 重写该方法 并返回true
     */
    protected abstract boolean regEvent();

    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (regEvent()) {
            BusManager.getInstance().unregister(this);
        }

    }

    private void createLoadDialog(){
        mLoadDialog = new TipLoadDialog(this).setNoShadowTheme()
                .setMsgAndType("加载中", TipLoadDialog.ICON_TYPE_LOADING2);
    }

    @Override
    public void showLoading() {
        if (mLoadDialog == null) createLoadDialog();
        mLoadDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (mLoadDialog == null) createLoadDialog();
        if (mLoadDialog.isShowing()) mLoadDialog.dismiss();
    }

    @Override
    public void onEmpty(Object tag) {

    }

    @Override
    public void onError(Object tag, String errorMsg) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 创建Presenter
     */
    protected abstract P createPresenter();
}
