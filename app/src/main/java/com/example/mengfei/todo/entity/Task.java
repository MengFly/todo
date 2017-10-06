package com.example.mengfei.todo.entity;


import android.util.Log;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务的实体类
 * Created by mengfei on 2017/3/14.
 */
public class Task extends DataSupport implements Serializable {

    private int id;
    private String title;
    private String desc;
    private Date createDate;
    private boolean isCompleted;
    private Date doneDate;
    private String taskId;
    private boolean isDelete;

    //想要完成的时间，也是提醒时间
    private Date wantDoneDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task(String title, String desc) {
        this.title = title;
        this.desc = desc;
        this.createDate = new Date();
        taskId = String.valueOf(createDate.getTime());
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setWantDoneDate(Date wantDoneDate) {
        this.wantDoneDate = wantDoneDate;
    }

    public Date getWantDoneDate() {
        return wantDoneDate;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Task && ((Task) obj).getTaskId().equals(getTaskId());
    }

    public Action getAction() {
        List<Action> actions = Action.where("taskid=?", getTaskId()).find(Action.class);
        return actions.isEmpty() ? null : actions.get(0);
    }
}