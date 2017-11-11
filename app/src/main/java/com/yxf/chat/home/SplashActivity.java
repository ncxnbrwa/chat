package com.yxf.chat.home;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.yxf.chat.R;

/**
 * 引导页面
 *
 * @author xiong
 * @name SplashActivity
 * @date 2017/9/11
 */
public class SplashActivity extends AppCompatActivity {
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.startActivity(HomeActivity.class);
                finish();
            }
        }, 800);
    }
}
