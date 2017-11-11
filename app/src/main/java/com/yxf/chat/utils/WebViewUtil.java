package com.yxf.chat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

import com.orhanobut.logger.Logger;

import static com.blankj.utilcode.util.FileUtils.isFileExists;

/**
 * 生成WebView的工具类
 *
 * @author xiong
 * @name WebViewUtil
 * @date 2017/8/19
 */
public class WebViewUtil {
    public static final String TAG = "WebViewUtil";

    public static void setWebViewSetting(WebSettings webSet) {
        webSet.setUseWideViewPort(true);
        webSet.setJavaScriptEnabled(true);
        //设置可以由JavaScript打开新窗口
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        webSet.setGeolocationEnabled(true);
        webSet.setAllowContentAccess(true);
        //允许WebView访问文件数据
        webSet.setAllowFileAccess(true);
        //启用应用缓存
        webSet.setAppCacheEnabled(true);
        //启用DOM缓存
        webSet.setDomStorageEnabled(true);
        //启用数据库缓存
        webSet.setDatabaseEnabled(true);
        //不可缩放
        webSet.setSupportZoom(false);
        webSet.setSaveFormData(false);
        webSet.setSavePassword(false);
        //允许加载图片,可不写,默认false
        webSet.setBlockNetworkImage(false);
        //指定WebView的页面布局显示形式，调用该方法会引起页面重绘。默认值为LayoutAlgorithm#NARROW_COLUMNS。
        webSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //将图片调整到适合webView的大小
        webSet.setUseWideViewPort(true);
        //缩放至屏幕的大小
        webSet.setLoadWithOverviewMode(true);

        //在Android 5.0上 Webview 默认不允许加载 Http 与 Https 混合内容
        //MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
        //MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
        //MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSet.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        //设置编码
        webSet.setDefaultTextEncodingName("utf-8");
    }

    // 同步Cookie
    public static void synCookies(Context context, String url) {
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, "app=Android;Path=/");
    }

    //获取Cookie字符串
    public static String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    //创建一个拍照的Intent
    public static Intent createCameraIntent(Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        return intent;
    }

    //创建一个打开相册的Intent
    public static Intent createAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    //检索文件路径,dataIntent用来传入activity回调传回的data
    public static String retrievePath(Context context, Intent dataIntent) {
        String picPath = null;
        try {
            Uri uri;
            if (dataIntent != null) {
                uri = dataIntent.getData();
                if (uri != null) {
                    //根据Uri拿到文件路径
                    picPath = UriUtil.getPath(context, uri);
                }
                //若文件存在直接返回当前文件路径
                if (isFileExists(picPath)) {
                    return picPath;
                }

                Logger.t(TAG).w(String.format("retrievePath failed from dataIntent:%s, extras:%s",
                        dataIntent, dataIntent.getExtras()));
            }

//            if (sourceIntent != null) {
//                //获取图片或者视频文件
//                uri = sourceIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
//                if (uri != null) {
//                    //获取该Uri的组合形式,如http
//                    String scheme = uri.getScheme();
//                    //若scheme以file为前缀,则获取当前的文件前缀
//                    if (scheme != null && scheme.startsWith("file")) {
//                        picPath = uri.getPath();
//                    }
//                }
//                if (!isFileExists(picPath)) {
//                    Logger.t(TAG).w(String.format("retrievePath file not found from sourceIntent " +
//                            "path:%s", picPath));
//                }
//            }
            return picPath;
        } finally {
//            Logger.t(TAG).w("retrievePath(" + sourceIntent + "," + dataIntent + ") ret: " + picPath);
        }
    }
}
