package code.aide.dn.com.aidecode.Core;

/**
 * Created by 大牛哥 on 2017/1/14.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import code.aide.dn.com.aidecode.Activity.CrashActivity;
import code.aide.dn.com.aidecode.Model.CrashData;
import code.aide.dn.com.aidecode.Service.BmobServiceImp;
import code.aide.dn.com.aidecode.util.Devices;

public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * Debug Log tag
     */
    public static final String TAG = "CrashHandler";
    /**
     * 是否开启日志输出,在Debug状态下开启,
     * 在Release状态下关闭以提示程序性能
     */
    public static final boolean DEBUG = false;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    /**
     * 异常崩溃数据对象
     */
    private CrashData crashData;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        if (App.DEBUG) {
            Log.i("TAG", "init()--start");
        }
        mContext = ctx;
        crashData = new CrashData();
        crashData.setId(UUID.randomUUID().toString());
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (App.DEBUG) {
            Log.i("TAG", "init()--end");
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (App.DEBUG) {
            Log.i("TAG", "uncaughtException()--start");
        }
        handleException(ex);
//        if (!handleException(ex) && mDefaultHandler != null) {
//            //如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, ex);
//        } else {

        //Sleep一会后结束程序
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (App.DEBUG) {
                Log.e(TAG, "Error : ", e);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
//        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        ex.printStackTrace();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        String text = exception;
        StackTraceElement stackTraceElement= ex.getStackTrace()[0];// 得到异常棧的首个元素
        String FileName = stackTraceElement.getClassName();
        int LineNum = stackTraceElement.getLineNumber();
        String Method = stackTraceElement.getMethodName();
        Log.e("TAG","File="+stackTraceElement.getFileName());// 打印文件名
        Log.e("TAG","Line="+stackTraceElement.getLineNumber());// 打印出错行号
        Log.e("TAG","Method="+stackTraceElement.getMethodName());// 打印出错方法
        if (App.DEBUG) {
            Log.i("TAG", "handleException()--start");
        }
        if (App.DEBUG) {
            Log.i("TAG", "exception--" + ex);
        }
        if (ex == null) {
            if (App.DEBUG) {
                Log.w(TAG, "handleException --- ex==null");
            }
            return true;
        }
        if (App.DEBUG) {
            Log.i("TAG", "handleException()--ifend");
        }
        final String msg = (ex.toString() == null ? "" : ex.toString()) + "\n\n" + (ex.getLocalizedMessage() == null ? "" : ex.getLocalizedMessage())+"\n\n出错文件:"+FileName+"\n\n出错行号:"+LineNum+"行\n\n出错方法:"+Method+"()\n\n错误详情:\n   "+text;
        if (App.DEBUG) {
            Log.i("TAG", "message--" + msg);
        }
        if (msg == null) {
            if (App.DEBUG) {
                Log.i("TAG", "message--null-------" + msg);
            }

            return false;
        }
        if (App.DEBUG) {
            Log.i("TAG", "handleException()--ifend1");
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                if (App.DEBUG) {
                    Log.i("TAG", "run()--start");
                }
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, "程序出错",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                crashData.setErrorMsg(msg);
                restart(msg);
                if (App.DEBUG) {
                    Log.i("TAG", "run()--end");
                }
                Looper.loop();
            }
        }.start();
        if (App.DEBUG) {
            Log.i("TAG", "start()--end");
        }
        //收集设备信息
        collectCrashDeviceInfo(mContext);
        //保存错误报告文件
//        saveCrashInfoToFile(ex);
        //发送错误报告到服务器
//        sendCrashReportsToServer(mContext);
        if (App.DEBUG) {
            Log.i("TAG", "handleException()--end");
        }
        return true;
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public void sendPreviousReportsToServer(CrashData data) {
        this.crashData = data;
        sendCrashReportsToServer(mContext);
    }

    /**
     * 把错误报告发送给服务器
     *
     * @param ctx
     */
    private void sendCrashReportsToServer(Context ctx) {

        if (App.DEBUG) {
            Log.i("TAG", "sendCrashReportsToServer()--start");
        }
        BmobServiceImp bmobServce = new BmobServiceImp(ctx);
        bmobServce.sendCrashMsg(crashData);
        if (App.DEBUG) {
            Log.i("TAG", "sendCrashReportsToServer()--end");
        }

    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        if (App.DEBUG) {
            Log.i("TAG", "collectCrashDeviceInfo()--start");
        }
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                //设备版本号
                crashData.setDevicesVersionCode("" + pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            if (App.DEBUG) {
                Log.e(TAG, "Error while collect package info", e);
            }
        }
        crashData.setDevicesInfo(getDeviceInfo());
        crashData.setDevicesId(Devices.getDevicesId(mContext));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String title = sdf.format(date);
        crashData.setTitle(title);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        crashData.setDevicesVersion("" + currentapiVersion);
        crashData.setDevicesName(Build.BRAND + "  " + Build.MODEL);
        crashData.setPhoneNumber(Devices.getPhoneNumber(mContext));
        //使用反射来收集设备信息.在Build类中包含各种设备信息,
        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        //具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), "" + field.get(null));
                if (App.DEBUG) {
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e) {
                if (App.DEBUG) {
                    Log.e(TAG, "Error while collect crash info", e);
                }
            }
        }
        if (App.DEBUG) {
            Log.i("TAG", "collectCrashDeviceInfo()--end");
        }
    }

    /**
     * 获取指定字段信息
     *
     * @return
     */
    private String getDeviceInfo() {

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
        if (App.DEBUG) {
            Log.i("TAG", sb.toString());
        }
        return sb.toString();
    }

    private void restart(String msg) {
        if (App.DEBUG) {
            Log.i("TAG", "restart()--start");
        }
        Intent intent = new Intent(mContext.getApplicationContext(), CrashActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("err", msg);
        bundle.putSerializable("data", crashData);
        intent.putExtras(bundle);
        PendingIntent restartIntent = PendingIntent.getActivity(
                mContext.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //无延迟跳转
        try {
            restartIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        System.exit(0);
//退出程序
//        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        if(App.DEBUG) {             Log.i("TAG", "restart()--testend");}
//        mgr.set(AlarmManager.RTC, 0,
//                restartIntent); // 1秒钟后重启应用
        System.exit(0);
        if (App.DEBUG) {
            Log.i("TAG", "restart()--end");
        }
    }

}