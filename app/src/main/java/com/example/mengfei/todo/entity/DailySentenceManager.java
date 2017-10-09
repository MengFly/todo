package com.example.mengfei.todo.entity;

import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.net.NetRequestCollector;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by mengfei on 2017/3/17.
 */

public class DailySentenceManager {

    public static void getOneWords(final UiShower<DailySentence> shower) {
        final DailySentence dailySentence = DailySentence.findLast(DailySentence.class);
        if (dailySentence != null && SimpleDateFormat.getDateInstance().format(new Date()).equals(SimpleDateFormat.getDateInstance().format(dailySentence.getGetDate()))) {
            shower.show(dailySentence);
        } else {
            NetRequestCollector.requestQuest(NetRequestCollector.ONE_WORDS_URL, new NetRequestCollector.ResultHandler<DailySentence>() {
                @Override
                public void successResult(DailySentence dailySentence) {
                    dailySentence.setGetDate(new Date());
                    dailySentence.saveIfNotExist("sid=?", dailySentence.getSid());
                    shower.show(dailySentence);
                }

                @Override
                public void errorResult(Object resultMessage) {
                    DailySentence dailySentence = DailySentence.findLast(DailySentence.class);
                    if (dailySentence == null) {
                        dailySentence = getDefaultOneWords();
                        dailySentence.saveIfNotExist("sid=?", dailySentence.getSid());
                    }
                    shower.show(dailySentence);
                }
            }, DailySentence.class);
        }
    }


    private static DailySentence getDefaultOneWords() {
        DailySentence dailySentence = new DailySentence("我不想谋生；我想生活。-- 奥斯卡·王尔德");
        dailySentence.setContent("I don't want to earn my living; I want to live. -- Oscar·Wilde");
        dailySentence.setGetDate(new Date());
        dailySentence.setSid("2539");
        dailySentence.setPicture("http://cdn.iciba.com/news/word/20170317.jpg");
        dailySentence.setPicture2("http://cdn.iciba.com/news/word/big_20170317b.jpg");
        return dailySentence;
    }

    public static List<DailySentence> getOneWordsList() {
        return DataSupport.order("getDate desc").find(DailySentence.class);
    }

    public static void getRandomOneWords(UiShower<DailySentence> oneWordsUiShower) {
        List<DailySentence> list = DataSupport.findAll(DailySentence.class);
        if (list == null || list.isEmpty()) {
            getOneWords(oneWordsUiShower);
        } else {
            oneWordsUiShower.show(list.get(Math.abs(new Random().nextInt()) % list.size()));
        }
    }
}
