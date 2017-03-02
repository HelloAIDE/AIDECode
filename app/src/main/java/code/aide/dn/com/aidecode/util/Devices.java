package code.aide.dn.com.aidecode.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by 大牛哥 on 2017/1/30.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class Devices {
    public  static TelephonyManager telephonyManager;
    /**
     * 获取指定字段信息
     *
     * @return
     */
    public static String getDeviceInfo() {

        StringBuffer sb = new StringBuffer();
        sb.append("主板：" + Build.BOARD);
        sb.append("\n系统启动程序版本号：" + Build.BOOTLOADER);
        sb.append("\n系统定制商：" + Build.BRAND);
        sb.append("\ncpu指令集：" + Build.CPU_ABI);
        sb.append("\ncpu指令集2：" + Build.CPU_ABI2);
        sb.append("\n设置参数：" + Build.DEVICE);
        sb.append("\n显示屏参数：" + Build.DISPLAY);
        sb.append("\n无线电固件版本：" + Build.getRadioVersion());
        sb.append("\n硬件识别码：" + Build.FINGERPRINT);
        sb.append("\n硬件名称：" + Build.HARDWARE);
        sb.append("\nHOST:" + Build.HOST);
        sb.append("\n修订版本列表：" + Build.ID);
        sb.append("\n硬件制造商：" + Build.MANUFACTURER);
        sb.append("\n版本：" + Build.MODEL);
        sb.append("\n硬件序列号：" + Build.SERIAL);
        sb.append("\n手机制造商：" + Build.PRODUCT);
        sb.append("\n描述Build的标签：" + Build.TAGS);
        sb.append("\nTIME:" + Build.TIME);
        sb.append("\nbuilder类型：" + Build.TYPE);
        sb.append("\nUSER:" + Build.USER);
        Log.i("TAG", sb.toString());
        return sb.toString();
    }
    public static String getPhoneNumber(Context context){
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber=null;
        NativePhoneNumber=telephonyManager.getLine1Number();
        return NativePhoneNumber==null?"":NativePhoneNumber;
    }
    public static String getDevicesId(Context context){
        String id = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        return id==null?"":id;
    }
}
