package com.yxf.chat.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ELS {
    private static final String ELS = "EL_SharePrefence";
    private static ELS mPref = null;
    private SharedPreferences mSharePrefer = null;
    private Editor mEditor = null;

    public static final String USER_ID = "UserId";
    public static final String USER_NAME = "UserName";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_ICON = "UserIcon";
    public static final String USER_PHONE = "Phone";
    public static final String SESSION_KEY = "SessionKey";

    public static final String SEX_CHOOSE = "sex";
    public static final String DISTANCE_CHOOSE = "distance";
    public static final String AGE_LEFT = "age_left";
    public static final String AGE_RIGHT = "age_right";

    public static ELS getInstance(Context context) {
        if (mPref == null) {
            mPref = new ELS(context);
        }
        return mPref;
    }

    private ELS(Context context) {
        mSharePrefer = context.getSharedPreferences(ELS, Context.MODE_PRIVATE);
        mEditor = mSharePrefer.edit();
    }

    //一键清除sessionkey
    public void clearSessionkey() {
        mEditor.putString(SESSION_KEY, null);
        mEditor.apply();
    }

    //清除所用户相关的信息
    public void clearUserInfo() {
        mEditor.putString(SESSION_KEY, null);
        mEditor.apply();
    }

    public void saveStringData(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getStringData(String key) {
        return mSharePrefer.getString(key, "");
    }

    public void saveIntData(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int getIntData(String key, int defaultValue) {
        return mSharePrefer.getInt(key, defaultValue);
    }

    public void saveFloatData(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public float getFloatData(String key) {
        return mSharePrefer.getFloat(key, 0);
    }

    /**
     * 清空 SharedPreferences
     */
    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }

}
