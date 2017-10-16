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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.adapter.TextWatcherAdapter;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;
import com.example.todolib.utils.CheckUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取练习人的Dialog
 * Created by mengfei on 2017/8/29.
 */
public class GetContactsDialog extends BaseDialog {
    private LinearLayout loadFromPhoneLL;
    private ListView contactLV;
    private CommonAdapter<Contact> adapter;
    private List<Contact> contacts;

    private CheckBox loadFromPhoneCB;
    private LinearLayout inputNumberLL;
    private EditText inputTitleET;
    private EditText inputNumberET;

    private UiShower<Contact> getContactListener;

    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    public GetContactsDialog(@NonNull Context context, UiShower<Contact> getContactListener) {
        super(context);
        this.getContactListener = getContactListener;
        initDatas();
        initListener();
    }

    private void initDatas() {
        changeOkBtnStat(false);
        //init List
        contacts = new ArrayList<>();
        adapter = new CommonAdapter<Contact>(getContext(), contacts, R.layout.layout_item_contact) {
            @Override
            public void bindItemDatas(ViewHolder holder, Contact bean) {
                ((TextView) holder.getView(R.id.tv_name)).setText(bean.name);
                ((TextView) holder.getView(R.id.tv_number)).setText(bean.number);
                ((ImageView) holder.getView(R.id.iv_icon)).setImageBitmap(bean.icon);
            }
        };
        contactLV.setAdapter(adapter);
    }

    private void initListener() {
        loadFromPhoneCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showLoadPhoneView();
                } else {
                    showInputNumberView();
                }
            }
        });
        contactLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = adapter.getItem(position);
                getContactListener.show(contact);
                dismiss();
            }
        });
        inputNumberET.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckUtils.isTel(s)||CheckUtils.isMobileExact(s)) {
                    changeOkBtnStat(true);
                } else {
                    changeOkBtnStat(false);
                }
            }
        });
        setCancelListener(null);
        setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactListener.show(getContactFromInputView());
                dismiss();
            }
        });


    }

    private void showLoadPhoneView() {
        loadFromPhoneLL.setVisibility(View.VISIBLE);
        inputNumberLL.setVisibility(View.GONE);
        if (contacts.isEmpty()) {
            contacts.addAll(getContacts());
            adapter.notifyDataSetChanged();
        }
    }

    private void showInputNumberView() {
        loadFromPhoneLL.setVisibility(View.GONE);
        inputNumberLL.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_contacts);
        contactLV = (ListView) findViewById(R.id.lv_contacts_list);
        inputNumberLL = (LinearLayout) findViewById(R.id.ll_input_number);
        loadFromPhoneLL = (LinearLayout) findViewById(R.id.ll_load_contacts);
        inputTitleET = (EditText) findViewById(R.id.et_title);
        inputNumberET = (EditText) findViewById(R.id.et_input_number);
        loadFromPhoneCB = (CheckBox) findViewById(R.id.cb_load_from_phone);

        View emptyView = findViewById(R.id.ly_empty);
        TextView emptyTipTv = (TextView) emptyView.findViewById(R.id.tv_empty);
        emptyTipTv.setText("没有读取到联系人，或没有授权。请在【设置】-【权限管理】中检查应用权限");
        contactLV.setEmptyView(emptyView);
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

    private Contact getContactFromInputView() {
        String title = inputTitleET.getText().toString();
        String number = inputNumberET.getText().toString();
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_contact);
        return new Contact(title, number, icon);
    }

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
