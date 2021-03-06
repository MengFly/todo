package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.DailySentence;
import com.example.mengfei.todo.utils.AppFileManager;
import com.example.mengfei.todo.utils.image.ImageLoader;
import com.example.todolib.utils.ImageUtils;
import com.example.todolib.utils.ShareTools;
import com.example.todolib.utils.io.FileManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 分享每日一句的Activity
 * Created by mengfei on 2017/3/23.
 */
public class ShareOneWordActivity extends BaseActivity {
    private static final String INTENT_KEY = "one words";

    private ScrollView shareView;
    private TextView showDateTv;
    private ImageView showImageIv;
    private TextView oneWordsTitleTv;
    private TextView oneWordsDescTv;
    private Button shareBtn;

    private DailySentence dailySentence;

    private static final int HANDLER_SHARE_IMAGE_WHAT = 0x8989;

    private Handler shareCallbackHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_SHARE_IMAGE_WHAT) {
                showToast("图片已保存:" + msg.obj.toString());
                Intent intent = ShareTools.getShareImageIntent(new File(msg.obj.toString()));
                if (isUsedIntentActivity(intent)) {
                    startActivity(Intent.createChooser(intent, "选择要分享的应用"));
                } else {
                    showSnackbar(showImageIv, "您没有对应的应用可以进行分享操作");
                }
            }
        }
    };

    public static void OpenShareOneWordsActivity(Context context, DailySentence dailySentence) {
        Intent intent = new Intent(context, ShareOneWordActivity.class);
        intent.putExtra(INTENT_KEY, dailySentence);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVirable();
        setContentView(R.layout.layout_activity_one_words_share);
        shareView = (ScrollView) findViewById(R.id.ly_share);
        showDateTv = (TextView) findViewById(R.id.tv_show_date);
        showImageIv = (ImageView) findViewById(R.id.iv_show_image);
        oneWordsTitleTv = (TextView) findViewById(R.id.tv_one_word_content);
        oneWordsDescTv = (TextView) findViewById(R.id.tv_one_word_note);
        shareBtn = (Button) findViewById(R.id.btn_share);
        if (dailySentence == null) {
            showSnackbar(shareView, "初始化失败，请重试");
        } else {
            initUI();
        }
        initListener();
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }

    private void initListener() {
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageAndShare();
            }
        });
    }


    private void saveImageAndShare() {
        final Bitmap shareBitmap = ImageUtils.getScrollViewImage(shareView);
        final String filePath = AppFileManager.getShareFileName("" + dailySentence.getGetDate().getTime());
        new Thread() {
            @Override
            public void run() {
                FileManager.saveBitmap(shareBitmap, filePath);
                Message msg = shareCallbackHandler.obtainMessage();
                msg.what = HANDLER_SHARE_IMAGE_WHAT;
                msg.obj = filePath;
                shareCallbackHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initUI() {
        ImageLoader.loadImage(mContext, dailySentence.getPicture2(), showImageIv, null);
        oneWordsTitleTv.setText(dailySentence.getContent());
        oneWordsDescTv.setText(dailySentence.getNote());
        showDateTv.setText(new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(dailySentence.getGetDate()));
    }


    private void initVirable() {
        Intent intent = getIntent();
        if (intent != null) {
            dailySentence = (DailySentence) intent.getSerializableExtra(INTENT_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_one_words, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
