package com.yxf.chat.user.security;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.orhanobut.logger.Logger;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.utils.MyUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class VerifyPasswordActivity extends BaseActivity {
    public static final String TAG = "VerifyPasswordActivity";

    @BindView(R.id.rl_verify_password)
    RelativeLayout mRootView;
    @BindView(R.id.verify_password_back)
    ImageView backButton;
    @BindView(R.id.verify_password_layout)
    TextInputLayout mInputLayout;
    @BindView(R.id.et_verify_password)
    TextInputEditText etVerify;
    @BindView(R.id.btn_continue_change_phone)
    Button continueChangePhoneBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //监听键盘弹出
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                mRootView.getWindowVisibleDisplayFrame(rect);
                //获取不可见区域高度
                int invisibleHeight = ScreenUtils.getScreenHeight() - rect.bottom;
                if (invisibleHeight > 100) {
                    Logger.t(TAG).w("键盘弹出");
                    continueChangePhoneBtn.layout(0
                            , rect.bottom - continueChangePhoneBtn.getHeight()
                            , ScreenUtils.getScreenWidth()
                            , rect.bottom);
                } else {
                    Logger.t(TAG).w("键盘收起");
                    continueChangePhoneBtn.layout(0
                            , ScreenUtils.getScreenHeight() - continueChangePhoneBtn.getHeight()
                            , ScreenUtils.getScreenWidth()
                            , ScreenUtils.getScreenHeight());
                }
            }
        });

        //添加编辑栏的监听
        mInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EmptyUtils.isNotEmpty(s)) {
                    mInputLayout.setErrorEnabled(false);
//                    mInputLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        KeyboardUtils.toggleSoftInput();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_password;
    }

    @OnClick({R.id.verify_password_back, R.id.btn_continue_change_phone})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.verify_password_back:
                finish();
                break;
            case R.id.btn_continue_change_phone:
                if (EmptyUtils.isEmpty(etVerify.getText().toString())) {
                    mInputLayout.setError("密码不为空");
                } else {
                    ActivityUtils.startActivity(ChangePhoneActivity.class);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (MyUtil.isKeyboardShown(mRootView)) {
            KeyboardUtils.toggleSoftInput();
        }
        super.onDestroy();
    }
}
