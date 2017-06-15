package cn.kutils.security;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/5/19.
 */
public class K3DESUtils {
    private static String encryptTypeName = null;//算法名称  可用 DES,DESede,Blowfish
    private static int keySize = -1;/*密钥长度*/
    private static String workMode = null;//工作模式和填充模式

    /**
     * @param encryptTypeName 算法名称  可用 DES,DESede,Blowfish
     * @param keySize         密钥长度  一般指定24
     * @param workMode        工作模式和填充模式()算法/模式/填充                    16字节加密后数据长度         不满16字节加密后长度
     *                        AES/CBC/NoPadding             16                         不支持
     *                        AES/CBC/PKCS5Padding          32                         16
     *                        AES/CBC/ISO10126Padding       32                          16
     *                        AES/CFB/NoPadding             16                          原始数据长度
     *                        AES/CFB/PKCS5Padding          32                          16
     *                        AES/CFB/ISO10126Padding       32                          16
     *                        AES/ECB/NoPadding             16                          不支持
     *                        AES/ECB/PKCS5Padding          32                          16
     *                        AES/ECB/ISO10126Padding       32                          16
     *                        AES/OFB/NoPadding             16                          原始数据长度
     *                        AES/OFB/PKCS5Padding          32                          16
     *                        AES/OFB/ISO10126Padding       32                          16
     *                        AES/PCBC/NoPadding            16                          不支持
     *                        AES/PCBC/PKCS5Padding         32                          16
     *                        AES/PCBC/ISO10126Padding      32                          16
     * @return
     */
    public static boolean setKeyInfo(String encryptTypeName, int keySize, String workMode) {
        boolean isOk = false;
        K3DESUtils.encryptTypeName = encryptTypeName;
        K3DESUtils.keySize = keySize;
        K3DESUtils.workMode = workMode;
        if (K3DESUtils.encryptTypeName != null && !K3DESUtils.encryptTypeName.equals("")
                && keySize > 0
                && K3DESUtils.workMode != null && !K3DESUtils.workMode.equals("")) {
            isOk = true;
//            V.d("3Des加密前参数设置成功\n算法名称设置为：" + encryptTypeName + "\n密钥长度设置为：" + keySize);
        }
        return isOk;
    }


    public static byte[] initKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(encryptTypeName);
            keyGenerator.init(keySize);
            SecretKey secretKey = keyGenerator.generateKey();

//            V.d("密钥生成成功,(secretKey.getFormat()):" + secretKey.getFormat());
//            V.d("-------"+secretKey.toString());
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            V.e("3Des加密密钥生成失败，" + e.getMessage());
            return null;
        }

    }


    public static byte[] encrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = new SecretKeySpec(key, encryptTypeName);
        Cipher c = Cipher.getInstance(workMode);
        c.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptData = c.doFinal(data);
        return encryptData;
    }


    public static byte[] decrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = new SecretKeySpec(key, encryptTypeName);
        Cipher c = Cipher.getInstance(workMode);
        c.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptData = c.doFinal(data);
        return decryptData;
    }
}
