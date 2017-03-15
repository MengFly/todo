package com.example.todolib.en_de_code.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用于获取应用密钥的工具类
 * Created by mengfei on 2017/1/1.
 */
public class KeyGenerator {
    //默认的密钥
    private static final String DEFAULT_KEY = "mengfly.todo";
    private static final byte[]  ivByte = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    //生成默认的keybyte数组
    static byte[] generatorKey() {
        return generatorKey(null);
    }

    static byte[] getIvByte() {
        return  ivByte;
    }

    //生成自定义的keybyte数组
    static byte[] generatorKey(String keyStr) {
        if (keyStr == null) {
            return getMD5DigestByte(DEFAULT_KEY);
        } else {
            return getMD5DigestByte(keyStr);
        }
    }

    private static byte[] getMD5DigestByte(String digestStr) {
        byte[] digestBytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digestBytes = digest.digest(digestStr.getBytes());
        } catch (NoSuchAlgorithmException e) {//this Exception Can't catch
        }
        return digestBytes;
    }



}