package com.example.mengfei.todo.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * 评论消息的实体类
 * Created by mengfei on 2017/3/14.
 */
public class Talk extends DataSupport implements Serializable{

    private Date talkDate;
    private String talkContent;
    @Column(unique = true)
    private String talkId;
    private String taskId;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public Talk(String talkContent) {
        this.talkContent = talkContent;
        this.talkDate = new Date();
        this.talkId = String.valueOf(talkDate.getTime());
    }

    public Date getTalkDate() {
        return talkDate;
    }

    public String getTalkContent() {
        return talkContent;
    }

    public String getTalkId() {
        return talkId;
    }
}
