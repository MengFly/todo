package com.example.mengfei.todo.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.TalkManager;
import com.example.mengfei.todo.entity.Task;

/**
 * 这是添加聊天信息的Dialog
 * Created by mengfei on 2017/3/19.
 */
public class AddChatDialog extends Dialog {

    private EditText inputEt;
    private UiShower<Talk> shower;
    private Button okBtn;
    public AddChatDialog(@NonNull Context context, UiShower<Talk> shower, Task task) {
        this(context, 0, shower, task);
    }

    private AddChatDialog(@NonNull Context context, @StyleRes int themeResId, UiShower<Talk> shower, Task task) {
        super(context, themeResId);
        initView(task);
        this.shower = shower;
    }
    private void initView(final Task task) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_add_chat);
        inputEt = (EditText) findViewById(R.id.et_talk_input);
        setTitle(null);
        okBtn = (Button) findViewById(R.id.btn_dialog_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputEt.getText().toString();
                Talk talk = null;
                if (!TextUtils.isEmpty(content)) {
                    talk = TalkManager.getTalk(content);
                    talk.setTaskId(task.getTaskId());
                }
                dismiss();
                shower.show(talk);
            }
        });
    }

    public static AddChatDialog newInstance(Context context, UiShower<Talk> shower, Task task) {
        return  new AddChatDialog(context, shower, task);
    }
}
