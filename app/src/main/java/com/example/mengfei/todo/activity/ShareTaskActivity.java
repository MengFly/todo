package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.OneWords;
import com.example.mengfei.todo.entity.OneWordsManager;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.AppFileManager;
import com.example.mengfei.todo.utils.image.ImageLoader;
import com.example.todolib.utils.ImageUtils;
import com.example.todolib.utils.ShareTools;
import com.example.todolib.utils.date.DateTools;
import com.example.todolib.utils.io.FileManager;
import com.example.todolib.view.widget.CustomDialogCreater;
import com.example.todolib.view.widget.DateTextView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Date;

/**
 * 分享Task的Activity
 * Created by mengfei on 2017/3/21.
 */

public class ShareTaskActivity extends BaseActivity {
    private static final int HANDLER_WHAT_SAVE = 0x22;
    private static final int HANDLER_WHAT_SHARE = 0x33;
    private static final int RESULT_REQUEST_CODE_PIC = 0x44;

    private Task task;
    private static final String INTENT_KEY = "task";

    private ScrollView shareLayout;
    private ImageView headerBackIV;
    private ImageView addImageIV;
    private LinearLayout chatLayout;

    private TextView taskTitleTv, taskDescTv, taskExtraTv;
    private TextView taskCreateDateTv, taskWantDoneDateTv, taskDoneDateTv;

    private Button shareBtn, saveBtn;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_WHAT_SAVE:
                    showToast("图像已保存" + msg.obj.toString());
                    break;
                case HANDLER_WHAT_SHARE:
                    Intent shareIntent = ShareTools.getShareImageIntent(new File(msg.obj.toString()));
                    if (isUsedIntentActivity(shareIntent)) {
                        startActivity(Intent.createChooser(shareIntent, "选择要分享的应用"));
                    } else {
                        showToast("您的设备没有对应可分享的应用");
                    }
                    break;
            }

        }
    };

    public static void openShareTaskActivity(Context context, Task task) {
        Intent intent = new Intent(context, ShareTaskActivity.class);
        intent.putExtra(INTENT_KEY, task);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVirable();
        setContentView(R.layout.layout_activity_share_task);
        initView();
        initListener();
        initUI();
    }

    private void initListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrShare(HANDLER_WHAT_SAVE);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrShare(HANDLER_WHAT_SHARE);
            }
        });
        addImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogCreater.getItemsDialog(mContext, null, new String[]{"换一张图片", "从手机中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                OneWords oneWords = OneWordsManager.getRandomOneWords();
                                ImageLoader.loadImage(mContext, oneWords.getPicture2(), headerBackIV, null);
                                break;
                            case 1:
                                startActivityForResult(ShareTools.getImageFromAlbumIntent(), RESULT_REQUEST_CODE_PIC);
                                break;

                        }
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_REQUEST_CODE_PIC:
                if (resultCode == RESULT_OK) {
                    Glide.with(mContext).load(data.getData()).into(headerBackIV);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveOrShare(final int type) {
        addImageIV.setVisibility(View.GONE);
        final Bitmap shareBitmap = ImageUtils.getScrollViewImage(shareLayout);
        addImageIV.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                String fileName = AppFileManager.getShareFileName("sharetask_" + TaskManager.getTaskID(task));
                FileManager.saveBitmap(shareBitmap, fileName);
                Message message = handler.obtainMessage();
                message.what = type;//保存选项
                message.obj = fileName;
                handler.sendMessage(message);
            }
        }.start();
    }

    private void initUI() {
        taskTitleTv.setText(task.getTitle());
        taskDescTv.setText(task.getDesc());
        ImageLoader.loadImage(mContext, DataSupport.findLast(OneWords.class).getPicture2(), headerBackIV, null);
        taskCreateDateTv.setText(getShowDate("任务创建时间: ", task.getCreateDate()));
        taskWantDoneDateTv.setText(getShowDate("预计完成时间: ", task.getWantDoneDate()));
        taskDoneDateTv.setText(getShowDate("实际完成时间: ", task.getDoneDate()));
        for (Talk talk : task.getTalks()) {
            chatLayout.addView(getTalkTextView(talk));
        }
        taskExtraTv.setText("我用3天的时间完成了这个任务,感觉棒棒的呢~");
    }

    private View getTalkTextView(Talk talk) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_talk, null);
        ((TextView) itemView.findViewById(R.id.tv_talk_content)).setText(talk.getTalkContent());
        ((DateTextView) itemView.findViewById(R.id.tv_talk_date)).setDate(talk.getTalkDate());
        return itemView;
    }

    private void initVirable() {
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra(INTENT_KEY);
        }
    }

    private void initView() {
        shareLayout = (ScrollView) findViewById(R.id.sv_share);
        headerBackIV = (ImageView) findViewById(R.id.iv_header_back);
        addImageIV = (ImageView) findViewById(R.id.iv_add_image);
        chatLayout = (LinearLayout) findViewById(R.id.ly_talk);
        taskTitleTv = (TextView) findViewById(R.id.tv_task_title);
        taskDescTv = (TextView) findViewById(R.id.tv_task_desc);
        taskExtraTv = (TextView) findViewById(R.id.tv_task_extra);
        shareBtn = (Button) findViewById(R.id.btn_share);
        saveBtn = (Button) findViewById(R.id.btn_save);
        taskCreateDateTv = (TextView) findViewById(R.id.tv_task_create_date);
        taskWantDoneDateTv = (TextView) findViewById(R.id.tv_task_want_done_date);
        taskDoneDateTv = (TextView) findViewById(R.id.tv_task_done_date);
        initActionBar("分享应用", null, true);
    }

    public SpannableString getShowDate(String tip, Date showDate) {
        String showStr;
        if (showDate == null) {
            showStr = tip + "没有日期信息";
        } else {
            showStr = tip + DateTools.formatDate(showDate);
        }
        SpannableString showSp = new SpannableString(showStr);
        showSp.setSpan(new RelativeSizeSpan(1.5f), 0, tip.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        showSp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_text_main_dark)),
                0, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return showSp;
    }
}
