package com.yxf.chat.leftMenuItem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseFragment;
import com.yxf.chat.appBase.ChatConfig;

import butterknife.OnClick;


/**
 * 友情链接界面
 *
 * @author xiong
 * @name FriendLinkFragment
 * @date 2017/8/28
 */
public class FriendLinkFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.yxfpt_card, R.id.hqcy_card})
    public void cardClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.yxfpt_card:
                bundle.putString(ChatConfig.LOAD_URL, ChatConfig.YXFPT);
                getBaseActivity().toActivity(WebLinkActivity.class, bundle);
                break;
            case R.id.hqcy_card:
                bundle.putString(ChatConfig.LOAD_URL, ChatConfig.HQCY);
                getBaseActivity().toActivity(WebLinkActivity.class, bundle);
                break;
        }
    }
}
