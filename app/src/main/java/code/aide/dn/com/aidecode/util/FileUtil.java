package code.aide.dn.com.aidecode.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import static code.aide.dn.com.aidecode.Core.App.log;

/**
 * Created by 大牛哥 on 2017/2/4.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class FileUtil {
    public static Context context;

    private FileUtil() {
    }

    public FileUtil(Context context) {
        this.context = context;
    }

    public static File[] getFiles(String path, Comparator<File> comparator) {
        Log.e("TAG", "PATH:" + path);
        File[] files = {};
        File file = new File(path);
        files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.getName().startsWith(".")) {
                    String[] sub = pathname.list();
                    if (sub == null) {
                        return false;
                    }
                    if (!Arrays.asList(sub).contains(".codehide") && (Arrays.asList(sub).contains("build.gradle") || Arrays.asList(sub).contains("AndroidManifest.xml") || Arrays.asList(sub).contains("project.properties"))) {
                        return true;
                    }
                }
                return false;
            }
        });
        if (file == null) {
            return files;
        }
        if (files == null) {
            files = new File[]{};
        }
        List list = Arrays.asList(files);
        if (comparator == null) {
            Collections.sort(list);
        } else {
            Collections.sort(list, comparator);
        }

        files = (File[]) list.toArray();
        return files;
    }

    /**
     * 不是工程目录
     */
    public final static int NO_PROJECT = 0;
    /**
     * AndroidStudio类型
     */
    public final static int Gradle_PROJECT = 1;
    /**
     * Eclipse类型工程
     */
    public final static int ECLIPSE_PORJECT = 2;

    /**
     * 获取工程类型
     *
     * @param file
     * @return
     */
    public static int isGradleOrEclipse(File file) {

        if (file == null) {
            return NO_PROJECT;
        }
        if (Arrays.asList(file.list()).contains("build.gradle") && !Arrays.asList(file.list()).contains(".codehide")) {
            return Gradle_PROJECT;
        } else if (!Arrays.asList(file.list()).contains(".codehide") && (Arrays.asList(file.list()).contains("AndroidManifest.xml") || Arrays.asList(file.list()).contains("project.properties"))) {
            return ECLIPSE_PORJECT;
        }
        return NO_PROJECT;
    }

    /**
     * 排序规则-时间排序
     */
    public static class TimeComparator implements Comparator<File> {
        //是否为降序
        boolean desc;

        public TimeComparator() {
            this.desc = false;
        }

        /**
         * @param desc 是否为降序
         */
        public TimeComparator(boolean desc) {
            this.desc = desc;
        }

        @Override
        public int compare(File o1, File o2) {
            String str1 = String.valueOf(o1.lastModified());
            String str2 = String.valueOf(o2.lastModified());
            if (desc) {
                return str2.compareTo(str1);
            }
            return str1.compareTo(str2);
        }
    }

    /**
     * AIDE打开文件
     *
     * @param file
     * @return
     */
    public boolean AideOpenFile(File file) {
        if (!file.exists()) {
            return false;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.aide.ui", "com.aide.ui.MainActivity");
        if (!isAppInstalled("com.aide.ui")) {
            return false;
        }
        intent.setData(Uri.fromFile(file));
        intent.setComponent(comp);
        context.startActivity(intent);
        return true;
    }

    /**
     * 判断应用是否安装
     *
     * @param packagename
     * @return
     */
    public boolean isAppInstalled(String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * 解压文件
     */
    public long unzip(String in, String out) {
        File mInput = new File(in);
        File mOutput = new File(out);
        long extractedSize = 0L;
        Enumeration<ZipEntry> entries;
        ZipFile zip = null;
        try {
            zip = new ZipFile(mInput);

            long uncompressedSize = getOriginalSize(zip);
            entries = (Enumeration<ZipEntry>) zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                File destination = new File(mOutput, entry.getName());
                if (!destination.getParentFile().exists()) {
                    Log.e("TAG——解压文件:", "make=" + destination.getParentFile().getAbsolutePath());
                    destination.getParentFile().mkdirs();
                }
                FileOutputStream outStream = new FileOutputStream(destination);
                extractedSize += copy(zip.getInputStream(entry), outStream);
                outStream.close();
            }
        } catch (ZipException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {

                zip.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return extractedSize;
    }

    //获取未压缩的大小
    private long getOriginalSize(ZipFile file) {
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
        long originalSize = 0l;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getSize() >= 0) {
                originalSize += entry.getSize();
            }
        }
        return originalSize;
    }

    /**
     * 复制文件
     *
     * @param input
     * @param output
     * @return
     */
    public static int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    /** */
    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists()) {//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                Log.e("TAG", "文件名存在");
                throw new RuntimeException("文件名重复");
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }

    public static File getAppIconFile(File file) {
        File file2 = null;
        if (!file.exists()) {
            return null;
        }
        int t = isGradleOrEclipse(file);
        switch (t) {
            case Gradle_PROJECT:
                file2 = new File(file, "/app/src/main/res/drawable-hdpi/ic_launcher.png");
                if (!file2.exists()) {
                    file2 = new File(file, "/app/src/main/res/drawable-hdpi/icon.png");
                    if (!file2.exists()) {
                        file2 = new File(file, "/app/src/main/res/mipmap-hdpi/ic_launcher.png");
                    }
                    if (!file2.exists()) {
                        file2 = new File(file, "/app/src/main/res/mipmap-hdpi/icon.png");
                    }
                }
                break;
            case ECLIPSE_PORJECT:
                file2 = new File(file, "/res/drawable-hdpi/ic_launcher.png");
                if (!file2.exists()) {
                    file2 = new File(file, "/res/drawable-hdpi/icon.png");
                }
                log(file2.getPath());
                break;
        }
        return file2;
    }

    public static void createFile(File path, String filename) {
        File newfile = new File(path, filename);
        if (!newfile.exists()) {
            try {
                newfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileSizeFormat(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static boolean checkInit() {
        File file = new File(Environment.getExternalStorageDirectory(), "/.aidecode/templet");
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    public static void copyAssets(Context context, String str, String str2) {
        try {
            String[] list = context.getAssets().list(str);
            if (list.length > 0) {
                new File(str2).mkdirs();
                for (String str3 : list) {
                    copyAssets(context, str + "/" + str3, str2 + "/" + str3);
                }
                return;
            }
            InputStream open = context.getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str2));
            copy(open, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkEditEmpty(EditText edit) {
        if (null == edit || edit.getText().toString().equals("")) {

            return false;
        }
        return true;
    }
}
