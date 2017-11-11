package com.yxf.chat.home.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yxf.chat.R;

/**
 * 这是一个锁定宽高比的RelativeLayout
 * Created by xmuSistone on 2016/7/16.
 */
public class AutoScaleRelativeLayout extends RelativeLayout {

    //宽高比
    private float widthHeightRate = 2f;

    public AutoScaleRelativeLayout(Context context) {
        this(context, null);
    }

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.card, 0, 0);
        widthHeightRate = a.getFloat(R.styleable.card_widthHeightRate, widthHeightRate);
        //关闭防内存泄漏
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //调整高度,与宽度成比例,并重新设置
        //xml设置的比例是1,故宽高一致
        int width = getMeasuredWidth();
        int height = (int) (width * widthHeightRate);
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
    }
}
