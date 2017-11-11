package com.yxf.chat.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录界面
 *
 * @author xiong
 * @name LoginActivity
 * @date 2017/8/19
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    //跳转注册页面
    @OnClick(R.id.tv_register)
    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    //登录方法
    @OnClick(R.id.btn_login)
    public void login(View view) {
        String name = etName.getText().toString();
        String pwd = etPwd.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

//    //连接融云
//    private void connect(String token) {
//        if (getApplicationInfo().packageName.equals(ChatApplication.getCurProcessName
//                (getApplicationContext()))) {
//
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//
//                /**
//                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
//                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
//                 */
//                @Override
//                public void onTokenIncorrect() {
//
//                }
//
//                /**
//                 * 连接融云成功
//                 * @param userid 当前 token 对应的用户 id
//                 */
//                @Override
//                public void onSuccess(String userid) {
//                    Log.d("LoginActivity", "--onSuccess" + userid);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                }
//
//                /**
//                 * 连接融云失败
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 */
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//
//                }
//            });
//        }
//    }

}
