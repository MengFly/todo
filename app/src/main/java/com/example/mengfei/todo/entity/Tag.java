package com.example.mengfei.todo.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 这是为任务的标签的类
 * Created by mengfei on 2017/5/15.
 */
public class Tag extends DataSupport implements Serializable {

    @Column(nullable = false, unique = true)
    private String name;//标签的名称

    public Tag(String tagName) {
        this.name = tagName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTagTask() {
        return DataSupport.where("").order("createDate desc").find(Task.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            if (((Tag) obj).name != null) {
                return ((Tag) obj).name.equals(this.name);
            }
        }
        return false;
    }
}
