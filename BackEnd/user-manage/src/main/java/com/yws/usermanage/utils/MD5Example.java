package com.yws.usermanage.utils;
/**
 * MD5是一种哈希算法，通常用于加密密码或其他敏感信息。
 * 在Java中，可以使用Java自带的MessageDigest类来进行MD5加密和解密。
 * 下面是一个简单的MD5加密和解密的Java代码示例：
 * */

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Example {
    // MD5哈希函数
    public static String MD5hash(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(input.getBytes());
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}