package com.yxf.chat.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;

import butterknife.BindView;

/**
 * 注册界面
 *
 * @author xiong
 * @name RegisterActivity
 * @date 2017/8/19
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String pwd = etPwd.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pwd.equals("")) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }
}
