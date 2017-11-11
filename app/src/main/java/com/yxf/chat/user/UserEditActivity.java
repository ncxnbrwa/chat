package com.yxf.chat.user;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.EmptyUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.orhanobut.logger.Logger;
import com.soundcloud.android.crop.Crop;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.user.customView.DraggableSquareView;
import com.yxf.chat.utils.DialogUtil;
import com.yxf.chat.utils.MyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserEditActivity extends BaseActivity {
    private static final String TAG = "UserEditActivity";

    @BindView(R.id.drag_pic_square)
    DraggableSquareView dragPicSquare;
    @BindView(R.id.tv_user_sign)
    TextView signTv;
    @BindView(R.id.user_profession_details)
    TextView professionTv;
    @BindView(R.id.user_company)
    TextView companyTv;
    @BindView(R.id.user_province)
    TextView provinceTv;
    @BindView(R.id.user_local)
    TextView localTv;
    @BindView(R.id.label_null_tv)
    TextView labelNull;
    @BindView(R.id.label_layout)
    FlexboxLayout labelLayout;
    private int imageStatus;
    private boolean isModify;
    //是否修改过的标记
    private boolean isChanged;
    // TODO: 2017/9/1 添加修改信息标记
    //签名的填充文本
    String finalSignFill;
    //公司填充文本
    String finalCompanyFill;
    //所在地填充文本
    String finalLocalFill;
    //标签集合
    List<String> labelList;
    List<Integer> checkList;
    //存放预设的选项
    Integer[] checkIndexArray;
    MaterialDialog labelDialog;
//    List<String> uncheckList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //必须初始化图片加载器
        initImageLoader();
        super.onCreate(savedInstanceState);
        // TODO: 2017/9/4  获取已有标签
        //获取数据初始化标签
        labelNull.setVisibility(View.GONE);
        if (labelList == null) {
            labelList = new ArrayList<>();
        }
        if (checkList == null) {
            checkList = new ArrayList<>();
        }
        labelList.add("运动健身");
        labelList.add("影视");
        labelList.add("吃美食");
        labelList.add("音乐");
        labelList.add("玩游戏");
        checkList.add(0);
        checkList.add(1);
        checkList.add(2);
        checkList.add(3);
        checkList.add(6);
        //初始化已选项
        if (checkIndexArray == null) {
            checkIndexArray = new Integer[checkList.size()];
        }
        for (int i = 0; i < checkList.size(); i++) {
            checkIndexArray[i] = checkList.get(i);
        }
        //添加标签视图
        addLabelView();

        //标签选择的对话框需要重复用一个实例
        labelDialog = DialogUtil.showCheckbox(this, "个性标签", R.array.label,
                checkIndexArray, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which,
                                               CharSequence[] text) {
                        checkList.clear();
                        labelList.clear();
                        labelLayout.removeAllViews();
                        //which代表选中项的下标,text代表选中项的文字
                        if (which.length != 0) {
                            for (int i = 0; i < which.length; i++) {
                                //保存已选项下标
                                checkList.add(which[i]);
                                //保存已选项文字
                                labelList.add(text[i].toString());
                            }
                            addLabelView();
                        }
                        //决定无标签的视图是否出现
                        //因为前面移除了全部View,所以需要重新添加
                        labelLayout.addView(labelNull);
                        if (labelList.size() == 0) {
                            labelNull.setVisibility(View.VISIBLE);
                        } else {
                            labelNull.setVisibility(View.GONE);
                        }
                        Logger.t(TAG).w("选中项:" + checkList.toString());
                        return true;
                    }
                });
        Logger.t(TAG).w("labelList:" + labelList.toString());
        Logger.t(TAG).w("checkList:" + checkList.toString());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_edit;
    }

    //给自定义dragItem使用
    public void pickImage(int imageStatus, boolean isModify) {
        this.imageStatus = imageStatus;
        this.isModify = isModify;
        //选好照片,跳去裁剪
        Crop.pickImage(this);
    }

    //初始化图片加载器
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(this))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs().build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination;
        if (Build.VERSION.SDK_INT < 24) {
            destination = Uri.fromFile(new File(getCacheDir(), "cropped_" + System
                    .currentTimeMillis() + ".jpg"));
        } else {
            destination = FileProvider.getUriForFile(this, "com.yxf.chat.fileProvider",
                    new File(getCacheDir(), "cropped_"
                            + System.currentTimeMillis() + ".jpg"));
        }
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imagePath = uri.toString();
            dragPicSquare.fillItemImage(imageStatus, imagePath, isModify);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getAbsolutePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @OnClick(R.id.user_edit_back)
    void back() {
        finish();
    }

    @OnClick(R.id.user_info_layout)
    void editUserInfo() {
        toActivity(EditBasicInfoActivity.class, null);
    }

    //编辑个人签名
    @OnClick(R.id.ll_sign)
    void editSign() {
        //确定预加载文字
        String prefill = signTv.getText().toString();
        if (prefill.equals("+ 添加你的个人签名")) {
            finalSignFill = "";
        } else {
            finalSignFill = prefill;
        }
        MaterialDialog editDialog = DialogUtil.showEditText(this, "个人签名", "添加你的个人签名",
                finalSignFill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //输入后点击确定的回调,input为输入的内容
                        if (EmptyUtils.isNotEmpty(input.toString())) {
                            signTv.setText(input);
                            signTv.setTextColor(getResources().getColor(R.color
                                    .primary_text_color));
                        } else {
                            signTv.setText("+ 添加你的个人签名");
                            signTv.setTextColor(getResources().getColor(R.color
                                    .secondary_text_color));
                        }
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //关闭对话框时检测文本是否改变
                        if (signTv.getText().toString().equals(finalSignFill)) {
                            isChanged = false;
                        } else {
                            isChanged = true;
                        }
                    }
                });
        editDialog.show();
    }

    //选择行业
    @OnClick(R.id.ll_profession)
    void chooseProfession() {
        MaterialDialog chooseProfessionDialog = DialogUtil.showListDialog(this, "行业", "清空",
                R.array.profession, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which,
                                            CharSequence text) {
                        professionTv.setText(text);
                        professionTv.setTextColor(getResources().getColor(R.color
                                .primary_text_color));
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        switch (which) {
                            //清空选项的操作
                            case POSITIVE:
                                professionTv.setText("+ 添加你的行业信息");
                                professionTv.setTextColor(getResources().getColor(R.color
                                        .secondary_text_color));
                                break;
                        }
                    }
                });
        chooseProfessionDialog.show();
    }

    //选择省份
    @OnClick(R.id.ll_company)
    void editCompany() {
        //最后公司预加载的判定
        String companyFill = companyTv.getText().toString();
        if (companyFill.equals("+ 添加公司信息")) {
            finalCompanyFill = "";
        } else {
            finalCompanyFill = companyFill;
        }
        //编辑公司的对话框
        MaterialDialog companyDialog = DialogUtil.showEditText(this, "公司", "添加公司信息",
                finalCompanyFill,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //输入文本的回调,设置给对应TextView
                        if (EmptyUtils.isNotEmpty(input.toString())) {
                            companyTv.setText(input);
                            companyTv.setTextColor(getResources().getColor(R.color
                                    .primary_text_color));
                        } else {
                            companyTv.setText("+ 添加公司信息");
                            companyTv.setTextColor(getResources().getColor(R.color
                                    .secondary_text_color));
                        }
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO: 2017/9/1 设置省份选择对话框消失事件,设置flag
                    }
                });
        companyDialog.show();
    }

    //选择家乡
    @OnClick(R.id.ll_province)
    void chooseProvince() {
        MaterialDialog provinceDialog = DialogUtil.showListDialog(this, "来自", "清空",
                R.array.province, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which,
                                            CharSequence text) {
                        provinceTv.setTextColor(getResources().getColor(R.color
                                .primary_text_color));
                        if (which == 0) {
                            //自定义标签编辑对话框
                            MaterialDialog customProvinceDialog = DialogUtil.showEditText
                                    (UserEditActivity.this,
                                            "来自", "添加你的家乡信息", "",
                                            new MaterialDialog.InputCallback() {
                                                @Override
                                                public void onInput(@NonNull MaterialDialog dialog,
                                                                    CharSequence input) {
                                                    if (EmptyUtils.isNotEmpty(input.toString())) {
                                                        provinceTv.setText(input);
                                                    }
                                                }
                                            }, new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                }
                                            });
                            customProvinceDialog.show();
                        } else {
                            provinceTv.setText(text);
                        }
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        switch (which) {
                            //清空选项的操作
                            case POSITIVE:
                                provinceTv.setText("+ 添加你的家乡信息");
                                provinceTv.setTextColor(getResources().getColor(R.color
                                        .secondary_text_color));
                                break;
                        }
                    }
                });
        provinceDialog.show();
    }

    //所在地编辑
    @OnClick(R.id.ll_local)
    void editLocal() {
        String localFill = localTv.getText().toString();
        if (localFill.equals("+ 添加你常去的地点")) {
            finalLocalFill = "";
        } else {
            finalLocalFill = localFill;
        }
        MaterialDialog localDialog = DialogUtil.showEditText(this, "所在地", "添加你的所在地", finalLocalFill,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (EmptyUtils.isNotEmpty(input.toString())) {
                            localTv.setText(input);
                            localTv.setTextColor(getResources().getColor(R.color
                                    .primary_text_color));
                        } else {
                            localTv.setText("+ 添加你常去的地点");
                            localTv.setTextColor(getResources().getColor(R.color
                                    .secondary_text_color));
                        }
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO: 2017/9/1 所在的对话框消失事件,设置flag
                    }
                });
        localDialog.show();
    }

    //标签布局点击事件
    @OnClick(R.id.label_layout)
    void editLabel() {
//        setCheckIndex();
        labelDialog.show();
    }

    //设置选择对话框选项选中
    private void setCheckIndex() {
        for (Integer index : checkList) {
            //设置已选项
            labelDialog.setSelectedIndex(index);
        }
    }

    //添加标签并设置相应参数
    private void addLabelView() {
        //遍历集合创建TextView
        for (String list : labelList) {
            labelLayout.addView(MyUtil.createLabel(this, list));
        }
        //给字View设置flex专属属性
        for (int i = 0; i < labelLayout.getChildCount(); i++) {
            View view = labelLayout.getChildAt(i);
            FlexboxLayout.LayoutParams flexLp = (FlexboxLayout.LayoutParams) view
                    .getLayoutParams();
            flexLp.setMargins(10, 6, 10, 6);
            view.setLayoutParams(flexLp);
        }
    }
}
