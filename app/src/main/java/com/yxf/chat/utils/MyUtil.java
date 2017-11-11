package com.yxf.chat.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yxf.chat.R;

import java.security.MessageDigest;
import java.util.Calendar;

/**
 * 时间操作工具类
 *
 * @author xiong
 * @name MyUtil
 * @date 2017/8/30
 */
public class MyUtil {
    public static final String TAG = "MyUtil";

    //根据生日获取年龄
    public static int getAge(String birthday) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) - getBirthYear(birthday);
    }

    //裁剪日期
    private static String[] spiltBirth(String birthday) {
        return birthday.split("-");
    }

    //获取年份
    private static int getBirthYear(String birthday) {
        return Integer.parseInt(spiltBirth(birthday)[0]);
    }

    //创建标签TextView
    public static TextView createLabel(Context context, String text) {
        TextView labelText = new TextView(context);
        labelText.setText(text);
        labelText.setTextColor(Color.WHITE);
        labelText.setTextSize(14);
        labelText.setBackgroundResource(R.drawable.label_item);
        return labelText;
    }

    // MD5加密，32位
    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    //键盘是否显示
    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect frame = new Rect();
        //获取root在窗体的可视区域
        rootView.getWindowVisibleDisplayFrame(frame);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        //获取不可见区域高度
        int heightDiff = rootView.getBottom() - frame.bottom;
        Logger.t(TAG).w("屏幕高度:" + rootView.getBottom() + ", 可见区域高度:" + frame.bottom + ", " +
                "键盘高度:" + heightDiff);
        return heightDiff > softKeyboardHeight * dm.density;
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            //在窗口中计算这个视图的坐标。参数必须是两个整数的数组。方法返回后，数组包含该View的x和y位置
            v.getLocationInWindow(l);
            //计算出View的上下左右位置
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            //当事件位于View范围外时返回true
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
