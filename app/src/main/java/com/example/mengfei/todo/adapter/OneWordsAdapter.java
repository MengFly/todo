package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.OneWords;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by mengfei on 2017/3/17.
 */

public class OneWordsAdapter  extends CommonAdapter<OneWords>{
    private Context mContext;

    public OneWordsAdapter(Context mContext, List<OneWords> mList, int mItemLayoutResorce) {
        super(mContext, mList, mItemLayoutResorce);
        this.mContext = mContext;
    }

    @Override
    public void bindItemDatas(ViewHolder holder, OneWords bean) {
        Glide.with(mContext).load(bean.getPicture()).into(((ImageView) holder.getView(R.id.iv_pic)));
        ((TextView) holder.getView(R.id.tv_content)).setText(bean.getShowSpannableString());
    }
}
