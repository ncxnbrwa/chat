package com.yxf.chat.userPair;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;

public class AllPairFragment extends BaseFragment {

    @BindView(R.id.all_pair_rv)
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_all_pair;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 测试数据
        List<PairUserBean> pairs = new ArrayList<>();
        pairs.add(new PairUserBean("http://i4.fuimg.com/591992/eda503906a925d52.jpg", "羞答答的玫瑰",
                "配对于12小时前"));
        pairs.add(new PairUserBean("http://i4.fuimg.com/591992/292abf85e72ceada.jpg", "大懒虾",
                "配对于12小时前"));
        pairs.add(new PairUserBean("http://i4.fuimg.com/591992/d9675c817d59e2cd.jpg", "妙舞清旋",
                "配对于12小时前"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AllPairAdapter allPairAdapter = new AllPairAdapter(pairs, getBaseActivity());
        recyclerView.setAdapter(allPairAdapter);
        allPairAdapter.setOnPairItemClickListener(new AllPairAdapter.OnPairItemClickListener() {
            @Override
            public void pairItemClickListener(int position) {
//                getBaseActivity().toActivity(ChatListActivity.class, null);
//                RongIM.getInstance().startConversation(getBaseActivity(), Conversation
//                        .ConversationType.PRIVATE, "10086", "收到回复");

                //手机测
                RongIM.getInstance().startPrivateChat(getBaseActivity(),"10086","移动");
                //模拟器测
//                RongIM.getInstance().startPrivateChat(getBaseActivity(),"110","警察");
            }
        });
    }
}
