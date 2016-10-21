package com.technology.gisgz.mo5todo.utils.common;

/**
 * Created by Jim.Lee on 2016/6/23.
 */
import android.util.Base64;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;



@SuppressWarnings("restriction")
public class EncryptUtil {

    public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        //return new BASE64Encoder().encode(bytes);
        return Base64.encodeToString(bytes,Base64.URL_SAFE);
    }

    public static String aesDecrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        //byte[] bytes = new BASE64Decoder().decodeBuffer(str);
        byte[] bytes = Base64.decode(str,Base64.URL_SAFE);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }
}
