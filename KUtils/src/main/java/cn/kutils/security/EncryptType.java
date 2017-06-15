package cn.kutils.security;

/**
 * 创建时间：2017/6/15  上午9:47
 * 创建人：赵文贇
 * 类描述：
 * 包名：cn.kutils.security
 * 待我代码编好，娶你为妻可好。
 */
public final class EncryptType {
    public static final String AES = "AES";// key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
    public static final String Blowfish = "Blowfish";//key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
    public static final String RC2 = "RC2";//key size must be between 40 and 1024 bits
    public static final String RC4 = "RC4";// key size must be between 40 and 1024 bits
    public static final String DES = "DES";//key size must be equal to 56
    public static final String DESede = "DESede";//DESede(TripleDES) key size must be equal to 112 or 168
}
