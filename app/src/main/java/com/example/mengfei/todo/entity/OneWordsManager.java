package com.example.mengfei.todo.entity;

import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.net.NetRequestCollector;
import com.example.todolib.net.CommonOkHttpClient;
import com.example.todolib.net.listener.DisposeDataHandle;
import com.example.todolib.net.listener.DisposeDataListener;
import com.example.todolib.net.request.CommonRequest;
import com.google.gson.Gson;

import org.bouncycastle.asn1.dvcs.Data;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by mengfei on 2017/3/17.
 */

public class OneWordsManager {

    public static void getOneWords(final UiShower<OneWords> shower) {
        final OneWords oneWords = OneWords.findLast(OneWords.class);
        if (oneWords != null && SimpleDateFormat.getDateInstance().format(new Date()).equals(SimpleDateFormat.getDateInstance().format(oneWords.getGetDate()))) {
            shower.show(oneWords);
        } else {
            NetRequestCollector.requestQuest(NetRequestCollector.ONE_WORDS_URL, new NetRequestCollector.ResultHandler<OneWords>() {
                @Override
                public void successResult(OneWords oneWords) {
                    oneWords.setGetDate(new Date());
                    oneWords.saveIfNotExist("sid=?", oneWords.getSid());
                    shower.show(oneWords);
                }

                @Override
                public void errorResult(Object resultMessage) {
                    OneWords oneWords = OneWords.findLast(OneWords.class);
                    if (oneWords == null) {
                        oneWords = getDefaultOneWords();
                        oneWords.saveIfNotExist("sid=?", oneWords.getSid());
                    }
                    shower.show(oneWords);
                }
            }, OneWords.class);
        }
    }


    private static OneWords getDefaultOneWords() {
        OneWords oneWords = new OneWords("我不想谋生；我想生活。-- 奥斯卡·王尔德");
        oneWords.setContent("I don't want to earn my living; I want to live. -- Oscar·Wilde");
        oneWords.setGetDate(new Date());
        oneWords.setSid("2539");
        oneWords.setPicture("http://cdn.iciba.com/news/word/20170317.jpg");
        oneWords.setPicture2("http://cdn.iciba.com/news/word/big_20170317b.jpg");
        return oneWords;
    }

    public static List<OneWords> getOneWordsList() {
        return DataSupport.order("getDate desc").find(OneWords.class);
    }

    public static void getRandomOneWords(UiShower<OneWords> oneWordsUiShower) {
        List<OneWords> list = DataSupport.findAll(OneWords.class);
        if (list == null || list.isEmpty()) {
            getOneWords(oneWordsUiShower);
        } else {
            oneWordsUiShower.show(list.get(Math.abs(new Random().nextInt()) % list.size()));
        }
    }
}
