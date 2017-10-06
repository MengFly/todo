package com.example.mengfei.todo.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by mengfei on 2017/10/6.
 */

public class ActionManager {

    public static void saveAction(Action action, String taskId) {
        DataSupport.deleteAll(Action.class, "taskId=?", taskId);
        if (action != null) {
            action.save();
        }
    }
}
