package com.example.todolib.en_de_code.impl;

import com.example.todolib.en_de_code.SuperMd5;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * Md5Degist的实现类
 * Created by mengfei on 2017/1/5.
 */
public class Md5Degist implements SuperMd5 {
    @Override
    public String md5Degist(String string) {
        MD5Digest digest = new MD5Digest();
        digest.update(string.getBytes(), 0, string.getBytes().length);
        byte[] md5Bytes = new byte[digest.getDigestSize()];
        digest.doFinal(md5Bytes, 0);
        return Hex.toHexString(md5Bytes);
    }
}