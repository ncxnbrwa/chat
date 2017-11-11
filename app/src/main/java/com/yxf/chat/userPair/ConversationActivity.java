package com.yxf.chat.userPair;

import android.os.Bundle;
import android.widget.TextView;

import com.yxf.chat.R;
import com.yxf.chat.appBase.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ConversationActivity extends BaseActivity {
    @BindView(R.id.conversation_title)
    TextView title;
//    @BindView(R.id.conversation_frag)
//    Fragment conversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(getIntent().getData().getQueryParameter("title"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_conversation;
    }

    @OnClick(R.id.conversation_back)
    void back() {
        finish();
    }
}
