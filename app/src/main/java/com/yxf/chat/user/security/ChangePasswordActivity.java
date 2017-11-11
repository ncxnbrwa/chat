package com.yxf.chat.user.security;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.utils.MyUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {
    public static final String TAG = "ChangePasswordActivity";

    @BindView(R.id.change_password_root)
    RelativeLayout mRoot;
    @BindView(R.id.old_password_layout)
    TextInputLayout oldPasswordLayout;
    @BindView(R.id.new_password_layout)
    TextInputLayout newPasswordLayout;
    @BindView(R.id.confirm_password_layout)
    TextInputLayout confirmPasswordLayout;
    @BindView(R.id.btn_change_password)
    Button changeDoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //输入监听
        oldPasswordLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s)) {
                    oldPasswordLayout.setErrorEnabled(false);
//                    oldPasswordLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPasswordLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s)) {
                    newPasswordLayout.setErrorEnabled(false);
//                    newPasswordLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPasswordLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s)) {
                    confirmPasswordLayout.setErrorEnabled(false);
//                    confirmPasswordLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //键盘监听
        mRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                mRoot.getWindowVisibleDisplayFrame(rect);
                int invisibleHeight = ScreenUtils.getScreenHeight() - rect.bottom;
                if (invisibleHeight > 100) {
                    changeDoneBtn.layout(0,
                            rect.bottom - changeDoneBtn.getHeight(),
                            ScreenUtils.getScreenWidth(),
                            rect.bottom);
                } else {
                    changeDoneBtn.layout(0,
                            ScreenUtils.getScreenHeight() - changeDoneBtn.getHeight(),
                            ScreenUtils.getScreenWidth(),
                            ScreenUtils.getScreenHeight());
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //点击空白处可隐藏键盘
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

    @Override
    protected void onStart() {
        super.onStart();
        KeyboardUtils.toggleSoftInput();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @OnClick({R.id.change_password_back, R.id.btn_change_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password_back:
                finish();
                break;
            case R.id.btn_change_password:
                String oldPassword = oldPasswordLayout.getEditText().getText().toString();
                String newPassword = newPasswordLayout.getEditText().getText().toString();
                String confirmPassword = confirmPasswordLayout.getEditText().getText().toString();
                if (EmptyUtils.isNotEmpty(oldPassword)) {
                    if (EmptyUtils.isNotEmpty(newPassword)) {
                        if (EmptyUtils.isNotEmpty(confirmPassword)) {
                            if (newPassword.equals(confirmPassword)) {
                                ToastUtils.showShort("修改完成");
                            } else {
                                confirmPasswordLayout.setError("两次输入不一致");
                            }
                        } else {
                            confirmPasswordLayout.setError("密码确认为空");
                        }
                    } else {
                        newPasswordLayout.setError("新密码为空");
                    }
                } else {
                    oldPasswordLayout.setError("旧密码为空");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (MyUtil.isKeyboardShown(mRoot)) {
            KeyboardUtils.toggleSoftInput();
        }
        super.onDestroy();
    }
}
