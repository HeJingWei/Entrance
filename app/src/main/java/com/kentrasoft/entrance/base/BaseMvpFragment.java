package com.kentrasoft.entrance.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.kentrasoft.entrance.base.utils.dialogtiplib.dialog_tip.TipLoadDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created date: 2020/2/28 on 14:53
 * des:
 * author: HJW HP
 */
@SuppressWarnings("unchecked")
public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment implements IBaseView{
    private Unbinder unBinder;
    protected Context mContext;
    protected View rootView;
    protected P presenter;
    private TipLoadDialog mLoadDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        unBinder = ButterKnife.bind(this, rootView);
        if (regEvent()) {
            BusManager.getInstance().register(this);
        }
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        return rootView;
    }

    /**
     * 需要接收事件 重写该方法 并返回true
     */
    protected abstract boolean regEvent();

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unBinder != null) {
            unBinder.unbind();
        }
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        if (regEvent()) {
            BusManager.getInstance().unregister(this);
        }
    }

    private void createLoadDialog(){
        mLoadDialog = new TipLoadDialog(mContext).setNoShadowTheme()
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

//    /**
//     * 同一个根布局加载多个fragment
//     */
//    public void loadFragments(int containerId, int showId, List<SupportFragment> fragments) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        for (int i = 0; i < fragments.size(); i++) {
//            SupportFragment baseFragment = fragments.get(i);
//            transaction.add(containerId, baseFragment, baseFragment.getClass().getSimpleName());
//            if (i != showId) {
//                transaction.hide(baseFragment);
//            }
//        }
//        transaction.commitAllowingStateLoss();
//    }
//
//    /**
//     * 选择要显示的fragment
//     */
//    public void switchFragment(SupportFragment show, SupportFragment hide) {
//        if (show == hide) {
//            return;
//        }
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.show(show);
//        transaction.hide(hide);
//        transaction.commitAllowingStateLoss();
//    }

    protected abstract int getLayoutId();

    protected abstract void initView();
}
