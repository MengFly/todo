package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.WebActivity;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.utils.LinkManager;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;
import com.example.todolib.utils.ClipboardUtils;
import com.example.todolib.view.widget.DateTextView;
import com.tencent.smtt.sdk.WebSettings;

import java.util.List;
import java.util.regex.Pattern;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by mengfei on 2017/3/14.
 */
public class TalkAdapter extends CommonAdapter<Talk> {

    public TalkAdapter(Context mContext, List<Talk> mList, int mItemLayoutResorce) {
        super(mContext, mList, mItemLayoutResorce);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, Talk bean) {
        TextView textView = ((TextView) holder.getView(R.id.tv_talk_content));
        textView.setText(bean.getTalkContent());
        LinkManager.linkClick(getContext(), textView);
        ((DateTextView) holder.getView(R.id.tv_talk_date)).setDate(bean.getTalkDate());
    }
}
