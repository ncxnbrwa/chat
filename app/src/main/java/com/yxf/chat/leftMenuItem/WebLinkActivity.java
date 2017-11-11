package com.yxf.chat.leftMenuItem;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.appBase.ChatConfig;
import com.yxf.chat.utils.DialogUtil;
import com.yxf.chat.utils.WebViewUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class WebLinkActivity extends BaseActivity implements ReWebChromeClient
        .OpenFileChooserCallBack {
    public static final String TAG = "WebLinkActivity";
    @BindView(R.id.web_title_text)
    TextView titleTv;
    @BindView(R.id.webView)
    WebView mWebView;

    private static final int INTENT_REQUEST_CODE_ALBUM = 0;
    private static final int INTENT_REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE_ALBUM = 3;
    private static final int PERMISSION_REQUEST_CODE_IMAGE_CAPTURE = 4;
    MaterialDialog progressDialog;

    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsg5Plus;

    //图片Uri
    Uri imageUri;
    //相册取出的文件Uri
    Uri pictureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = DialogUtil.showContentWithProgress(WebLinkActivity.this, "加载中。。。");
        WebSettings mWebSetting = mWebView.getSettings();
        WebViewUtil.setWebViewSetting(mWebSetting);
        //水平滚动条不显示
        mWebView.setHorizontalScrollBarEnabled(false);
        //垂直滚动条不显示
        mWebView.setVerticalScrollBarEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (EmptyUtils.isNotEmpty(bundle)) {
            String linkUrl = bundle.getString(ChatConfig.LOAD_URL);
            Logger.t(TAG).w("当前加载网址:" + linkUrl);
            switch (linkUrl) {
                case ChatConfig.HAN_SHENG_CAO:
                    //设置标题
                    titleTv.setText("含生草");
                    break;
                case ChatConfig.YXFPT:
                    titleTv.setText("玉熙坊平台");
                    break;
                case ChatConfig.HQCY:
                    titleTv.setText("华齐创业");
                    break;
            }
            mWebView.loadUrl(linkUrl);
        } else {
            titleTv.setText("含生草");
            mWebView.loadUrl(ChatConfig.HAN_SHENG_CAO);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= 21) {
                    view.loadUrl(request.getUrl().toString());
                    Logger.t(TAG).w("当前加载网址:" + request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                    Logger.t(TAG).w("当前加载网址:" + request.toString());
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
                Logger.t(TAG).w("开始加载:" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
                Logger.t(TAG).w("加载完成:" + url);
            }
        });
        mWebView.setWebChromeClient(new ReWebChromeClient(this, WebLinkActivity.this));
        WebViewUtil.synCookies(WebLinkActivity.this, ChatConfig.HAN_SHENG_CAO);
    }

    @OnClick(R.id.web_back)
    public void onClick() {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_link;
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        Logger.t(TAG).w("调用方法openFileChooserCallBack");
        mUploadMsg = uploadMsg;
        showOptions();
    }

    @Override
    public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
        Logger.t(TAG).w("调用方法showFileChooserCallBack");
        mUploadMsg5Plus = filePathCallback;
        showOptions();
    }

    //弹出选择对话框
    public void showOptions() {
//        MaterialDialog choosePictureDialog = new MaterialDialog.Builder(getActivity())
//                .title("选择图片")
//                .items(R.array.pic_options)
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View itemView, int which,
//                                            CharSequence text) {
//                        if (which == 0) {
//                            // 获取存储空间权限来获取照片
//                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest
//                                    .permission.WRITE_EXTERNAL_STORAGE) == PackageManager
//                                    .PERMISSION_GRANTED) {
//                                openAlbum();
//                            } else {
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                        PERMISSION_REQUEST_CODE_ALBUM);
//                            }
//                        } else {
//                            // 打开相机拍照的权限
//                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest
//                                    .permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                                cameraCapture();
//                            } else {
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.CAMERA},
//                                        PERMISSION_REQUEST_CODE_IMAGE_CAPTURE);
//                            }
//                        }
//                        dialog.dismiss();
//                    }
//                })
//                .cancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        if (mUploadMsg != null) {
//                            mUploadMsg.onReceiveValue(null);
//                            mUploadMsg = null;
//                        }
//                        if (mUploadMsg5Plus != null) {
//                            mUploadMsg5Plus.onReceiveValue(null);
//                            mUploadMsg5Plus = null;
//                        }
//                    }
//                })
//                .cancelable(false)
//                .canceledOnTouchOutside(false)
//                .build();
//        choosePictureDialog.show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mUploadMsg != null) {
                    mUploadMsg.onReceiveValue(null);
                    mUploadMsg = null;
                }
                if (mUploadMsg5Plus != null) {
                    mUploadMsg5Plus.onReceiveValue(null);
                    mUploadMsg5Plus = null;
                }
            }
        })
                .setTitle("图片操作")
                .setItems(R.array.pic_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // 获取存储空间权限来获取照片
                            if (ContextCompat.checkSelfPermission(WebLinkActivity.this, Manifest
                                    .permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                                    .PERMISSION_GRANTED) {
                                openAlbum();
                            } else {
                                ActivityCompat.requestPermissions(WebLinkActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSION_REQUEST_CODE_ALBUM);
                            }
                        } else {
                            // 打开相机拍照的权限
                            if (ContextCompat.checkSelfPermission(WebLinkActivity.this, Manifest
                                    .permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                cameraCapture();
                            } else {
                                ActivityCompat.requestPermissions(WebLinkActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        PERMISSION_REQUEST_CODE_IMAGE_CAPTURE);
                            }
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_ALBUM:
                if (verifyPermissions(grantResults)) {
                    openAlbum();
                } else {
                    ToastUtils.showShort("你拒绝了内存读写权限");
                }
                break;
            case PERMISSION_REQUEST_CODE_IMAGE_CAPTURE:
                if (verifyPermissions(grantResults)) {
                    cameraCapture();
                } else {
                    ToastUtils.showShort("你拒绝了相机权限");
                }
                break;
        }
    }

    //权限验证
    public boolean verifyPermissions(int[] grantResults) {
        // 至少要有一个请求结果
        if (grantResults.length < 1) {
            return false;
        }

        // 核实每个权限是否请求成功
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_REQUEST_CODE_ALBUM:
                if (resultCode == RESULT_OK) {
                    String path = WebViewUtil.retrievePath(this, data);
                    pictureUri = getUriFromFile(new File(path));
                    webReceiveValue(pictureUri);
                }
                break;
            case INTENT_REQUEST_CODE_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    webReceiveValue(imageUri);
                }
                break;
        }
    }

    //传值给网页
    private void webReceiveValue(Uri imageUri) {
        if (EmptyUtils.isNotEmpty(mUploadMsg)) {
            mUploadMsg.onReceiveValue(imageUri);
            mUploadMsg = null;
        } else {
            mUploadMsg5Plus.onReceiveValue(new Uri[]{imageUri});
            mUploadMsg5Plus = null;
        }
    }

    //打开相册
    private void openAlbum() {
        Intent albumIntent = WebViewUtil.createAlbumIntent();
        startActivityForResult(albumIntent, INTENT_REQUEST_CODE_ALBUM);
    }

    //跳转拍照
    private void cameraCapture() {
        File captureImageFile = new File(Environment
                .getExternalStorageDirectory(), "capture_image.jpg");
        imageUri = getUriFromFile(captureImageFile);
        Intent captureIntent = WebViewUtil.createCameraIntent(imageUri);
        startActivityForResult(captureIntent, INTENT_REQUEST_CODE_IMAGE_CAPTURE);
    }

    //根据版本获取Uri
    private Uri getUriFromFile(File captureImageFile) {
        Uri imageUri;
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(captureImageFile);
        } else {
            imageUri = FileProvider.getUriForFile(this, "com.yxf" +
                    ".chat.fileProvider", captureImageFile);
        }
        return imageUri;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
