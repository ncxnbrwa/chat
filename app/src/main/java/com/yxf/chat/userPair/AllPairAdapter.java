package com.yxf.chat.userPair;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yxf.chat.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 配对数据adapter
 *
 * @author xiong
 * @name AllPairAdapter
 * @date 2017/8/19
 */
public class AllPairAdapter extends RecyclerView.Adapter<AllPairAdapter.MyViewHolder> {

    private List<PairUserBean> pairs;
    Context context;
    OnPairItemClickListener mOnPairItemClickListener;

    public AllPairAdapter(List<PairUserBean> pairs, Context context) {
        this.pairs = pairs;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout
                .recyclerview_all_pair, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        PairUserBean pairUser = pairs.get(position);
        holder.tvName.setText(pairUser.getName());
        holder.tvDec.setText(pairUser.getDescribe());
        Picasso.with(context).load(pairUser.getUrlIcon()).placeholder(R.drawable.all_pair_icon).into(holder.roundIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPairItemClickListener.pairItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }

    public void setOnPairItemClickListener(OnPairItemClickListener mOnPairItemClickListener) {
        this.mOnPairItemClickListener = mOnPairItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.all_pair_icon)
        CircleImageView roundIcon;
        @BindView(R.id.all_pair_name)
        TextView tvName;
        @BindView(R.id.all_pair_dec)
        TextView tvDec;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //单机事件回调接口
    public interface OnPairItemClickListener {
        void pairItemClickListener(int position);
    }

}
