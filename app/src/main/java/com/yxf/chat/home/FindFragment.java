package com.yxf.chat.home;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseFragment;
import com.yxf.chat.home.customView.CardItemView;
import com.yxf.chat.home.customView.CardSlidePanel;
import com.yxf.chat.home.customView.CardSlidePanel.CardSwitchListener;
import com.yxf.chat.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * 滑卡片的fragment
 *
 * @author xiong
 * @name FindFragment
 * @date 2017/8/20
 */
public class FindFragment extends BaseFragment {
    public static final String TAG = "FindFragment";

    @BindView(R.id.image_slide_panel)
    CardSlidePanel cardSlidePanel;
    @BindViews({R.id.cardItem_0, R.id.cardItem_1, R.id.cardItem_2, R.id.cardItem_3})
    CardItemView[] cardItemViews;
    @BindView(R.id.card_bottom_layout)
    LinearLayout llCardBottom;

    //用来记录卡片下标
    private int[] cardNums = {3, 2, 1, 0};
    private int numShow;

    private List<CardDataBean> dataList = CardDataBean.initDatas();
    private List<String> urlList = new ArrayList<>();
    private MaterialDialog progressDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        urlList.add("http://i4.fuimg.com/591992/8ed1b8a93a80ac7b.jpg");
        urlList.add("http://i4.fuimg.com/591992/a1af4f020e95c66e.jpg");
        urlList.add("http://i4.fuimg.com/591992/c4afa92830e3fd71.jpg");
        //回调卡片数据
        CardSwitchListener cardSwitchListener = new CardSwitchListener() {

            @Override
            public void onShow(int index) {
                Logger.t(TAG).w("正在显示:" + dataList.get(index).getName());
                cardSlidePanel.setBottomButtonUnchecked();
                // Banner设置初始化状态
                CardItemView.isInitBanner = false;
                if (index <= 3) {
                    numShow = cardNums[index];
                }
                if (index > 3) {
                    numShow = cardNums[index % 4];
                }

                //到数据结束时无限去加重复数据(测试)
                if (index > 0) {
                    dataList.add(new CardDataBean(urlList, "关晓彤", "1997-12-28", "女", "童星"));
                    Logger.t(TAG).w("长度:" + dataList.size());
                }
                Logger.t(TAG).w("card index:" + index + "   numShow:" + numShow);
            }

            //卡片消失回调,type=0代表飞向左侧VANISH_TYPE_LEFT,type=1代表飞向右侧VANISH_TYPE_RIGHT
            @Override
            public void onCardVanish(int index, int type) {
                Logger.t(TAG).w("正在消失:" + dataList.get(index).getName() + " 消失type:" + type);
                //消失时调用对应方法使按钮变化
                switch (type) {
                    case CardSlidePanel.VANISH_TYPE_LEFT:
                        cardSlidePanel.setLeftBottomButtonChecked();
                        break;
                    case CardSlidePanel.VANISH_TYPE_RIGHT:
                        cardSlidePanel.setRightBottomButtonChecked();
                        break;
                }
            }

            //卡片点击回调(当前未触发效果)
            @Override
            public void onItemClick(View cardView, int index) {
                Logger.t(TAG).w(dataList.get(index).getName() + "被点击");
            }
        };
        cardSlidePanel.setCardSwitchListener(cardSwitchListener);
        if (dataList.size() > 0) {
            //填充卡片数据
            cardSlidePanel.fillData(dataList);
        } else {
            //没数据时的处理
            cardSlidePanel.setVisibility(View.GONE);
            ToastUtils.showShort("附近暂无用户");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = DialogUtil.showContentWithProgress(getBaseActivity(), "搜索中。。。");
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("hideCard");
        filter.addAction("showCard");
        getActivity().registerReceiver(layoutChangeReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(layoutChangeReceiver);
    }

    //布局改变的广播
    private BroadcastReceiver layoutChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到隐藏布局的广播
            if (intent.getAction().equals("hideCard")) {
                //底部按钮消失
                llCardBottom.setVisibility(View.GONE);
                for (int i = 3; i >= 0; i--) {
                    if (numShow == i) {
                    } else {
                        cardItemViews[i].setVisibility(View.GONE);
                    }
                }
            } else if (intent.getAction().equals("showCard")) {
                //接收到显示布局的广播
                //底部按钮出现
                llCardBottom.setVisibility(View.VISIBLE);
                for (int i = 3; i >= 0; i--) {
                    cardItemViews[i].setVisibility(View.VISIBLE);
                }
            }

        }
    };

}
