package com.yxf.chat.leftMenuItem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dalong.rangeseekbar.RangeBar;
import com.orhanobut.logger.Logger;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseFragment;
import com.yxf.chat.user.security.ChangePasswordActivity;
import com.yxf.chat.user.security.VerifyPasswordActivity;
import com.yxf.chat.utils.DialogUtil;
import com.yxf.chat.utils.ELS;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 设置界面
 *
 * @author xiong
 * @name SettingFragment
 * @date 2017/8/22
 */
public class SettingFragment extends BaseFragment {
    public static final String TAG = "SettingFragment";

    @BindView(R.id.ll_setting_sex)
    LinearLayout llSex;
    @BindView(R.id.tv_sex_choose)
    TextView tvSexShow;
    @BindView(R.id.tv_setting_distance)
    TextView tvDistance;
    @BindView(R.id.setting_distance_seekBar)
    AppCompatSeekBar distanceSeekBar;
    @BindView(R.id.setting_age_start)
    TextView tvAgeStart;
    @BindView(R.id.setting_age_end)
    TextView tvAgeEnd;
    @BindView(R.id.setting_age_seekBar)
    RangeBar ageRangeBar;
    @BindView(R.id.setting_data_layout)
    LinearLayout dateLayout;
    @BindView(R.id.setting_phone_layout)
    LinearLayout phoneLayout;
    @BindView(R.id.setting_password_layout)
    LinearLayout passwordLayout;
    @BindView(R.id.btn_logout)
    Button logoutButton;
    @BindView(R.id.tv_app_version)
    TextView appVersionTv;

    ELS els;
    private RadioButton[] rbSetSex = new RadioButton[3];
    private LinearLayout[] llSetSex = new LinearLayout[3];
    int[] rbArraySex = {R.id.radio_sex_man, R.id.radio_sex_women, R.id.radio_sex_unlimited};
    int[] llArraySex = {R.id.ll_dialog_man, R.id.ll_dialog_woman, R.id.ll_dialog_unlimited};
    private View dialogSexView;
    int selectSex;
    int distanceInt, ageStartInt, ageEndInt;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        els = ELS.getInstance(getActivity());
        initConfig();
    }

    @Override
    public void onStart() {
        super.onStart();
        getConfig();
    }

    private void getConfig() {
        //设置性别的选择文字
        selectSex = els.getIntData(ELS.SEX_CHOOSE, 0);
        Logger.t(TAG).w("性别选择:" + selectSex);
        switch (selectSex) {
            case 0:
                tvSexShow.setText("男生");
                break;
            case 1:
                tvSexShow.setText("女生");
                break;
            case 2:
                tvSexShow.setText("不限");
                break;
        }

        //设置范围参数
        distanceInt = els.getIntData(ELS.DISTANCE_CHOOSE, 0);
        distanceSeekBar.setProgress(distanceInt);
        tvDistance.setText(distanceInt + "km");
        switch (distanceInt) {
            case 0:
                tvDistance.setText("<1km");
                break;
            case 100:
                tvDistance.setText("100km+");
                break;
        }

        //设置年龄参数
        ageStartInt = els.getIntData(ELS.AGE_LEFT, 0);
        ageEndInt = els.getIntData(ELS.AGE_RIGHT, 32);
        tvAgeStart.setText(ageStartInt + 18 + "");
        if (ageEndInt == 32) {
            tvAgeEnd.setText(ageEndInt + 18 + "+");
        } else {
            tvAgeEnd.setText(ageEndInt + 18 + "");
        }
        ageRangeBar.setThumbIndices(ageStartInt, ageEndInt);
    }

    //初始化一些显示的参数
    private void initConfig() {
        getConfig();

        //距离SeekBar的拖动事件
        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDistance.setText(progress + "km");
                switch (progress) {
                    case 0:
                        tvDistance.setText("<1km");
                        break;
                    case 100:
                        tvDistance.setText("100km+");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //年龄SeekBar的拖动事件
        ageRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int
                    rightThumbIndex) {
                Logger.t(TAG).w("左侧位置:" + leftThumbIndex + "  右侧位置:" + rightThumbIndex);
                //最小岁数为18,RangeBar最小下标为0
                tvAgeStart.setText(leftThumbIndex + 18 + "");
                if (rightThumbIndex == 32) {
                    tvAgeEnd.setText(rightThumbIndex + 18 + "+");
                } else {
                    tvAgeEnd.setText(rightThumbIndex + 18 + "");
                }
            }
        });

        //版本名设置下划线
        appVersionTv.setText(new SpanUtils().appendLine(appVersionTv.getText()).setUnderline()
                .create());
    }

    @OnClick({R.id.ll_setting_sex, R.id.setting_data_layout, R.id.setting_phone_layout, R.id
            .setting_password_layout, R.id.btn_logout})
    public void layoutClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_sex:
                //选择性别
                chooseSex();
                break;
            case R.id.setting_data_layout:
                //清除缓存
                showClearCache();
                break;
            case R.id.setting_phone_layout:
                //更改手机
                ActivityUtils.startActivity(VerifyPasswordActivity.class);
                break;
            case R.id.setting_password_layout:
                //更改密码
                ActivityUtils.startActivity(ChangePasswordActivity.class);
                break;
            case R.id.btn_logout:
                //退出登录
                logout();
                break;
        }
    }

    //选择性别
    private void chooseSex() {
        dialogSexView = LayoutInflater.from(getActivity()).inflate(R.layout
                .dialog_setting_sex, null);
        for (int i = 0; i < rbSetSex.length; i++) {
            rbSetSex[i] = (RadioButton) dialogSexView.findViewById(rbArraySex[i]);
            rbSetSex[i].setClickable(false);
            llSetSex[i] = (LinearLayout) dialogSexView.findViewById(llArraySex[i]);
        }
        final AlertDialog chooseSexDialog = new AlertDialog.Builder(getActivity())
                .setTitle("显示性别")
                .setView(dialogSexView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        chooseSexDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                for (int i = 0; i < 3; i++) {
                    if (selectSex == i) {
                        rbSetSex[i].setChecked(true);
                    }
                }
            }
        });
        chooseSexDialog.show();

        for (int i = 0; i < llSetSex.length; i++) {
            final int finalI = i;
            llSetSex[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保存性别选择
                    els.saveIntData(ELS.SEX_CHOOSE, finalI);
                    chooseSexDialog.dismiss();
                    for (int j = 0; j < rbSetSex.length; j++) {
                        rbSetSex[j].setChecked(false);
                    }
                    rbSetSex[finalI].setChecked(true);
                    String selectSexStr = ((TextView) llSetSex[finalI].getChildAt(1))
                            .getText().toString();
                    tvSexShow.setText(selectSexStr);
                }
            });
        }
    }

    //清除缓存
    private void showClearCache() {
        MaterialDialog clearDataDialog = DialogUtil.showPositiveNegativeContent(getActivity(),
                "清除缓存", "确认清除?", new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        switch (which) {
                            case POSITIVE:
                                ToastUtils.showShort("清除完毕");
                                break;
                        }
                    }
                });
        clearDataDialog.show();
    }

    //退出登录
    private void logout() {
        MaterialDialog logoutDialog = DialogUtil.showPositiveNegativeContent(getActivity(),
                "退出登录", "确定要退出吗?", new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which) {
                            case POSITIVE:
                                ToastUtils.showShort("退出登录");
                                break;
                        }
                    }
                });
        logoutDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        //暂停时保存距离参数
        els.saveIntData(ELS.DISTANCE_CHOOSE, distanceSeekBar.getProgress());
        //暂停时保存年龄参数
        els.saveIntData(ELS.AGE_LEFT, ageRangeBar.getLeftIndex());
        els.saveIntData(ELS.AGE_RIGHT, ageRangeBar.getRightIndex());
    }
}
