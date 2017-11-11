package com.yxf.chat.user.customView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.yxf.chat.R;

/**
 * 自定义照片操作对话框
 *
 * @author xiong
 * @name CustomDialog
 * @date 2017/8/19
 */
public class CustomDialog extends AlertDialog {

    private Button button1, button2;
    private View.OnClickListener btnListener;
    private View.OnClickListener innerListener;

    protected CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickListener(View.OnClickListener btnListener) {
        this.btnListener = btnListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_picture);

        innerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnListener.onClick(v);
                dismiss();
            }
        };

        findViewById(R.id.pick_image).setOnClickListener(innerListener);
        findViewById(R.id.delete).setOnClickListener(innerListener);
    }
}
