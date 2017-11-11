package com.yxf.chat.appBase;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.yxf.chat.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


//基础activity
public abstract class BaseActivity extends AppCompatActivity {
    public ChatApplication mChatApplication = null;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialConfiguration();
        mChatApplication = ChatApplication.getInstance();
        mChatApplication.pushActivity(this);
        setContentView(getLayoutId());
        Logger.w("onCreate");
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.w("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.w("onResume");
    }

    /**
     * 获取当前Activity
     *
     * @return BaseActivity
     */
    public BaseActivity getBaseActivity() {
        return this;
    }

    /**
     * 跳转至其他Activity
     *
     * @param cls    Activity class
     * @param bundle android.os.bundle
     */
    //页面跳转的方法
    public void toActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
        //设置简单的切换activity的动画
//        this.overridePendingTransition(R.anim.slide_in_right, R.anim.activity_open_exit);
    }

    public abstract int getLayoutId();

    @Override
    protected void onPause() {
        super.onPause();
        Logger.w("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.w("onStop");
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        Logger.w("onDestroy");
        mChatApplication.popActivity(this);
    }


    //实现透明状态栏
    public void setInitialConfiguration() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            ViewGroup decorContentView = (ViewGroup) findViewById(android.R.id.content);
            ViewGroup rootView = (ViewGroup) decorContentView.getChildAt(0);
            if (rootView != null) {
                ViewCompat.setFitsSystemWindows(rootView, true);
                rootView.setClipToPadding(true);
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View
                    .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
