package com.m7.imkfsdk;

import android.Manifest;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.m7.imkfsdk.utils.PermissionUtils;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.utils.MoorUtils;

import java.util.Locale;


public class MainActivity extends Activity {
    public static String accessId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kf_activity_main);

        /**
         * 文件写入权限 （初始化需要写入文件，点击在线客服按钮之前需打开文件写入权限）
         * 读取设备 ID 权限 （初始化需要获取用户的设备 ID）
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.hasAlwaysDeniedPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {

                PermissionUtils.requestPermissions(this, 0x11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(MainActivity.this, R.string.notpermession, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }
                });
            }
        }

        /**
         * 第一步：初始化help 文件
         */
        final KfStartHelper helper = new KfStartHelper(MainActivity.this);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 第二步
                 * 设置参数
                 * 初始化sdk方法，必须先调用该方法进行初始化后才能使用IM相关功能
                 * @param accessId       接入id（需后台配置获取）
                 * @param userName       用户名
                 * @param userId         用户id
                 */


                //商品信息示例
//                String s = "https://wap.boosoo.com.cn/bobishop/goodsdetail?id=10160&mid=36819";
//                CardInfo ci = new CardInfo("http://seopic.699pic.com/photo/40023/0579.jpg_wh1200.jpg", "我是一个标题当初读书", "我是name当初读书。", "价格 1000-9999", "https://www.baidu.com");
//                try {
//                    ci = new CardInfo("http://seopic.699pic.com/photo/40023/0579.jpg_wh1200.jpg", "我是一个标题当初读书", "我是name当初读书。", "价格 1000-9999", URLEncoder.encode(s, "utf-8"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                helper.initSdkChat("fa0f5110-82ec-11e7-8ca1-8b4900b05172", "测试2", "97289000");//陈辰8002
                helper.initSdkChat("bd696e40-bca1-11e8-8e5f-7bbf4c6bde74", "测试", "123456789");//陈辰正式

            }
        });
        /**
         * 获取未读消息数示例
         */
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (MoorUtils.isInitForUnread(MainActivity.this)) {
                    IMChatManager.getInstance().getMsgUnReadCountFromService(new IMChatManager.HttpUnReadListen() {
                        @Override
                        public void getUnRead(int acount) {
                            Toast.makeText(MainActivity.this, "未读消息数为：" + acount, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //未初始化，消息当然为 ：0
                    Toast.makeText(MainActivity.this, "还没初始化", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionUtils.onRequestPermissionsResult(this, 0x11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);
        }
    }

    /**
     * 语言切换
     * 中文 language：""
     * 英文 language："en"
     */
    private void initLanguage(String language) {
        Resources resources = getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());//更新配置
    }
}
