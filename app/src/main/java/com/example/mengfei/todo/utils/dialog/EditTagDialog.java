package com.example.mengfei.todo.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Tag;
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.view.widget.FlowLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 编辑任务的界面
 * Created by mengfei on 2017/5/15.
 */
public class EditTagDialog extends Dialog {

    private Task task;

    private FlowLayout addedTagsLL;
    private FlowLayout allTagsFL;
    private Button addTagBtn;
    private Button okBtn;

    private LinearLayout addTagLL;
    private ImageButton okTagIBtn;
    private ImageButton cancelTagBtn;
    private EditText tagInputEt;
    private UiShower<Void> shower;

    public EditTagDialog(Context context, Task task) {
        this(context, R.style.MyDialogStyle, task, null);
    }

    public EditTagDialog(Context context, Task task, UiShower<Void> shower) {
        this(context, R.style.MyDialogStyle, task, shower);
    }

    private EditTagDialog(Context context, int themeResId, Task task, UiShower<Void> shower) {
        super(context, themeResId);
        this.task = task;
        this.shower = shower;
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.layout_dialog_edit_tag);
        setTitle("Edit Tag");
        addedTagsLL = (FlowLayout) findViewById(R.id.ll_added_tags);
        allTagsFL = (FlowLayout) findViewById(R.id.fl_all_tags);
        addTagBtn = (Button) findViewById(R.id.btn_add_tag);
        okBtn = (Button) findViewById(R.id.btn_ok);
        addTagLL = (LinearLayout) findViewById(R.id.ll_add_tag);
        okTagIBtn = (ImageButton) findViewById(R.id.ibtn_ok);
        cancelTagBtn = (ImageButton) findViewById(R.id.ibtn_cancel);
        tagInputEt = (EditText) findViewById(R.id.et_tag_input);
        initListener();
        initDatas(context);
    }

    private void initDatas(Context context) {
        List<Tag> allTags = DataSupport.findAll(Tag.class);
        for (Tag allTag : allTags) {
            CheckBox cb = getAddedCheckBox(context, allTag);
            if (task.getAddedTags().contains(allTag)) {
                //当它被选中的时候就会自动添加到已经添加的界面里面
                cb.setChecked(true);
            } else {
                //当它没有被选中的时候直接添加到界面里面
                allTagsFL.addView(cb);
            }
        }
    }

    private CheckBox getAddedCheckBox(Context c, final Tag allTag) {
        CheckBox cb = new CheckBox(c);
        cb.setPadding(10, 10, 10, 10);
        cb.setButtonDrawable(R.drawable.none);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.leftMargin = params.topMargin = params.rightMargin = params.bottomMargin = 8;
        cb.setLayoutParams(params);
        cb.setBackgroundResource(R.drawable.back_all_tag);
        cb.setTag(allTag);
        cb.setText(allTag.getName());
        cb.setTextColor(c.getResources().getColor(R.color.color_text_main_dark));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addTag(buttonView, allTag);
                } else {
                    removeTag(buttonView, allTag);
                }
            }
        });
        return cb;
    }

    private void addTag(CompoundButton cb, Tag allTag) {
        addedTagsLL.removeView(cb);
        allTagsFL.removeView(cb);
        addedTagsLL.addView(cb);
        if (task != null) {
            task.addTag(allTag);
        }
        cb.setTextColor(getContext().getResources().getColor(R.color.color_text_main_light));
    }

    private void removeTag(CompoundButton cb, Tag allTag) {
        addedTagsLL.removeView(cb);
        allTagsFL.removeView(cb);
        allTagsFL.addView(cb);
        if (task != null) {
            task.removeTag(allTag);
        }
        cb.setTextColor(getContext().getResources().getColor(R.color.color_text_main_dark));
    }


    private void initListener() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shower != null) {
                    shower.show(null);
                }
                dismiss();
            }
        });
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagLy();
            }
        });
        cancelTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTagLy();
            }
        });
        okTagIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = tagInputEt.getText().toString();
                if (TextUtils.isEmpty(tagName)) {
                    Snackbar.make(okBtn, "Tag Name Can't Empty!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Tag tag = new Tag(tagName);
                    if (tag.save()) {
                        allTagsFL.addView(getAddedCheckBox(getContext(), tag));
                        closeTagLy();
                    } else {
                        Snackbar.make(okBtn, "This Tag Exists!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void closeTagLy() {
        tagInputEt.setText("");
        addTagLL.setVisibility(View.GONE);
        addTagBtn.setVisibility(View.VISIBLE);
    }

    private void openTagLy() {
        tagInputEt.setText("");
        addTagBtn.setVisibility(View.GONE);
        addTagLL.setVisibility(View.VISIBLE);
    }


}
