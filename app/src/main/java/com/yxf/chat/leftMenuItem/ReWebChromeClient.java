package com.yxf.chat.leftMenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * 自定义WebView加载的WebChromeClient
 *
 * @author xiong
 * @name ReWebChromeClient
 * @date 2017/8/19
 */
public class ReWebChromeClient extends WebChromeClient {

    public static final String TAG = "ReWebChromeClient";
    private OpenFileChooserCallBack mOpenFileChooserCallBack;
    private Context context;

    public ReWebChromeClient(OpenFileChooserCallBack openFileChooserCallBack) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
    }

    public ReWebChromeClient(OpenFileChooserCallBack openFileChooserCallBack, Context context) {
        mOpenFileChooserCallBack = openFileChooserCallBack;
        this.context = context;
    }

    // 重写JS的Alert弹窗样式
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.confirm();
                }
                return false;
            }
        });
        return true;
    }

    // 重写JS的Confirm弹窗样式
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.cancel();
                }
                return false;
            }
        });
        return true;
    }

    // 重写JS的Prompt弹窗样式
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                              final JsPromptResult result) {
        final EditText editText = new EditText(context);
        editText.setText(defaultValue);

        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(message)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm(editText.getText().toString());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.cancel();
                }
                return false;
            }
        });
        return true;
    }

    //3.0+,不是对外开放的方法
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
    }

    //4.1.1+,也不是对外开放的方法
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
    }

    //5.0以上才起效果
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        mOpenFileChooserCallBack.showFileChooserCallBack(filePathCallback);
        return true;
    }

    public interface OpenFileChooserCallBack {
        void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);

        void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback);

    }


}
