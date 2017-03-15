package com.example.todolib.en_de_code;

import com.example.todolib.en_de_code.impl.DecodeAES;
import com.example.todolib.en_de_code.impl.EncodeAES;
import com.example.todolib.en_de_code.impl.Md5Degist;

/**
 *  用于获取Encode和Decode的实体类对象
 * Created by mengfei on 2017/3/6.
 */
public class EncodeAndDecodeFactory {

    public static SuperDecode getDecodeHelper() {
        return new DecodeAES();
    }

    public static SuperEncode getEnCodeHelper() {
        return new EncodeAES();
    }

    public static SuperMd5 getMd5Degist() {return new Md5Degist();}

}
