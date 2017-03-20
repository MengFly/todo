package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.AbsListView;
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
    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_history_one_words);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        oneWordLv = (ListView) findViewById(R.id.lv_one_words);
        TextView emptyView = (TextView) findViewById(R.id.tv_empty_view);
        oneWordLv.setEmptyView(emptyView);
        oneWordLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.app_main_color));
                } else {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initActionBar("历史每日一句", null, true);
    }
}
