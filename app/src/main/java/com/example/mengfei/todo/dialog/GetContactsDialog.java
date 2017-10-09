package com.example.mengfei.todo.dialog;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取练习人的Dialog
 * Created by mengfei on 2017/8/29.
 */
public class GetContactsDialog extends BaseDialog {
    private CommonAdapter<Contact> adapter;

    private UiShower<Contact> getContactListener;

    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    public GetContactsDialog(@NonNull Context context, UiShower<Contact> getContactListener) {
        super(context);
        this.getContactListener = getContactListener;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_contacts);
        ListView contactLV = (ListView) findViewById(R.id.lv_contacts_list);
        View emptyView = findViewById(R.id.ly_empty);
        TextView emptyTipTv = (TextView) emptyView.findViewById(R.id.tv_empty);
        emptyTipTv.setText("没有读取到联系人，或没有授权。请在【设置】-【权限管理】中检查应用权限");
        contactLV.setEmptyView(emptyView);
        adapter = new CommonAdapter<Contact>(getContext(), getContacts(), R.layout.layout_item_contact) {
            @Override
            public void bindItemDatas(ViewHolder holder, Contact bean) {
                ((TextView) holder.getView(R.id.tv_name)).setText(bean.name);
                ((TextView) holder.getView(R.id.tv_number)).setText(bean.number);
                ((ImageView) holder.getView(R.id.iv_icon)).setImageBitmap(bean.icon);
            }
        };
        contactLV.setAdapter(adapter);
        //设置按钮界面不可见
        findViewById(R.id.ll_dialog_btn).setVisibility(View.GONE);
        contactLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = adapter.getItem(position);
                getContactListener.show(contact);
                dismiss();
            }
        });

    }

    private List<Contact> getContacts() {
        List<Contact> contactsContracts;
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor queryContracts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        if (queryContracts != null) {
            contactsContracts = new ArrayList<>();
            while (queryContracts.moveToNext()) {
                String phoneNumber = queryContracts.getString(queryContracts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (TextUtils.isEmpty(phoneNumber)) continue;
                String contractName = queryContracts.getString(queryContracts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //得到联系人ID
                Long contactid = queryContracts.getLong(queryContracts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                //得到联系人头像ID
                Long photoid = queryContracts.getLong(queryContracts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));

                //得到联系人头像Bitamp
                Bitmap contactPhoto;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
                    contactPhoto = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_contact);
                }
                Contact contact = new Contact(contractName, phoneNumber, contactPhoto);
                contactsContracts.add(contact);
            }
            queryContracts.close();
            return contactsContracts;
        } else {
            return Collections.emptyList();
        }

    }

    /**
     * 联系人的类
     */
    public class Contact {
        public Contact(String name, String number, Bitmap icon) {
            this.name = name;
            this.number = number;
            this.icon = icon;
        }

        public String name;
        public String number;
        public Bitmap icon;
    }
}
