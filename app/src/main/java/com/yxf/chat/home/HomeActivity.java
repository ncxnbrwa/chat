package com.yxf.chat.home;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;
import com.yxf.chat.appBase.ChatApplication;
import com.yxf.chat.appBase.ChatConfig;
import com.yxf.chat.leftMenuItem.FriendLinkFragment;
import com.yxf.chat.leftMenuItem.SettingFragment;
import com.yxf.chat.leftMenuItem.WebLinkActivity;
import com.yxf.chat.user.UserEditActivity;
import com.yxf.chat.user.UserFragment;
import com.yxf.chat.userPair.AllPairFragment;
import com.yxf.chat.utils.ELS;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles
        .ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 主页
 *
 * @author xiong
 * @name HomeActivity
 * @date 2017/8/15
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title_left_menu)
    ImageView ivLeftMenu;
    //标题右边图标
    @BindView(R.id.title_right_menu)
    ImageView ivRightMenu;
    @BindView(R.id.fl_fragment)
    FrameLayout fl_container;
    // 标题文本
    @BindView(R.id.title_name)
    TextView tvTitleName;
    private SlidingMenu slidingMenu;

    private static final String TAG = "HomeActivity";
    ELS els;
    // 标题高度
    private double titleHeight;
    private int module = 0;

    private int titleRightState = ChatConfig.TITLE_RIGHT_IMAGE_IS_MENU; // 标题栏右边图标状态

    LinearLayout ll_left_item[] = new LinearLayout[ChatConfig.LEFT_MENU_ITEM_COUNT];  //
    // slidingmenu左侧菜单栏item项
    ImageView iv_left_icon[] = new ImageView[ChatConfig.LEFT_MENU_ITEM_COUNT];    // 左侧菜单栏图标
    TextView tv_left_title[] = new TextView[ChatConfig.LEFT_MENU_ITEM_COUNT]; // 左侧菜单栏标题

    private LinearLayout llIconLayout;  // 用户头像部分
    private TextView tvName;

    private int left_menu_icon[] = {R.drawable.menu_find, R.drawable.menu_setting, R.drawable.mall,
            R.drawable.menu_share, R.drawable.menu_share};
    private int left_menu_icon_pressed[] = {R.drawable.menu_find1, R.drawable.menu_setting1, R
            .drawable.mall_1, R.drawable.menu_share1, R.drawable.menu_share1};

    //定位功能参数
//    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();

    private ConversationListFragment mConversationFragment;
    private UserFragment userFragment;
    private Fragment fragments[] = {new FindFragment(), new SettingFragment(), null,
            new FriendLinkFragment()};
    //屏幕宽度
    int screenWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenWidth = ScreenUtils.getScreenWidth();
        Logger.w(TAG, "屏幕宽:" + screenWidth);
        els = ELS.getInstance(this);

        // 初始化两边侧滑
        initSlidingMenu();
        // 初始化左侧菜单
        initLeftMenu();
        // 初始化右侧菜单栏
        initRightMenu();
        // 声明LocationClient类
//        mLocationClient = new LocationClient(ChatApplication.getContext());
        // 注册监听函数
//        mLocationClient.registerLocationListener(myListener);
        // 初始化定位
//        initLocation();
        // 开始定位
//        mLocationClient.start();
        // 初始化碎片
        initFragment();

        //获取Token
//        APIConstant getTokenClient = ServiceGenerator.createService(APIConstant.class);
//        Call<TokenBean> getToken = getTokenClient.getToken("12306", "安卓大佬",
//                "http://i4.nbimg.com/591992/0c437a3d83bac7b1.jpg");
////        Call<TokenBean> getToken = getTokenClient.getToken(new TokenBody("12306", "安卓大佬",
////                "http://i4.nbimg.com/591992/0c437a3d83bac7b1.jpg"));
//        getToken.enqueue(new Callback<TokenBean>() {
//            @Override
//            public void onResponse(Call<TokenBean> call, Response<TokenBean> response) {
//                TokenBean tokenBean = response.body();
//                if (EmptyUtils.isNotEmpty(tokenBean)) {
//                    Logger.t(TAG).w("token:" + tokenBean.getToken());
//                }
//                Logger.t(TAG).w("call:" + call.toString());
//            }
//
//            @Override
//            public void onFailure(Call<TokenBean> call, Throwable t) {
//                ToastUtils.showShort("获取Token失败");
//            }
//        });
        //手机
        connect(ChatConfig.TEST_TOKEN_110);
        //模拟器
//        connect(ChatConfig.TEST_TOKEN_10086);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    // 初始化两边侧滑
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);

        //设置菜单(导航栏)出现的模式(左右同时有)
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置菜单(导航栏)视图与内容图接壤的边缘区域的宽
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);

        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        // 设置渐入渐出效果的值(没效果)
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.5f);
        //设置与activity关联的模式
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW, true);

        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.layout_left_menu);
        slidingMenu.setSecondaryMenu(R.layout.layout_right_menu);
        //设置菜单(导航栏)的显示宽度
        slidingMenu.setBehindWidth(screenWidth * 3 / 4);
    }

    int lastItem = 0;

    // 初始化左侧菜单
    private void initLeftMenu() {
        //初始化左侧菜单各控件
        llIconLayout = (LinearLayout) findViewById(R.id.ll_user_icon);
        tvName = (TextView) findViewById(R.id.tv_user_name);
        ll_left_item[0] = (LinearLayout) findViewById(R.id.ll_find);
        ll_left_item[1] = (LinearLayout) findViewById(R.id.ll_msg);
        ll_left_item[2] = (LinearLayout) findViewById(R.id.ll_setting);
        ll_left_item[3] = (LinearLayout) findViewById(R.id.ll_friendUrl);
        ll_left_item[4] = (LinearLayout) findViewById(R.id.ll_share);

        iv_left_icon[0] = (ImageView) findViewById(R.id.left_icon_0);
        iv_left_icon[1] = (ImageView) findViewById(R.id.left_icon_1);
        iv_left_icon[2] = (ImageView) findViewById(R.id.left_icon_2);
        iv_left_icon[3] = (ImageView) findViewById(R.id.left_icon_3);
        iv_left_icon[4] = (ImageView) findViewById(R.id.left_icon_share);

        tv_left_title[0] = (TextView) findViewById(R.id.left_title_0);
        tv_left_title[1] = (TextView) findViewById(R.id.left_title_1);
        tv_left_title[2] = (TextView) findViewById(R.id.left_title_2);
        tv_left_title[3] = (TextView) findViewById(R.id.left_title_3);
        tv_left_title[4] = (TextView) findViewById(R.id.left_title_share);

        //设置布局触摸变色
        for (int i = 0; i < ll_left_item.length; i++) {
            final int finalI = i;
            ll_left_item[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int itemNum = -1;  // 触摸状态, 判断是否消费
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //手指按压时改变图片及文字颜色为红色
                            iv_left_icon[finalI].setImageResource(left_menu_icon_pressed[finalI]);
                            tv_left_title[finalI].setTextColor(getResources().getColor(R.color
                                    .pressed_red));
                            ll_left_item[finalI].onTouchEvent(event);
                            itemNum = 1;
                            break;
                        case MotionEvent.ACTION_UP:
                            //手指放开时恢复为白色
                            iv_left_icon[finalI].setImageResource(left_menu_icon[finalI]);
                            tv_left_title[finalI].setTextColor(Color.WHITE);
                            itemNum = 2;
                            break;
                    }
                    if (itemNum == 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            //切换主页fragment事件
            ll_left_item[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.t(TAG).w("当前fragment下标:" + finalI);
                    Logger.t(TAG).w("上次fragment下标:" + lastItem);
                    if (finalI == ll_left_item.length - 1) {
                        //最后一个是分享
                        ToastUtils.showShort("正在分享");
                    } else if (finalI == 2) {
                        //跳转网页加载页面
                        Bundle bundle = new Bundle();
                        bundle.putString(ChatConfig.LOAD_URL, ChatConfig.HAN_SHENG_CAO);
                        toActivity(WebLinkActivity.class, bundle);
                    } else {
                        //设置标志位防止重复点击添加栈
                        if (lastItem != finalI) {
                            //提交fragment事务
                            addFragmentStack(fragments[finalI]);
                            //改变标题
                            String title = ((TextView) ll_left_item[finalI].getChildAt(1))
                                    .getText()
                                    .toString();
                            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                            tvTitleName.setText(title);
                            setTitleRightEnable(true);
                            lastItem = finalI;
                        }
                    }

                    //如果侧滑菜单已打开,则关掉
                    if (slidingMenu.isMenuShowing()) {
                        slidingMenu.toggle();
                    }
                }
            });
        }

        //设置左侧滑菜单头部按压名字变色
        llIconLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int isTouch = -1;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvName.setTextColor(getResources().getColor(R.color.pressed_red));
                        llIconLayout.onTouchEvent(event);
                        isTouch = 1;
                        break;
                    case MotionEvent.ACTION_UP:
                        tvName.setTextColor(Color.WHITE);
                        isTouch = 2;
                        break;
                }
                if (isTouch == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        //点击头部事件
        llIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改标题
                tvTitleName.setText("个人资料");
                //替换主页fragment
                userFragment = new UserFragment();
                setTitleRightEnable(false);
                //设置标志位防止重复点击添加栈
                if (lastItem != -1) {
                    addFragmentStack(userFragment);
                    lastItem = -1;
                }

                slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                //如果侧滑菜单已打开,则关掉
                if (slidingMenu.isMenuShowing()) {
                    slidingMenu.toggle();
                }
            }
        });
    }

    //右侧滑栏标题
    TextView rightMenuTitle;
    //计数
    TextView pairCount;
    //Tab导航栏
    MagicIndicator magicIndicator;
    //标题数据集合
    List<String> mTitleDataList = new ArrayList<>();
    ViewPager rightMenuViewPager;

    // 初始化右侧菜单栏
    private void initRightMenu() {
        //fragment放入对应集合
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllPairFragment());
        fragments.add(initConversationList());
        //右侧滑栏标题
        rightMenuTitle = (TextView) findViewById(R.id.right_menu_title);
        pairCount = (TextView) findViewById(R.id.pair_count);
        //Tab导航栏
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        //设置ViewPager适配器
        rightMenuViewPager = (ViewPager) findViewById(R.id.view_pager);
        rightMenuViewPager.setAdapter(new RightMenuFragAdapter(getSupportFragmentManager(),
                fragments));
        //标题数据放入对应集合
        mTitleDataList.add("所有配对");
        mTitleDataList.add("聊天");
        //设置导航栏适配器
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new
                        ColorTransitionPagerTitleView(context);
                //设置标题未选中颜色
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color
                        .dim_gray));
                //设置标题选中颜色
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                //设置对应标题
                colorTransitionPagerTitleView.setText(mTitleDataList.get(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rightMenuViewPager.setCurrentItem(index);
                    }
                });
                Logger.t(TAG).w("右侧滑栏fragment下标:" + index);
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //设置下划线模式
                //MODE_MATCH_EDGE 长度铺满标签
                //MODE_WRAP_CONTENT 长度和标题长度一样
                //MODE_EXACTLY 长度一点点
                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                //设置下划线颜色
                indicator.setColors(getResources().getColor(R.color.indicator_line));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        //与ViewPager绑定
        rightMenuViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                rightMenuTitle.setText(mTitleDataList.get(position));
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
//        ViewPagerHelper.bind(magicIndicator, rightMenuViewPager);
        //设置会话监听
    }

    //初始化聊天列表
    private Fragment initConversationList() {
        if (mConversationFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri listUri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    //true聚合显示,false不聚合显示
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                    .build();
            listFragment.setUri(listUri);
            mConversationFragment = listFragment;
        }
        return mConversationFragment;
    }

    //初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");

        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span = 0;
        option.setScanSpan(span);

        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);

        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);

        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(false);

        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);

        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(true);

        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);

        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);

//        mLocationClient.setLocOption(option);
    }

    // 初始化显示FindFragment
    private void initFragment() {
        fl_container.bringToFront();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_fragment, fragments[0])
                .addToBackStack(fragments[0].getClass().getSimpleName())
                .commit();
    }

    private void addFragmentStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
                .setCustomAnimations(R.anim.open_enter, R.anim.open_exit,
                        R.anim.open_enter, R.anim.open_exit)
                .replace(R.id.fl_fragment, fragment)
                .commit();
        Logger.t(TAG).w("添加到回退栈的fragment:" + fragment.getClass().getSimpleName());
    }

    // 点击展示对应的侧滑菜单
    @OnClick({R.id.title_left_menu, R.id.title_right_menu})
    void titleClick(ImageView v) {
        switch (v.getId()) {
            case R.id.title_left_menu:
                slidingMenu.toggle();
                break;
            case R.id.title_right_menu:
                if (titleRightState == ChatConfig.TITLE_RIGHT_IMAGE_IS_MENU) {
                    slidingMenu.showSecondaryMenu();
                } else if (titleRightState == ChatConfig.TITLE_RIGHT_IMAGE_IS_EDIT) {
                    startActivity(new Intent(this, UserEditActivity.class));
                }
                break;
        }
    }

    boolean doubleExitFlag = false;

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        } else {
            //返回键fragment弹栈
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                Logger.t(TAG).w("fragment回退栈数目:" + getSupportFragmentManager()
                        .getBackStackEntryCount());
                getSupportFragmentManager().popBackStackImmediate();
                int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
                Logger.t(TAG).w("回退栈最后一位下标:" + index);
                String stackCurrentFragment = getSupportFragmentManager()
                        .getBackStackEntryAt(index).getName();
                //更改lastItem,作为fragment栈中的下标
                switch (stackCurrentFragment) {
                    case "FindFragment":
                        lastItem = 0;
                        setTitleRightEnable(true);
                        break;
                    case "SettingFragment":
                        lastItem = 1;
                        setTitleRightEnable(true);
                        break;
                    case "UserFragment":
                        lastItem = -1;
                        setTitleRightEnable(false);
                        break;
                    case "FriendLinkFragment":
                        lastItem = 3;
                        setTitleRightEnable(true);
                        break;
                }
                Logger.t(TAG).w("变化后的lastItem:" + lastItem);
            } else {
                if (doubleExitFlag) {
//                    super.onBackPressed();
                    FragmentUtils.popAllFragments(getSupportFragmentManager());
                    mChatApplication.finishApplication();
                    return;
                }
                doubleExitFlag = true;
                ToastUtils.showShort("再按一次退出程序");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleExitFlag = false;
                    }
                }, 2000);
            }
        }
    }

    private void setTitleRightEnable(boolean flag) {
        //替换右上角图标,更改状态标识符,以便更改点击事件
        if (flag) {
            ivRightMenu.setImageResource(R.drawable.title_right_menu);
            titleRightState = ChatConfig.TITLE_RIGHT_IMAGE_IS_MENU;
        } else {
            ivRightMenu.setImageResource(R.drawable.user_edit);
            titleRightState = ChatConfig.TITLE_RIGHT_IMAGE_IS_EDIT;
        }
    }

    //控制ToolBar变化的广播
    private BroadcastReceiver toolbarChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "hideBar":
                    toolbar.setVisibility(View.GONE);
                    //设置侧滑菜单不能滑出
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    ((RelativeLayout.LayoutParams) fl_container.getLayoutParams()).setMargins(0,
                            SizeUtils.dp2px(25), 0, 0);
                    break;
                case "showBar":
                    toolbar.setVisibility(View.VISIBLE);
                    //设置侧滑菜单不能滑出
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    ((RelativeLayout.LayoutParams) fl_container.getLayoutParams()).setMargins(0,
                            SizeUtils.dp2px(72), 0, 0);
                    break;
            }
        }
    };

    //连接融云
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(ChatApplication.getCurProcessName
                (this))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Logger.t(TAG).w("--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Logger.t(TAG).w("--onSuccess  " + userid);
                    //设置用户ID,昵称及头像
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid,
                                userid.equals("110") ? "警察" : "移动",
                                userid.equals("110") ? Uri.parse("http://i4.fuimg" +
                                        ".com/591992/0c437a3d83bac7b1.jpg")
                                        : Uri.parse("http://i4.fuimg" +
                                        ".com/591992/f50dfc3f6e01e272.jpg")));
                    }
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
//                    RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
//                    RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
//                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
//                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.t(TAG).w("--onError  " + errorCode);
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("hideBar");
        filter.addAction("showBar");
        registerReceiver(toolbarChangeReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(toolbarChangeReceiver);
    }
}
