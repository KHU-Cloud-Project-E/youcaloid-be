package com.example.cloud.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptographyUtils {

    private String alg = "AES/CBC/PKCS5Padding";

    private final String key = "abcdefghabcdefghabcdefghabcdefgh";

    private final String iv ="1234567890123456";

    public String encrypt(Long userId) throws Exception{

        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE,keySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(userId.toString().getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);

    }

    public String decrypt(String text) throws Exception{

        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodeBytes = Base64.getDecoder().decode(text);
        byte[] decrypted = cipher.doFinal(decodeBytes);
        return new String(decrypted, "UTF-8");

    }

}
