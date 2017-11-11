package com.yxf.chat.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.EmptyUtils;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.utils.DialogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xiong
 * @name EditBasicInfoActivity
 * @date 2017/7/31
 */
public class EditBasicInfoActivity extends BaseActivity {
    public static final String TAG = "EditBasicInfoActivity";

    @BindView(R.id.ll_edit_sex)
    LinearLayout llEditSex;
    @BindView(R.id.ll_edit_name)
    LinearLayout llEditName;
    @BindView(R.id.ll_edit_birth)
    LinearLayout llEditBirth;
    @BindView(R.id.tv_info_sex)
    TextView sexTv;
    @BindView(R.id.tv_info_name)
    TextView nameTv;
    @BindView(R.id.tv_info_time)
    TextView timeTv;

    //预选择项
    int preSex = -1;
    //名字预填项
    String namePrefill;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_basic_info;
    }

    @OnClick(R.id.edit_basic_info_back)
    void back() {
        finish();
    }

    @OnClick(R.id.ll_edit_sex)
    void sexPicker() {
        //选择性别
        MaterialDialog chooseSexDialog = DialogUtil.showSingleChoice(this, "性别", R.array.sex,
                preSex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which,
                                               CharSequence text) {
                        sexTv.setText(text);
                        preSex = which;
                        return true;
                    }
                });
        chooseSexDialog.show();
    }

    @OnClick(R.id.ll_edit_name)
    void nameSet() {
        //获取已输入的姓名
        String name = nameTv.getText().toString();
        if (name.equals("请输入你的姓名")) {
            namePrefill = "";
        } else {
            namePrefill = name;
        }
        //设置姓名
        MaterialDialog editNameDialog = DialogUtil.showEditText(this, "姓名", "请填写姓名", namePrefill,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (EmptyUtils.isNotEmpty(input.toString())) {
                            nameTv.setText(input);
                        } else {
                            nameTv.setText("请输入你的姓名");
                        }
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
        editNameDialog.show();
    }

    @OnClick(R.id.ll_edit_birth)
    void datePicker() {
        //获取当前年月日
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        //选择生日
        Dialog.Builder datePicker = new DatePickerDialog.Builder(R.style.DatePicker_style,
                1, 1, 1950, 31, 12, 2050, currentDay, currentMonth, currentYear) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dateDialog = (DatePickerDialog) fragment.getDialog();
                timeTv.setText(dateDialog.getFormattedDate(new SimpleDateFormat("yyyy/MM/dd")));
                super.onPositiveActionClicked(fragment);
            }
        }
                .positiveAction(getResources().getString(R.string.confirm))
                .negativeAction(getResources().getString(R.string.cancel));
        //显示出来
        DialogFragment dialogFragment = DialogFragment.newInstance(datePicker);
        dialogFragment.show(getSupportFragmentManager(), null);
    }
}
