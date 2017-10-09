package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.DailySentence;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;

import java.util.List;

/**
 * Created by mengfei on 2017/3/17.
 */

public class OneWordsAdapter extends CommonAdapter<DailySentence> {
    private Context mContext;

    public OneWordsAdapter(Context mContext, List<DailySentence> mList, int mItemLayoutResorce) {
        super(mContext, mList, mItemLayoutResorce);
        this.mContext = mContext;
    }

    @Override
    public void bindItemDatas(ViewHolder holder, DailySentence bean) {
        Glide.with(mContext).load(bean.getPicture()).into(((ImageView) holder.getView(R.id.iv_pic)));
        ((TextView) holder.getView(R.id.tv_content)).setText(bean.getShowSpannableString());
    }
}
