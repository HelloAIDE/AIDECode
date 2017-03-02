package code.aide.dn.com.aidecode.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import code.aide.dn.com.aidecode.Adapter.HorizontalPagerAdapter;
import code.aide.dn.com.aidecode.Core.App;
import code.aide.dn.com.aidecode.Model.PackageName;
import code.aide.dn.com.aidecode.R;
import freemarker.template.Configuration;
import freemarker.template.Template;

import static code.aide.dn.com.aidecode.Core.App.ProjectPath;
import static code.aide.dn.com.aidecode.Core.App.log;
import static code.aide.dn.com.aidecode.util.FileUtil.checkEditEmpty;

public class SelectActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppCompatButton btn_create;
    private AppCompatEditText tv_name;
    private AppCompatEditText tv_package;
    private PackageName packageName;
    private AlertDialog dialog;
    int image = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        toolbar = (Toolbar) findViewById(R.id.select_tool);
        btn_create = (AppCompatButton) findViewById(R.id.select_but_create);
        tv_name = (AppCompatEditText) findViewById(R.id.select_tv_name);
        tv_package = (AppCompatEditText) findViewById(R.id.select_tv_package);
        toolbar.setTitle("创建工程");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        packageName = new PackageName();
        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                (HorizontalInfiniteCycleViewPager) findViewById(R.id.hicvp);
        horizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(this));
        horizontalInfiniteCycleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                image = Math.abs(position % horizontalInfiniteCycleViewPager.getAdapter().getCount());
//                Toast.makeText(SelectActivity.this, "第" + image + "页", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEditEmpty(tv_name)) {
                    tv_name.setSelected(true);
                    tv_package.setSelected(false);
                    tv_name.setError("项目名不能为空");
                    return;
                }
                if (!checkEditEmpty(tv_package)) {
                    tv_name.setSelected(false);
                    tv_package.setSelected(true);
                    tv_package.setError("包名不能为空");
                    return; 
                }
                if(!tv_package.getText().toString().matches("^[a-z]{1,}+\\.[a-z]{1,}+\\.[a-z]{1,}\\.[a-z]{1,}.+?")){
                    tv_package.setError("示例格式:com.myapp.myapplication");
                    return;
                }

                packageName.setAppName(tv_name.getText().toString());
                packageName.setPackagefullname(tv_package.getText().toString());
                createProject();

            }
        });
    }

    private void configDialog(boolean b) {
        if (b) {
            if (dialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请稍后");
                builder.setMessage("工程正在创建");
                builder.setCancelable(false);
                dialog = builder.create();
            }
            this.dialog.show();
            return;
        }
        dialog.dismiss();
        ;
    }

    private void createProject() {
        File file = new File(App.ProjectPath + "/" + packageName.getAppName());
        final String activityName = "MainActivity";
        String layoutName = "activity_main";
        Map map = new HashMap();
        map.put("packagefullname", packageName.getPackagefullname());
        map.put("activityname", activityName);
        map.put("layoutname", layoutName);
        map.put("app_name", packageName.getAppName());
        if (file.exists()) {
            tv_name.setError("该项目已存在");
            return;
        }
        switch (image) {
            case 1:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/BasicActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("BasicActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout");
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("BasicActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("BasicActivity", "MainActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("BasicActivity", "activity_main", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/" + layoutName, map);
                makexml("BasicActivity", "content_main", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/content_main", map);
                makexml("BasicActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            case 2:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/NavigationDrawerActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("NavigationDrawerActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("NavigationDrawerActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("NavigationDrawerActivity", "MainActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("NavigationDrawerActivity", "activity_main", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/" + layoutName, map);
                makexml("NavigationDrawerActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            case 3:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/TabbedActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("TabbedActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("TabbedActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("TabbedActivity", "MainActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("TabbedActivity", "activity_main", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/" + layoutName, map);
                makexml("TabbedActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            case 4:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/FullscreenActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("FullscreenActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout");
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("FullscreenActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("FullscreenActivity", "FullscreenActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("FullscreenActivity", "activity_fullscreen", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/" + layoutName, map);
                makexml("FullscreenActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            case 5:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/LoginActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("LoginActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout");
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("LoginActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("LoginActivity", "LoginActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("LoginActivity", "activity_login", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/" + layoutName, map);
                makexml("LoginActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            case 6:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/ScrollActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("ScrollActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout");
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("ScrollActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("ScrollActivity", "ScrollingActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("ScrollActivity", "activity_scrolling", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/" + layoutName, map);
                makexml("ScrollActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            case 7:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/templet/SettingsActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("SettingsActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("SettingsActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makexml("SettingsActivity", "pref_headers", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/xml/pref_headers", map);
                makejava("SettingsActivity", "AppCompatPreferenceActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() + "/AppCompatPreferenceActivity", map);
                makejava("SettingsActivity", "SettingsActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() +"/"+ activityName, map);
                makexml("SettingsActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;


            default:
                configDialog(true);
                makeRootDirectory(App.ProjectPath);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName());
                copyFolder(App.TempPath + "/EmptyActivity/app", App.ProjectPath + "/" + packageName.getAppName() + "/app");
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/.gitignore", getResources().getString(R.string.gitignore));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/build.gradle", getResources().getString(R.string.build));
                writeFileSdcardFile(App.ProjectPath + "/" + packageName.getAppName() + "/settings.gradle", "include':app'");
                makegradle("EmptyActivity", "build.gradle", App.ProjectPath + "/" + packageName.getAppName() + "/app/", map);
                makeRootDirectory(App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout");
                makeRootDirectory(ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg());
                makexml("EmptyActivity", "AndroidManifest", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/AndroidManifest", map);
                makejava("EmptyActivity", "MainActivity", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/java/" + packageName.getFullpkg() + "/" + activityName, map);
                makexml("EmptyActivity", "activity_main", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/layout/activity_main", map);
                makexml("EmptyActivity", "strings", App.ProjectPath + "/" + packageName.getAppName() + "/app/src/main/res/values/strings", map);
                configDialog(false);
                break;

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(packageName.getAppName() + " 工程创建成功，是否打开？");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = "file://"+ProjectPath+"/"+packageName.getAppName()+"/app/src/main/java/"+ packageName.getFullpkg()+"/"+activityName+".java";
                OpenJavaFile(str);
                finish();
            }
        });
        builder.create().show();

    }

    private void OpenJavaFile(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setDataAndType(Uri.parse(str), "*/.java");
        startActivity(intent);
    }

    private void makegradle(String str, String str2, String str3, Map map) {
        try {
            File file = new File(App.sdcardPath + "/.aidecode/templet/" + str + "/app_");
            log("str:" + str);
            log("str2:" + str2);
            log("str3:" + str3);
            log("makefile:" + file.getPath());

            Writer bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str3 + str2), "utf-8"));
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setDirectoryForTemplateLoading(file);
            configuration.getTemplate(str2 + ".ftl").process(map, bufferWriter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makejava(String str, String str2, String str3, Map map) {
        try {
            File file = new File(App.sdcardPath + "/.aidecode/templet/" + str + "/app_");
            log("str:" + str);
            log("str2:" + str2);
            log("str3:" + str3);
            log("makefile:" + file.getPath());
            log("template0");
            Writer bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str3 + ".java"), "utf-8"));
            Configuration configuration = new Configuration();
            log("template1");
            configuration.setDefaultEncoding("utf-8");
            log("template2");
            configuration.setDirectoryForTemplateLoading(file);
            log("template3");
            Template template = configuration.getTemplate(str2 + ".ftl");
            log("template4");
            template.process(map, bufferWriter);
            log("template:" + template.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makexml(String str, String str2, String str3, Map map) {
        try {
            File file = new File(App.sdcardPath + "/.aidecode/templet/" + str + "/app_");
            log("str:" + str);
            log("str2:" + str2);
            log("str3:" + str3);
            log("makefile:" + file.getPath());

            Writer bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str3 + ".xml"), "utf-8"));
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setDirectoryForTemplateLoading(file);
            configuration.getTemplate(str2 + ".ftl").process(map, bufferWriter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeRootDirectory(String str) {
        log("ROOTDir:" + str);
        File file;

        Object obj;
        try {
            File file2 = new File(str);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            file = file2;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFolder(String str, String str2) {
        try {
            new File(str2).mkdirs();
            String[] list = new File(str).list();
            for (int i = 0; i < list.length; i++) {
                File file = str.endsWith(File.separator) ? new File(str + list[i]) : new File(str + File.separator + list[i]);
                if (file.isFile()) {
                    log("复制文件："+file.getName());
                    FileInputStream fis = new FileInputStream(file);
                    FileOutputStream fos = new FileOutputStream(str2 + "/" + file.getName().toString());
                    byte[] barr = new byte[5120];
                        while (true) {
                            int read = fis.read(barr);
                            if (read == -1) {
                                break;
                            }
                            fos.write(barr, 0, read);
                        }
                    fos.flush();
                    fos.close();
                    fis.close();
                    log("----------复制文件："+file.getName()+"成功");
                }
                if (file.isDirectory()) {
                    log("复制目录："+file.getName());
                    copyFolder(str + "/" + list[i], str2 + "/" + list[i]);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            log("复制文件出错");
        }
    }

    public void writeFileSdcardFile(String str, String str2) {
        try {
            FileOutputStream fos = new FileOutputStream(str);
            fos.write(str2.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_right_left, R.anim.translate_left_right);
    }
}
