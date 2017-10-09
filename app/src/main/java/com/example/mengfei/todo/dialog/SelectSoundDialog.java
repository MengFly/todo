package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;

import java.util.ArrayList;
import java.util.List;

public class SelectSoundDialog extends BaseDialog {
    private List<Ringtone> ringtones;
    private List<Uri> ringUris;
    private ListView selectSoundLV;
    private int selectPosition = 0;


    public SelectSoundDialog(@NonNull Context context) {
        super(context);
        initData();
        initListener();
    }

    private void initListener() {
        setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = ringUris.get(selectPosition);
                AppConfig.getInstance(getContext()).setSelectSound(uri);
            }
        });
        setCancelListener(null);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_select_sound);
        selectSoundLV = (ListView) findViewById(R.id.lv_select_sound);
        selectSoundLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void initData() {
        ringtones = new ArrayList<>();
        ringUris = new ArrayList<>();
        List<String> showList = new ArrayList<>();
        RingtoneManager manager = new RingtoneManager(getContext());
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        int count = manager.getCursor().getCount();
        for (int i = 0; i < count; i++) {
            Ringtone ringtone = manager.getRingtone(i);
            Uri uri = manager.getRingtoneUri(i);
            ringtones.add(ringtone);
            ringUris.add(uri);
            showList.add(ringtone.getTitle(getContext()));
        }
        selectSoundLV.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_single_choice, showList));
        selectSoundLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                playSound(position);
            }
        });

    }

    private void playSound(int position) {
        for (int i = 0; i < ringtones.size(); i++) {
            Ringtone ringtone = ringtones.get(i);
            if (i != position && ringtone.isPlaying()) {
                ringtone.stop();
            } else if (i == position) {
                ringtone.play();
            }
        }
    }

}
