package code.aide.dn.com.aidecode.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;

import code.aide.dn.com.aidecode.Model.Project;
import code.aide.dn.com.aidecode.R;
import code.aide.dn.com.aidecode.util.DownLoaderTask;
import code.aide.dn.com.aidecode.util.FileUtil;
import code.aide.dn.com.aidecode.util.ZipExtractorTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static code.aide.dn.com.aidecode.Core.App.ProjectPath;
import static code.aide.dn.com.aidecode.util.FileUtil.context;

public class PostActivity extends TouchActivity implements DownLoaderTask.DownLoaderShowTip ,ZipExtractorTask.ZipExtractorTip{
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private Project mProject;
    private FloatingActionButton fab;
    private ImageView topImg;
    private TextView appname, appSize, appUser, appTime, appType,hint;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String zipname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        }
        setContentView(R.layout.activity_post);
        initId();
        init();
        initData();
        initListener();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        toolbar.setTitle(mProject.getTitle());
        setSupportActionBar(toolbar);

        //执行下载操作
        DownLoadTask task = new DownLoadTask(topImg);
        task.execute(mProject.getIcon().getFileUrl());
        appSize.setText(mProject.getSize());
        appUser.setText(mProject.getUid());
        appTime.setText(mProject.getTime());
        int type = mProject.getType();
        switch (type) {
            case FileUtil.Gradle_PROJECT:
                appType.setText("Android Studio");
                break;
            case FileUtil.ECLIPSE_PORJECT:
                appType.setText("Eclipse");
                break;
        }
        hint.setText(mProject.getContent());
        progressBar.setVisibility(View.GONE);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownLoadDialog();
            }
        });
    }

    private void initId() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.post_progress_load);
        fab = (FloatingActionButton) findViewById(R.id.post_fab);
        topImg = (ImageView) findViewById(R.id.post_top_img);
        appname = (TextView) findViewById(R.id.post_name);
        appSize = (TextView) findViewById(R.id.post_size);
        appUser = (TextView) findViewById(R.id.post_user);
        appTime = (TextView) findViewById(R.id.post_time);
        appType = (TextView) findViewById(R.id.post_type);
        hint = (TextView) findViewById(R.id.post_hint);
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mProject = (Project) bundle.getSerializable("data");
        zipname = mProject.getUrl().substring(mProject.getUrl().lastIndexOf("/")+1);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Post Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    public void showDownLoadDialog() {
        new AlertDialog.Builder(this).setTitle("确认")
                .setMessage("是否下载？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        doDownLoadWork();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                })
                .show();
    }
    @Override
    public void showUnzipDialog() {
        new AlertDialog.Builder(this).setTitle("确认")
                .setMessage("是否解压？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        doZipExtractorWork();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                })
                .show();
    }

    @Override
    public void doZipExtractorWork() {

        ZipExtractorTask task = new ZipExtractorTask(ProjectPath+"/"+zipname, ProjectPath + "/"+mProject.getTitle(), this, true);
        task.execute();
    }

    private void doDownLoadWork() {
        DownLoaderTask downLoaderTask = new DownLoaderTask(mProject.getUrl(),ProjectPath,PostActivity.this,true);
        downLoaderTask.execute();
    }

    @Override
    public void zipSuccess() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否打开工程?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        File file = new File(ProjectPath+"/"+mProject.getTitle());
                        FileUtil fileUtil = new FileUtil(PostActivity.this);
                        int type = FileUtil.isGradleOrEclipse(file);
                        switch (type) {
                            case 0: {
                                //普通目录

                                break;
                            }
                            case 1: {
                                //AS项目
                                if (!fileUtil.AideOpenFile(new File(file, "/app/src/main/AndroidManifest.xml"))) {
                                    Toast.makeText(context, "项目打开失败，请确保您的AIDE正常安装", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case 2: {
                                //Eclipse项目
                                if (!fileUtil.AideOpenFile(new File(file, "/AndroidManifest.xml"))) {
                                    Toast.makeText(context, "项目打开失败，请确保您的AIDE正常安装", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                })
                .show();
    }

    /**
     * 异步加载图片
     */
    public class DownLoadTask extends AsyncTask<String ,Void,BitmapDrawable> {
        private ImageView mImageView;
        String url;
        public DownLoadTask(ImageView imageView){
            mImageView = imageView;
        }
        @Override
        protected BitmapDrawable doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = downLoadBitmap(url);
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(),bitmap);
            return  drawable;
        }

        private Bitmap downLoadBitmap(String url) {
            Bitmap bitmap = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            super.onPostExecute(drawable);

            if ( mImageView != null && drawable != null){
                mImageView.setImageDrawable(drawable);
            }
        }
    }
}    /**
     * 异步加载图片
     */
    class DownLoadTask extends AsyncTask<String ,Void,BitmapDrawable> {
    private ImageView mImageView;
    String url;

    public DownLoadTask(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    protected BitmapDrawable doInBackground(String... params) {
        url = params[0];
        Bitmap bitmap = downLoadBitmap(url);
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    private Bitmap downLoadBitmap(String url) {
        Bitmap bitmap = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            bitmap = BitmapFactory.decodeStream(response.body().byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(BitmapDrawable drawable) {
        super.onPostExecute(drawable);

        if (mImageView != null && drawable != null) {
            mImageView.setImageDrawable(drawable);
        }
    }
}