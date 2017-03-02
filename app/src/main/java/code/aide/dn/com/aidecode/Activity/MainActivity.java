package code.aide.dn.com.aidecode.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import code.aide.dn.com.aidecode.Adapter.ViewPageFrameLayouAdapter;
import code.aide.dn.com.aidecode.Core.App;
import code.aide.dn.com.aidecode.R;
import code.aide.dn.com.aidecode.Service.BmobServce;
import code.aide.dn.com.aidecode.Service.BmobServiceImp;
import code.aide.dn.com.aidecode.util.Devices;
import code.aide.dn.com.aidecode.util.DownLoaderTask;
import code.aide.dn.com.aidecode.util.FileUtil;
import code.aide.dn.com.aidecode.util.NotificationUtil;
import code.aide.dn.com.aidecode.util.ZipExtractorTask;

import static code.aide.dn.com.aidecode.Core.App.BetaToast;
import static code.aide.dn.com.aidecode.util.Devices.getDevicesId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DownLoaderTask.DownLoaderShowTip {
    private final String TAG = "TAG";
    private String[] mTitles = new String[]{"主页", "社区"};
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    public FloatingActionButton fab;
    private DrawerLayout drawer;
    public static NavigationView navigationView;
    private ViewPager viewpage;
    private View view1, view2, view3; // ViewPager包含的页面
    public ImageView imageView;
    public TextView nameTV;
    public TextView timeTV;
    public static Handler handler;
    private View headerView;
    private NotificationUtil mNotificationUtil;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //获取样式中的属性值
            TypedValue typedValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
            int[] attribute = new int[] { android.R.attr.colorPrimary };
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();
            window.setStatusBarColor(color);
        }*/

        setContentView(R.layout.activity_main);
        initFindById();
        initData();
        init();
        initListener();
        Log.e("TAG", "" + nameTV);
        Log.e("TAG", "" + timeTV);
        checkAIDEofInstance();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 初始化事件监听器
     */
    private void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        viewpage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpage.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        BmobServce bmobServce = new BmobServiceImp(this);
        bmobServce.isLogin(getDevicesId(this));
        //添加数据
        for (int i = 0; i < mTitles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]));
        }

    }

    /**
     * 初始化控件
     */
    private void initFindById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        viewpage = (ViewPager) findViewById(R.id.main_viewpage);
        headerView = navigationView.getHeaderView(0);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.icon);
        nameTV = (TextView) headerView.findViewById(R.id.nameTV);
        timeTV = (TextView) headerView.findViewById(R.id.timeTV);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
//                    Toast.makeText(MainActivity.this,"handle",Toast.LENGTH_SHORT).show();
                    Bundle bundle = msg.getData();
                    String name = bundle.getString("name");
                    String email = bundle.getString("email");
                    if (App.DEBUG) {
                        Log.i("DEBUG", "name:" + name + "  email:" + email);
                    }
//                    nameTV.setText("技术交流群：186117943");
//                    timeTV.setText(email);
                    SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = user.edit();

                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("did", Devices.getDevicesId(MainActivity.this));
                    editor.commit();
                }
            }
        };
    }

    /**
     * 初始化设置
     */
    private void init() {
        //设置ToolBar
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.color.colorPrimary);
        mTabLayout.setBackgroundResource(R.color.colorPrimary);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final List<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.view1, null);
        view2 = inflater.inflate(R.layout.view2, null);
        view3 = inflater.inflate(R.layout.view3, null);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        ViewPageFrameLayouAdapter pagerAdapter = new ViewPageFrameLayouAdapter(getSupportFragmentManager(), this, mTitles);
        viewpage.setAdapter(pagerAdapter);

        if (!FileUtil.checkInit()) {
            android.support.v7.app.AlertDialog dialog = null;
            android.support.v7.app.AlertDialog.Builder builder =new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示");
            builder.setMessage("首次运行正在进行初始化数据" +
                    "");
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
            final android.support.v7.app.AlertDialog finalDialog = dialog;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtil.copyAssets(MainActivity.this,"templet",App.sdcardPath+"/.aidecode/templet/");
                    finalDialog.dismiss();
                }
            });
            thread.start();
        }
        //初始化分享sdk
        ShareSDK.initSDK(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_info) {
//            toUserInfoActivity();
            BetaToast(MainActivity.this);
        } else if (id == R.id.nav_post) {
            BetaToast(MainActivity.this);
        } else if (id == R.id.nav_message) {
            BetaToast(MainActivity.this);

        } else if (id == R.id.nav_setting) {
            BetaToast(MainActivity.this);
        } else if (id == R.id.nav_share) {
            showShare();
        } else if (id == R.id.nav_auto) {
            toAboutActivity();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toUserInfoActivity() {
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    private void toAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    public void showDownLoadDialog() {
        new AlertDialog.Builder(this).setTitle("确认")
                .setMessage("是否下载？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onClick 1 = " + which);
                        doDownLoadWork();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onClick 2 = " + which);
                    }
                })
                .show();
    }

    public void showUnzipDialog() {
        new AlertDialog.Builder(this).setTitle("确认")
                .setMessage("是否解压？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onClick 1 = " + which);
                        doZipExtractorWork();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onClick 2 = " + which);
                    }
                })
                .show();
    }

    public void doZipExtractorWork() {
        //ZipExtractorTask task = new ZipExtractorTask("/storage/usb3/system.zip", "/storage/emulated/legacy/", this, true);
        ZipExtractorTask task = new ZipExtractorTask(Environment.getExternalStorageDirectory() + "/46cb8b5e881b40ba9aa895fb66226c32.zip", Environment.getExternalStorageDirectory() + "", this, true);
        task.execute();
    }

    private void doDownLoadWork() {
        DownLoaderTask task = new DownLoaderTask("http://bmob-cdn-8697.b0.upaiyun.com/2017/02/04/46cb8b5e881b40ba9aa895fb66226c32.zip", Environment.getExternalStorageDirectory() + "", this, true);
        //DownLoaderTask task = new DownLoaderTask("http://192.168.9.155/johnny/test.h264", getCacheDir().getAbsolutePath()+"/", this);
        task.execute();
    }

    private void checkAIDEofInstance() {
        FileUtil fileUtil = new FileUtil(MainActivity.this);
        if (!fileUtil.isAppInstalled("com.aide.ui")) {
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("您的设备未安装aide,是否进行安装?");
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DownLoaderTask task = new DownLoaderTask("http://bmob-cdn-8697.b0.upaiyun.com/2017/02/16/60b3e2c5d84046d39a9f469619a547c5.apk", Environment.getExternalStorageDirectory() + "", MainActivity.this, false);
                    //DownLoaderTask task = new DownLoaderTask("http://192.168.9.155/johnny/test.h264", getCacheDir().getAbsolutePath()+"/", this);
                    task.execute();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.translate_right_left, R.anim.translate_left_right);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("AIDE Code");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("https://www.pgyer.com/aidecode");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在使用AIDE Code，感觉很不错，AIDE Code集成一键创建Materal Design 风格软件、对项目进行分享、下载。\n下载地址:https://www.pgyer.com/aidecode");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("https://www.pgyer.com/aidecode");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我正在使用AIDE Code，感觉很不错，AIDE Code集成一键创建Materal Design 风格软件、对项目进行分享、下载。\n" +
                "下载地址:https://www.pgyer.com/aidecode");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://www.pgyer.com/aidecode");

// 启动分享GUI
        oks.show(this);
    }
}
