package cn.kutils.sample.aty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.kutils.klog.KLog;
import cn.kutils.sample.R;
import cn.kutils.security.EncryptType;
import cn.kutils.security.K3DESUtils;
import cn.kutils.security.KBase64Utils;
import cn.kutils.security.KMD5Util;
import cn.kutils.security.KRSAUtils;
import cn.kutils.security.WorkMode;

/**
 * 创建时间：2017/6/15  上午9:17
 * 创建人：赵文贇
 * 类描述：
 * 包名：cn.kutils.sample.aty
 * 待我代码编好，娶你为妻可好。
 */
public class SecurityAty extends AppCompatActivity {
    @Bind(R.id.bt_md5)
    Button mBtMd5;
    @Bind(R.id.rsa_en)
    Button mRsaEn;
    @Bind(R.id.rsa_de)
    Button mRsaDe;
    @Bind(R.id.tv_app)
    TextView mTvApp;
    @Bind(R.id.creatkey_rsa)
    Button mCreatkeyRsa;
    private final String testData = "123456abc";
    private String publicKey;
    private String privateKey;
    private byte[] data_en;//加密后的数据
    private byte[] data_de;//解密后的数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_security);
        ButterKnife.bind(this);
    }


    private void append(String msg) {
        mTvApp.append("\n  " + msg);
    }

    @OnClick({R.id.bt_md5, R.id.creatkey_rsa, R.id.rsa_en, R.id.rsa_de})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_md5:
                String md5_str = KMD5Util.getMD5String("我是要获取MD5的字符串");
                append("摘要结果:" + md5_str + "\n  长度:" + md5_str.length());
                break;
            case R.id.creatkey_rsa:
                append("开始生成密钥对");
                try {
                    Map<String, Object> keyPair = KRSAUtils.genKeyPair();
                    publicKey = KRSAUtils.getPublicKey(keyPair);
                    privateKey = KRSAUtils.getPrivateKey(keyPair);
                    append("密钥生成成功:\n公钥:" + publicKey + "\n私钥:" + privateKey);
                    KLog.json("publicKey:" + publicKey);
                    KLog.json("privateKey:" + privateKey);

                } catch (Exception e) {
                    e.printStackTrace();
                    append("密钥对获取失败");
                }
                break;
            case R.id.rsa_en:
                append("开始加密数据");
                try {
                    data_en = KRSAUtils.encryptByPublicKey(testData.getBytes(), publicKey);
                    append("加密成功,加密后的数据:\n" + KBase64Utils.encode(data_en));
                } catch (Exception e) {
                    e.printStackTrace();
                    append("加密失败," + e
                            .toString());
                }
                break;

/*             publicKey:
            ╔═══════════════════════════════════════════════════════════════════════════════════════
            ║MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdsJeXKErMhVOL6JMlxVerlSMS1Br36lo60meJ
            ║ ZZ4wvcz0JUktrukbkzE6J4Q+3STO7KNYAzM4SpaxVGPzRhw3MWMn9D7bJblFWUNp5vBCW6o66KZW
            ║ Wr9KKVeTo1NTWwIT9EpMx2mFKA+oA7pjMLrApFzOERs3NEzOZCfmhDUjqQIDAQAB
            ╚═══════════════════════════════════════════════════════════════════════════════════════
                privateKey:
            ╔═══════════════════════════════════════════════════════════════════════════════════════
            ║MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN2wl5coSsyFU4vokyXFV6uVIxLU
            ║ GvfqWjrSZ4llnjC9zPQlSS2u6RuTMTonhD7dJM7so1gDMzhKlrFUY/NGHDcxYyf0PtsluUVZQ2nm
            ║ 8EJbqjroplZav0opV5OjU1NbAhP0SkzHaYUoD6gDumMwusCkXM4RGzc0TM5kJ+aENSOpAgMBAAEC
            ║ gYBrqCBVpZl9bJm/7bfXtdf3WTtpH1vhXQtoWVkyXgjOPr3TKnA5ISHO5FIZnasZJCeF3WENkrBC
            ║ ++XmQ8cuErQMKhNZ3YEvqrL1dvuvtHBXUyxnVJnx8duSl4WhwkEWzZaKBpt9vzwcunUHjyLhjt4g
            ║ jJXosCcSsV5GXbsuwxyG6QJBAPVGSZ7F/Jq9xgBTNd8tdCQgqxjSt0IfGJfJ635+7+00HjdUKblE
            ║ ZqnPlwCUpVe1PxizdKokI/6j+rOLn+rVsncCQQDnYklTJafMUx/moKUoDFPDUtgd7+o57kM1LWEJ
            ║ w4EOfdTQXeoVauxHkiUGFoBMkHVQNvDholHFLyS01DlbVELfAkEAiZ3lQqttftkJljcZ292h839M
            ║ 4IGiBvxxjQDjG7dXIO0EIyEaw/Nn8tEhtVaxqpNsEozl4WyjsYUJ7eFiWZ2LIwJAV0swaKsbLC3s
            ║ KiFPTdWgwF7/5AOAdYybVHOjWYClrubwJBBaYOwu6i2LxEk27CvP5oxjwmxvtxm7GnG6lecrNwJB
            ║ AJNLqll4b68xg4xJJuM2eOGxVe9nCi1OFQu4k+OgGB2Hu/tJvh67jtZj3WRCGMI6X3t31XWvvcP2
            ║ koc+0O3m6ZM=
            ╚═══════════════════════════════════════════════════════════════════════════════════════
            */
            case R.id.rsa_de:
                append("开始解密数据");
                try {
                    data_de = KRSAUtils.decryptByPrivateKey(data_en, privateKey);
                    append("解密成功,解密结果:\n" + new String(data_de));
                } catch (Exception e) {
                    e.printStackTrace();
                    append("解密失败," + e
                            .toString());
                }
                break;
        }
    }
}
