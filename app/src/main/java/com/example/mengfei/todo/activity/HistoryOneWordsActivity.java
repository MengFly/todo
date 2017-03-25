package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.OneWordsAdapter;
import com.example.mengfei.todo.entity.OneWords;

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
        List<OneWords> oneWordsList = OneWords.findAll(OneWords.class);
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
        TextView emptyView = (TextView) findViewById(R.id.tv_empty_view);
        oneWordLv.setEmptyView(emptyView);
        oneWordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OneWords oneWords = adapter.getItem(position);
                ShareOneWordActivity.OpenShareOneWordsActivity(mContext, oneWords);
            }
        });
    }
}
