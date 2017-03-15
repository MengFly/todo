package com.example.todolib.en_de_code.impl;

import com.example.todolib.en_de_code.SuperDecode;

import org.bouncycastle.util.encoders.Hex;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 使用AES算法对字符串进行解密
 * TODO:备注:(之后如果项目有用户这一模块的时候可以考虑PBE算法,使用应用口令和用户口令对数据进行加密计算)
 * Created by mengfei on 2017/1/1.
 */
public class DecodeAES implements SuperDecode {

    @Override
    public String decodeString(String decodeStr) throws Exception{
        Key key = new SecretKeySpec(KeyGenerator.generatorKey(), "AES");//获取Key
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(KeyGenerator.getIvByte());
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decodeBytes  = cipher.doFinal(Hex.decode(decodeStr.getBytes()));
        return new String(decodeBytes);
    }
}
