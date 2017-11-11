package com.yxf.chat.appBase;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Map;
import java.util.Stack;

import io.rong.imkit.RongIM;

/**
 * Application基类
 *
 * @author xiong
 * @name ChatApplication
 * @date 2017/8/20
 */
public class ChatApplication extends Application {

    private static Context context;
    // 用于存放倒计时时间
    public static Map<String, Long> timeMap;

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("chat")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Logger.t("application").w("----------onCreate");
        context = getApplicationContext();
        Utils.init(context);
        mInstance = this;
    }

    public static Context getContext() {
        return context;
    }

    public final Stack<Activity> mActivityStack = new Stack<Activity>();
    private static ChatApplication mInstance;

    public static ChatApplication getInstance() {
        return mInstance;
    }

    public ChatApplication() {
        Logger.t("ChatApplication").w("ChatApplication");
    }

    /**
     * 移除堆栈里某个特定的Activity，不销毁
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 在Activity堆栈里最顶部增加一个Activity
     */
    public void pushActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    /**
     * 清除销毁获取堆栈里某个特定的Activity
     */
    private void finishActivity(Activity activity) {
        if (activity != null) {
            Logger.w(getClass().getSimpleName(), "finish:" + getClass().getSimpleName());
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 获取堆栈里某个特定的Activity
     */
    public Activity getSpecialActivity(Class<?> cls) {
        Activity act = null;
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity act_ = mActivityStack.get(i);
            if (act_ == null)
                continue;
            if (cls.equals(act_.getClass())) {
                act = act_;
                break;
            }
        }
        return act;
    }

    /**
     * 清除销毁获取堆栈里某个特定的Activity
     */
    public void finishSpecialActivity(Class<?> cls) {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity act = mActivityStack.get(i);
            if (act == null)
                continue;
            if (cls.equals(act.getClass())) {
                finishActivity(act);
            }
        }
    }

    /**
     * 获取堆栈里 最顶部的Activity
     */
    public Activity getTopActivity() {
        Activity activity = null;
        if (!mActivityStack.empty())
            activity = mActivityStack.lastElement();
        return activity;
    }


    /**
     * 清除销毁堆栈里所有的Activity，除了特定的某个Activity
     */
    public void finishAllActivityExceptOne(Class<?> cls) {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity act = mActivityStack.get(i);
            if (act == null)
                continue;
            if (!cls.equals(act.getClass())) {
                finishActivity(act);
            }
        }
    }

    /**
     * 清除销毁堆栈里所有的Activity，退出程序
     */
    public void finishApplication() {
        RongIM.getInstance().disconnect();
        while (!mActivityStack.empty()) {
            Activity activity = getTopActivity();
            finishActivity(activity);
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
