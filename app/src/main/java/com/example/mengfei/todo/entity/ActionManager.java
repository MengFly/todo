package com.example.mengfei.todo.entity;

import org.litepal.crud.DataSupport;

public class ActionManager {

    public static void saveAction(Action action, String taskId) {
        if (action != null) {
            int actionSumCount = DataSupport.where("taskId=?", taskId).count(Action.class);
            if (actionSumCount == 0) {
                action.save();
            } else {
                // 如果不为零，那么说明存在的那个Action可能是和这个要存储的Action是相同的
                int actionCount = DataSupport.where("taskId=? and title=? and desc=? and type=?",
                        action.taskId, action.title, action.desc, action.type).count(Action.class);
                if (actionCount == 0) {
                    //删除其他Action，保存我们想要保存的
                    DataSupport.deleteAll(Action.class, "taskId=?", taskId);
                    action.save();
                }/*else {
                 //如果不为零，那么说明数据库里面已经存在一个Action记录了，那么就没必要保存这个Action了
                 }**/
            }
        } else {
            //如果Action为空，那么说明这个Task没有设置Action，所以清除所有和Task有关的Action
            DataSupport.deleteAll(Action.class, "taskId=?", taskId);
        }
    }


    public static void deleteAction(Action action) {
        if (action != null) {
            DataSupport.deleteAll(Action.class, "taskId=?", action.taskId);
        }
    }
}
