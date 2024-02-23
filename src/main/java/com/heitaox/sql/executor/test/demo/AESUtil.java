package com.heitaox.sql.executor.test.demo;

import sun.util.resources.LocaleData;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;

public class AESUtil {

    public static String AES_KEY;

    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";

    private static final String CHARSET = "UTF-8";

    /**
     * 注入秘钥
     * @param aesKey
     */
    public void setAesKeyValue(String aesKey) {
        AESUtil.AES_KEY = aesKey;
    }

    /**
     * 加密数据
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) {

        String cipher = null;
        try {
            byte[] cipherBytes = init(data.getBytes(CHARSET), key.getBytes(CHARSET), Cipher.ENCRYPT_MODE);
            cipher = Base64.getEncoder().encodeToString(cipherBytes);
        } catch (Exception e) {
        }
        return cipher;
    }


    /**
     * 解密数据
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) {
        String decryptData = null;
        try {
            byte[] cipherBytes = Base64.getDecoder().decode(data);
            byte[] dataBytes = init(cipherBytes, key.getBytes(CHARSET), Cipher.DECRYPT_MODE);
            decryptData = new String(dataBytes, CHARSET);
        } catch (Exception e) {
        }
        return decryptData;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] init(byte[] content, byte[] initKey, int modes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //获取密钥
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(initKey);
        kg.init(random);
        // 转换为专用密钥
        SecretKey secretKey = kg.generateKey();
        // 初始化GCM
        GCMParameterSpec encryptSpec = new GCMParameterSpec(128, initKey);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(modes, secretKey, encryptSpec);
        return cipher.doFinal(content);
    }


    public static void main(String[] args) throws Exception {
        System.out.println(decrypt("/HDtrDkqIBMl8fzwyeKQAkGUthSH7GkDWDb5", "81tYGWGWc+Z1VYwzkVr"));
        System.out.println(decrypt("/HY/P6zyqKD0THYXOQAmwWbvy8EoXlclo9ky", "81tYGWGWc+Z1VYwzkVr"));
        System.out.println(decrypt("/HgwOabwrqf6QnEnvmL8pIbc4MTCLpxUioXq", "81tYGWGWc+Z1VYwzkVr"));

        System.out.println(encrypt("13368235896", "81tYGWGWc+Z1VYwzkVr"));
    }
}