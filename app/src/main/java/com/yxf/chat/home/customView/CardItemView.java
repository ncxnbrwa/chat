package com.yxf.chat.home.customView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.google.android.flexbox.FlexboxLayout;
import com.orhanobut.logger.Logger;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.yxf.chat.R;
import com.yxf.chat.home.CardDataBean;
import com.yxf.chat.home.PicassoImageLoader;
import com.yxf.chat.utils.MyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jeremyfeinstein.slidingmenu.lib.app.PublicData.isCanSlide;


/**
 * 卡片View的item项
 *
 * @author xiong
 * @name CardItemView
 * @date 2017/8/19
 */
@SuppressLint("NewApi")
public class CardItemView extends FrameLayout {
    public static final String TAG = "CardItemView";
    private CardSlidePanel parentView;
    private Spring springX, springY;
    public Banner banner;
    TextView userNameTv, picCountTv, ageAndSexTv, userConstellationTv, userProfessionTv,
            userSignTv, userProfessionDetailTv, userCompanyTv, userHomeTv, userLocationTv;
    LinearLayout detailLayout;
    AutoScaleRelativeLayout topLayout;
    RelativeLayout bottomLayout;
    ScrollView mScrollView;
    FlexboxLayout labelLayout;
    List<String> labelList;

    //动画
    ValueAnimator showDetailAnimator;
    ValueAnimator hideDetailAnimator;

    // 是否初始化banner
    public static boolean isInitBanner;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.item_swipe_card2, this);
//        inflate(context, R.layout.item_swipe_card, this);
        banner = (Banner) findViewById(R.id.card_banner);
        picCountTv = (TextView) findViewById(R.id.card_pic_num);
        userNameTv = (TextView) findViewById(R.id.tv_user_name);
        ageAndSexTv = (TextView) findViewById(R.id.tv_user_age_sex);
        userConstellationTv = (TextView) findViewById(R.id.tv_user_constellation);
        userProfessionTv = (TextView) findViewById(R.id.tv_user_profession);
        userSignTv = (TextView) findViewById(R.id.tv_user_sign);
        userProfessionDetailTv = (TextView) findViewById(R.id.tv_user_profession_detail);
        userCompanyTv = (TextView) findViewById(R.id.tv_company);
        userHomeTv = (TextView) findViewById(R.id.tv_user_home);
        userLocationTv = (TextView) findViewById(R.id.tv_location);
        detailLayout = (LinearLayout) findViewById(R.id.need_hide_layout);
        topLayout = (AutoScaleRelativeLayout) findViewById(R.id.card_top_layout);
        bottomLayout = (RelativeLayout) findViewById(R.id.card_bottom_layout);
        mScrollView = (ScrollView) findViewById(R.id.scroll_root);
        labelLayout = (FlexboxLayout) findViewById(R.id.label_layout);
        detailLayout.setVisibility(View.GONE);
        if (labelList == null)
            labelList = new ArrayList<>();
        initSpring();
    }

    //填充主页卡片数据
    public void fillData(CardDataBean itemData) {
        //设置图片加载器,内含图片加载方式
        banner.setImageLoader(new PicassoImageLoader());
        //设置轮播图片
        banner.setImages(itemData.getUrlList());
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置UI
        userNameTv.setText(itemData.getName());
        picCountTv.setText(itemData.getUrlList().size() + "");
        String sex = itemData.getSex();
        if (sex.equals("男")) {
            ageAndSexTv.setText("♂ " + MyUtil.getAge(itemData.getBirthday()));
        } else if (sex.equals("女")) {
            ageAndSexTv.setText("♀ " + MyUtil.getAge(itemData.getBirthday()));
        }
        userConstellationTv.setText(TimeUtils.getZodiac(itemData.getBirthday(),
                new SimpleDateFormat("yyyy-MM-dd")));
        userProfessionTv.setText(itemData.getProfession());
//        userSignTv.setText();
//        userProfessionDetailTv.setText();
//        userCompanyTv.setText();
//        userHomeTv.setText();
//        userLocationTv.setText();
        //设置个人标签
        labelList.clear();
        labelList.add("运动健身");
        labelList.add("影视");
        labelList.add("吃美食");
        labelList.add("音乐");
        labelList.add("玩游戏");
        addLabelView();

        banner.start();
        //轮播点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (isCanSlide) {
                    showDetailLayout();
                } else {
                    hideDetailLayout();
                }

            }
        });
    }

    //添加标签并设置相应参数
    private void addLabelView() {
        labelLayout.removeAllViews();
        //遍历集合创建TextView
        for (String list : labelList) {
            labelLayout.addView(MyUtil.createLabel(getContext(), list));
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

    //显示详细信息
    private void showDetailLayout() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        // 发送广播通知主界面进行隐藏toolbar操作以及按钮隐藏
        getContext().sendBroadcast(new Intent("hideBar"));
        getContext().sendBroadcast(new Intent("hideCard"));
        //设置卡片点击放大动画
        showDetailAnimator = ValueAnimator.ofInt(20, 0).setDuration(300);
        showDetailAnimator.addUpdateListener(new ShowDetailAnimUpdateListener());
        showDetailAnimator.addListener(new ShowDetailAnimListener());
        showDetailAnimator.start();
    }

    //隐藏详细信息
    private void hideDetailLayout() {
        //卡片滚动到顶部
//        mScrollView.scrollTo(0, 0);
        mScrollView.fullScroll(View.FOCUS_UP);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources
                ().getDimension(R.dimen.card_height)));
        Logger.t(TAG).w("card_height:" + getHeight());
        // 发送广播通知主界面进行展示toolbar操作以及显示按钮
        getContext().sendBroadcast(new Intent("showBar"));
        getContext().sendBroadcast(new Intent("showCard"));
        //设置点击变回卡片动画
        hideDetailAnimator = ValueAnimator.ofInt(0, 20).setDuration(300);
        hideDetailAnimator.addUpdateListener(new HideDetailAnimUpdateListener());
        hideDetailAnimator.addListener(new HideDetailAnimListener());
        hideDetailAnimator.start();
    }

    //弹性滑动初始化
    private void initSpring() {
        SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(15, 20);
        //创建一个Spring系统
        SpringSystem mSpringSystem = SpringSystem.create();
        //分别设置X轴和Y轴的Spring
        springX = mSpringSystem.createSpring().setSpringConfig(springConfig);
        springY = mSpringSystem.createSpring().setSpringConfig(springConfig);

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });
    }

    /**
     * 动画移动到某个位置
     */
    public void animTo(int xPos, int yPos) {
        setCurrentSpringPos(getLeft(), getTop());
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }

    /**
     * 设置当前spring位置
     */
    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    public void setParentView(CardSlidePanel parentView) {
        this.parentView = parentView;
    }

    public void onStartDragging() {
        springX.setAtRest();
        springY.setAtRest();
    }

    /**
     * 判断(x, y)是否在可滑动的矩形区域内
     * 这个函数也被CardSlidePanel调用
     *
     * @param x 按下时的x坐标
     * @param y 按下时的y坐标
     * @return 是否在可滑动的矩形区域
     */
    public boolean shouldCapture(int x, int y) {
        int captureLeft = getLeft() + topLayout.getPaddingLeft();
        int captureTop = getTop() + topLayout.getTop() + topLayout.getPaddingTop();
        int captureRight = getRight() - bottomLayout.getPaddingRight();
        int captureBottom = getBottom() - getPaddingBottom() - bottomLayout.getPaddingBottom();

        if (x > captureLeft && x < captureRight && y > captureTop && y < captureBottom) {
            return true;
        }
        return false;
    }

    //依赖的Activity销毁时调用
    @Override
    protected void onDetachedFromWindow() {
        //关闭对应动画,防止内存泄漏
        if (showDetailAnimator != null && showDetailAnimator.isRunning())
            showDetailAnimator.cancel();
        if (hideDetailAnimator != null && hideDetailAnimator.isRunning())
            hideDetailAnimator.cancel();
        super.onDetachedFromWindow();
    }

    private class ShowDetailAnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int currentValue = (int) animation.getAnimatedValue();
            float fraction = animation.getAnimatedFraction();
            Logger.t(TAG).w("展现动画数值:" + currentValue);
            Logger.t(TAG).w("展现动画比例:" + fraction);
            topLayout.setPadding(currentValue, currentValue, currentValue, 0);
            // Card底部部分细节设置
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    bottomLayout.getLayoutParams();
            layoutParams.setMargins(currentValue, 0, currentValue, currentValue);
            bottomLayout.setLayoutParams(layoutParams);
            //设置底部布局透明度渐变
            detailLayout.setAlpha(fraction);
        }
    }

    private class ShowDetailAnimListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            bottomLayout.setBackgroundResource(R.drawable.bottom_show);
            //Panel不可滑动
            isCanSlide = false;
            picCountTv.setVisibility(View.GONE);
            // 指示器样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置轮播可手动滑动
            banner.setViewPagerIsScroll(true);

            if (!isInitBanner) {
                banner.start();
                isInitBanner = true;
            } else {
                banner.changeBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            }
//            detailLayout.setAlpha(1.0f);
            detailLayout.setVisibility(View.VISIBLE);

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private class HideDetailAnimUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int currentValue = (int) animation.getAnimatedValue();
            float fraction = animation.getAnimatedFraction();
            Logger.t(TAG).w("隐藏动画比例:" + (1.0f - fraction));
            // Card头部部分细节设置
            topLayout.setPadding(currentValue, currentValue, currentValue, 0);
            // Card底部细节设置
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    bottomLayout.getLayoutParams();
            layoutParams.setMargins(currentValue, 0, currentValue, 0);
            bottomLayout.setLayoutParams(layoutParams);
            //设置底部布局透明度渐变
            detailLayout.setAlpha(1.0f - fraction);
        }
    }

    private class HideDetailAnimListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            bottomLayout.setBackgroundResource(R.drawable.bottom);
            Logger.t(TAG).w("底部布局高度:" + bottomLayout.getHeight());
            // 设置CardSlidePanel可滑动
            isCanSlide = true;
            picCountTv.setVisibility(View.VISIBLE);
            //设置轮播指示器样式消失
            banner.changeBannerStyle(BannerConfig.NOT_INDICATOR);
            //设置轮播不可手动滑动
            banner.setViewPagerIsScroll(false);
            detailLayout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
