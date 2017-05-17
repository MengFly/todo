package com.example.mengfei.todo.entity;


import android.content.ContentValues;
import android.text.TextUtils;

import com.example.mengfei.todo.AppConstant;

import org.bouncycastle.asn1.dvcs.Data;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 任务的实体类
 * Created by mengfei on 2017/3/14.
 */
public class Task extends DataSupport implements Serializable {

    //任务类型为文本类型
    public static final String TASK_TYPE_TEXT = "text";
    //任务类型为打电话类型
    public static final String TASK_TYPE_MOBILE = "mobile";
    //任务类型为发送邮件类型
    public static final String TASK_TYPE_EMAIL = "email";
    //任务类型为打开网页类型
    public static final String TASK_TYPE_NET = "net";

    private String title;
    private String desc;
    private Date createDate;
    private boolean isCompleted;
    private Date doneDate;
    @Column(unique = true)
    private String taskId;
    //想要完成的时间，也是提醒时间
    private Date wantDoneDate;

    //任务的标签
    private String tags = "";

    private String taskType = TASK_TYPE_TEXT;//默认为文本类型

    private List<Talk> talks;

    public Task(String title, String desc) {
        this.title = title;
        this.desc = desc;
        this.createDate = new Date();
        taskId = String.valueOf(createDate.getTime());
    }
    public void addTag(Tag tag) {
        if (tags == null || !tags.contains(tag.getName())) {
            tags += tag.getName() + AppConstant.APPSPLITE;
            ContentValues values = new ContentValues();
            values.put("tags", tags);
            updateAll(Task.class, values, "taskId=?", getTaskId());
        }
    }

    public void removeTag(Tag tag) {
        String tagName = tag.getName();
        if (tags != null && tags.contains(tagName)) {
            tags = tags.replace(tagName + AppConstant.APPSPLITE, "");
            ContentValues values = new ContentValues();
            values.put("tags", tags);
            updateAll(Task.class, values, "taskId=?", getTaskId());
        }
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        if (tags == null) {
            this.tags = "";
        }
    }

    public List<Tag> getAddedTags() {
        if (tags == null) {
            return Collections.emptyList();
        }
        String[] tagsName = tags.split(AppConstant.APPSPLITE);
        List<Tag> tags =new ArrayList<>();
        for (String tagName : tagsName) {
            if (!TextUtils.isEmpty(tagName)) {
                Tag tag = new Tag(tagName);
                tags.add(tag);
            }
        }
        return tags;
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

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Talk> getTalks() {
        if (talks == null) {
            talks = DataSupport.where("taskId=?", taskId).order("talkDate desc").find(Talk.class);
        }
        return talks;
    }

    public void setWantDoneDate(Date wantDoneDate) {
        this.wantDoneDate = wantDoneDate;
    }

    public Date getWantDoneDate() {
        return wantDoneDate;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    public String getTaskType() {
        return taskType == null ? TASK_TYPE_TEXT : taskType;//默认是text类型
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void addTalk(Talk talk) {
        talk.save();
        if (talks == null) {
            talks = new ArrayList<>();
        }
        talks.add(talk);
    }
}
