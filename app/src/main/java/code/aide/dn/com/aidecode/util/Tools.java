package code.aide.dn.com.aidecode.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import code.aide.dn.com.aidecode.Activity.MainActivity;

/**
 * Created by 大牛哥 on 2017/2/1.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class Tools extends Bmob{
    public static void toast(Context context,String str){
        Snackbar.make(((MainActivity)(context)).fab, str, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }
    public static String getTime(String format){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(date);
        return time;
    }
}
