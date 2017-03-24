package com.example.mengfei.todo.entity;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日一句
 * Created by mengfei on 2017/3/14.
 */
public class OneWords extends DataSupport implements Serializable{

    private String note;

    public OneWords(String note) {
        this.note = note;
    }

    @Column(unique = true)
    private String sid;

    private String content;

    private String picture;

    private String picture2;

    private Date getDate;

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public String getNote() {
        return note;
    }

    public SpannableString getShowSpannableString() {
        SpannableString spannableString = new SpannableString(note+ "\n" + content );
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), note.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }
}
