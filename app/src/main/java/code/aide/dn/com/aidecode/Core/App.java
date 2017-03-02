package code.aide.dn.com.aidecode.Core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.ArrayList;

import code.aide.dn.com.aidecode.Activity.MainActivity;

/**
 * Created by 大牛哥 on 2017/1/14.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class App extends Application {
    public final static String TAG = "TAG";
    public static boolean DEBUG = false;
    private final static float HEAP_UTILIZATION = 0.75f;
    private final static int MIN_HEAP_SIZE = 6 * 1024 * 1024;
    public final static String sdcardPath = Environment.getExternalStorageDirectory().toString();
    public final static String ProjectPath = sdcardPath+"/AppProjects";
    public final static String TempPath = sdcardPath+"/.aidecode/templet";
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    ArrayList<Activity> list = new ArrayList<Activity>();

    public void init() {

        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程  
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public static void BetaToast(Context context){
        Snackbar.make(((MainActivity)(context)).fab, "功能开发中，后续版本开放！", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public static void log(String str){
        Log.i(TAG,str);
    }

}
