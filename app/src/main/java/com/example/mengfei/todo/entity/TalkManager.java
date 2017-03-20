package com.example.mengfei.todo.entity;

/**
 * Created by mengfei on 2017/3/19.
 */

public class TalkManager {

    public static Talk getTalk(String content){
        return  new Talk(content);
    }
}
