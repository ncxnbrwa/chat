package com.yxf.chat.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseFragment;
import com.yxf.chat.home.PicassoImageLoader;
import com.yxf.chat.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 个人资料界面,点击头像进入
 *
 * @author xiong
 * @name UserFragment
 * @date 2017/8/19
 */
public class UserFragment extends BaseFragment {
    @BindView(R.id.user_banner)
    Banner banner;
    @BindView(R.id.tv_user_name)
    TextView userNameTv;
    @BindView(R.id.tv_user_age_sex)
    TextView userSexTv;
    @BindView(R.id.tv_constellation)
    TextView constellationTv;
    @BindView(R.id.tv_user_sign)
    TextView signTv;
    @BindView(R.id.user_profession_details)
    TextView professionTv;
    @BindView(R.id.user_company)
    TextView companyTv;
    @BindView(R.id.user_province)
    TextView comeFromTv;
    @BindView(R.id.user_local)
    TextView localTv;
    @BindView(R.id.iv_right_arrow)
    ImageView rightIv;
    @BindView(R.id.tv_edit_hint)
    TextView editHint;
    @BindView(R.id.label_null_tv)
    TextView labelNull;
    @BindView(R.id.label_layout)
    FlexboxLayout labelLayout;
    List<String> labelList;

    List<String> imageUrls = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rightIv.setVisibility(View.GONE);
        editHint.setVisibility(View.GONE);
        labelNull.setVisibility(View.VISIBLE);
        if (labelList == null) {
            labelList = new ArrayList<>();
        }
        labelList.add("运动健身");
        labelList.add("影视");
        labelList.add("吃美食");
        labelList.add("音乐");
        labelList.add("玩游戏");
        if (labelList.size() != 0) {
            addLabelView();
            labelNull.setVisibility(View.GONE);
        } else {
            labelNull.setVisibility(View.VISIBLE);
        }
        initData();
        //设置UI数据
        setUI();
    }

    //模拟一组图片数据
    private void initData() {
        imageUrls.add("http://i1.cfimg.com/591992/be1f3aec52f9f6ea.jpg");
        imageUrls.add("http://i1.cfimg.com/591992/458334c766eeca5c.jpg");
        imageUrls.add("http://i1.cfimg.com/591992/6822107bdcd5b8a0.jpg");
        //轮播的一些初始化操作
        initBanner();
    }

    //轮播的一些初始化操作
    private void initBanner() {
        banner.setImageLoader(new PicassoImageLoader());
        banner.setImages(imageUrls);
        banner.isAutoPlay(false);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                getBaseActivity().toActivity(UserEditActivity.class, null);
            }
        });
        banner.start();
    }

    //设置UI
    private void setUI() {
//        userNameTv.setText();
//        userSexTv.setText();
//        constellationTv.setText();
//        signTv.setText();
//        professionTv.setText();
//        companyTv.setText();
//        comeFromTv.setText();
//        localTv.setText();
    }

    //添加标签并设置相应参数
    private void addLabelView() {
        //遍历集合创建TextView
        for (String list : labelList) {
            labelLayout.addView(MyUtil.createLabel(getBaseActivity(), list));
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
