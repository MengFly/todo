package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.OneWordsAdapter;
import com.example.mengfei.todo.entity.OneWords;
import com.example.mengfei.todo.entity.OneWordsManager;
import com.example.todolib.utils.DisplayUtils;

import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 查看历史Words的界面
 * Created by mengfei on 2017/3/17.
 */
public class HistoryOneWordsActivity extends BaseActivity {

    private ImageView backImageView;
    private ListView oneWordLv;
    private OneWordsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_history_one_words);
        initActionBar("历史每日一句", null, true);
        initView();
        initDatas();
    }

    private void initDatas() {
        List<OneWords> oneWordsList = OneWordsManager.getOneWordsList();
        adapter = new OneWordsAdapter(mContext, oneWordsList, R.layout.layout_item_one_words);
        oneWordLv.setAdapter(adapter);
        if (oneWordsList != null && oneWordsList.size() > 0) {
            OneWords showPic = oneWordsList.get(Math.abs(new Random().nextInt()) % oneWordsList.size());
            Glide.with(mContext).load(showPic.getPicture2()).bitmapTransform(new BlurTransformation(mContext, 30)).into(backImageView);
        }
    }

    private void initView() {
        backImageView = (ImageView) findViewById(R.id.iv_back);
        oneWordLv = (ListView) findViewById(R.id.lv_one_words);
        oneWordLv.addFooterView(getOneWordsListence());
        oneWordLv.setEmptyView(getEmptyView(null));
        oneWordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < adapter.getCount()) {
                    OneWords oneWords = adapter.getItem(position);
                    ShareOneWordActivity.OpenShareOneWordsActivity(mContext, oneWords);
                }
            }
        });
    }

    //获取有关每日一句出处信息的TextView
    private TextView getOneWordsListence() {
        TextView textView = new TextView(mContext);
        textView.setTextColor(getResources().getColor(R.color.color_text_second_light));
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, DisplayUtils.dip2px(mContext.getApplicationContext(), 20f), 0, DisplayUtils.dip2px(mContext.getApplicationContext(), 20f));
        textView.setText("每日一句信息由金山词霸每日一句平台提供");
        return textView;
    }
}
