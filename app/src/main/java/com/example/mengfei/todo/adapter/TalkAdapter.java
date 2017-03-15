package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Talk;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;
import com.example.todolib.view.widget.DateTextView;

import java.util.List;

/**
 * Created by mengfei on 2017/3/14.
 */
public class TalkAdapter extends CommonAdapter<Talk> {

    public TalkAdapter(Context montext, List<Talk> mList, int mItemLayoutResorce) {
        super(montext, mList, mItemLayoutResorce);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, Talk bean) {
        ((TextView) holder.getView(R.id.tv_talk_content)).setText(bean.getTalkContent());
        ((DateTextView) holder.getView(R.id.tv_talk_date)).setDate(bean.getTalkDate());
    }
}
