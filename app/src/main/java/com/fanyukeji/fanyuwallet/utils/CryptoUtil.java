package com.fanyukeji.fanyuwallet.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CryptoUtil {

    public static String md5Util(String content) throws NoSuchAlgorithmException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(content.getBytes());
        return new String(digest).substring(0, 8);

    }

    public static String DesEncrypt(String content, String password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        SecureRandom secureRandom = new SecureRandom();

        DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

        byte[] bytes = cipher.doFinal(content.getBytes());
        byte[] base64 = android.util.Base64.encode(bytes, android.util.Base64.DEFAULT);

        return new String(base64);

    }

    public static String DesDecrypt(String content, String password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        byte[] decode = android.util.Base64.decode(content, android.util.Base64.DEFAULT);

        SecureRandom secureRandom = new SecureRandom();

        DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");

        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);

        byte[] bytes = cipher.doFinal(decode);
        return new String(bytes);

    }

}
