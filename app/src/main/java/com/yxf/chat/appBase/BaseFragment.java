package com.yxf.chat.appBase;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author luquan
 *         <p>
 *         2014-8-28 上午11:22:56
 */
public abstract class BaseFragment extends Fragment {
    boolean isInit = false;
    boolean isLoad = false;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.w(getClass().getSimpleName(), "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.w(getClass().getSimpleName(), "onCreateView");
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    //XUtils必写方法，初始view
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInit = true;
        //创建时也去加载数据
        canLoadData();
    }

    public abstract int getLayoutId();

    //该方法可以获取当前fragment是否可见
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        canLoadData();
    }

    //懒加载
    private void canLoadData() {
        Logger.w(getClass().getSimpleName(), "isInit:" + isInit);
        if (!isInit)
            return;

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    protected abstract void lazyLoad();

    protected void stopLoad() {
    }

    //获取当前依赖的Activity
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.w(getClass().getName(),"onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.w(getClass().getSimpleName(), "onPause");

    }

    @Override
    public void onDestroyView() {
        //销毁试图时把标志位都置为false
        super.onDestroyView();
        Logger.w(getClass().getSimpleName(), "onDestroyView");
        isInit = false;
        isLoad = false;
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.w(getClass().getSimpleName(), "onDestroy");
    }


}
