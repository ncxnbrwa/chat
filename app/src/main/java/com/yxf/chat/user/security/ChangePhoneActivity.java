package com.yxf.chat.user.security;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.utils.MyUtil;
import com.yxf.chat.utils.TimeButton;

import butterknife.BindView;
import butterknife.OnClick;


public class ChangePhoneActivity extends BaseActivity {
    public static final String TAG = "ChangePhoneActivity";

    @BindView(R.id.change_phone_root_layout)
    RelativeLayout mRootView;
    @BindView(R.id.change_phone_back)
    ImageView ivBack;
    @BindView(R.id.new_phone_layout)
    TextInputLayout newPhoneLayout;
    @BindView(R.id.new_phone_verify_code_layout)
    TextInputLayout verifyCodeLayout;
    @BindView(R.id.btn_verify_code)
    TimeButton verifyCodeBtn;
    @BindView(R.id.btn_change_phone_commit)
    Button commitBtn;

    boolean phoneEditFlag, verifyCodeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyCodeBtn.onCreate();
        forbidButton();

        //输入文字时把错误提示关闭
        newPhoneLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s)) {
                    newPhoneLayout.setErrorEnabled(false);
//                    newPhoneLayout.setError("");
                }
                if (RegexUtils.isMobileExact(newPhoneLayout.getEditText().getText()
                        .toString())) {
                    enableButton(true, R.color.white);
                } else {
                    forbidButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verifyCodeLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s)) {
                    verifyCodeLayout.setErrorEnabled(false);
//                    verifyCodeLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //监听键盘弹出
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                mRootView.getWindowVisibleDisplayFrame(rect);
                int invisibleHeight = ScreenUtils.getScreenHeight() - rect.bottom;
                //键盘弹出
                if (invisibleHeight > 100) {
                    commitBtn.layout(0,
                            rect.bottom - commitBtn.getHeight(),
                            ScreenUtils.getScreenWidth(),
                            rect.bottom);
                } else {
                    commitBtn.layout(0,
                            ScreenUtils.getScreenHeight() - commitBtn.getHeight(),
                            ScreenUtils.getScreenWidth(),
                            ScreenUtils.getScreenHeight());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        KeyboardUtils.toggleSoftInput();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (MyUtil.isShouldHideKeyboard(view, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //按钮可用
    private void enableButton(boolean enable, @ColorRes int color) {
        verifyCodeBtn.setEnabled(enable);
        if (Build.VERSION.SDK_INT > 23) {
            verifyCodeBtn.setTextColor(getColor(color));
        } else {
            verifyCodeBtn.setTextColor(getResources().getColor(color));
        }
    }

    //按钮不可用
    private void forbidButton() {
        enableButton(false, R.color.gray);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_phone;
    }

    @OnClick({R.id.change_phone_back, R.id.btn_verify_code, R.id.btn_change_phone_commit})
    public void changeOnclick(View view) {
        switch (view.getId()) {
            case R.id.change_phone_back:
                finish();
                break;
            case R.id.btn_verify_code:
                ToastUtils.showShort("验证码已发送");
                break;
            case R.id.btn_change_phone_commit:
//                newPhoneLayout.setErrorEnabled(true);
//                verifyCodeLayout.setErrorEnabled(true);
                String newPhone2 = newPhoneLayout.getEditText().getText().toString();
                String verifyCode = verifyCodeLayout.getEditText().getText().toString();
                if (EmptyUtils.isEmpty(newPhone2)) {
                    newPhoneLayout.setError("手机号不能为空");
                    return;
                } else {
                    if (!RegexUtils.isMobileExact(newPhone2)) {
                        newPhoneLayout.setError("手机号格式不正确");
                        return;
                    }
                }
                if (EmptyUtils.isEmpty(verifyCode)) {
                    verifyCodeLayout.setError("验证码不能为空");
                    return;
                } else {
                    if (verifyCode.length() != 4) {
                        verifyCodeLayout.setError("验证码是4位的数字");
                        return;
                    }
                }
                ToastUtils.showShort("提交成功");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (MyUtil.isKeyboardShown(mRootView)) {
            KeyboardUtils.toggleSoftInput();
        }
        verifyCodeBtn.onDestroy();
        super.onDestroy();
    }
}
