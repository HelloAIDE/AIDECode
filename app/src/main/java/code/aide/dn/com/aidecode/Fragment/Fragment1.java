package code.aide.dn.com.aidecode.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import code.aide.dn.com.aidecode.Activity.MainActivity;
import code.aide.dn.com.aidecode.Adapter.MyRecyclerAdapter;
import code.aide.dn.com.aidecode.Decoration.SpaceItemDecoration;
import code.aide.dn.com.aidecode.Model.DataModel;
import code.aide.dn.com.aidecode.R;
import code.aide.dn.com.aidecode.Service.BmobServce;
import code.aide.dn.com.aidecode.Service.BmobServiceImp;
import code.aide.dn.com.aidecode.util.FileUtil;
import code.aide.dn.com.aidecode.util.ZipCompressorByAnt;

import static code.aide.dn.com.aidecode.Core.App.log;
import static code.aide.dn.com.aidecode.util.Tools.toast;

/**
 * Created by 健 on 2017/1/13.
 */
@SuppressLint("ValidFragment")
public class Fragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<DataModel> mDatas;
    private Context context;

    private SwipeRefreshLayout swipelayout;
    private View view;
    private RecyclerView recyclerView;
    private NestedScrollView scrllview;
    private ProgressBar progressBar;

    private MyRecyclerAdapter recylclerAdapter;
    private File[] files;
    int count = 10;
    private Handler hand;
    private FileUtil fileUtil;

    public Fragment1(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG", "onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("TAG", "onCreateView()");
        view = inflater.inflate(R.layout.view1, container, false);
        initId();
        init();
        if (mDatas == null) {
            initData();
        } else {
            recylclerAdapter.setData(mDatas);
            progressBar.setVisibility(View.GONE);
        }
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        progressBar.setVisibility(View.VISIBLE);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }
        //后台初始化数据
        hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        recylclerAdapter.setData(mDatas);
                        swipelayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        };
        //启用新线程进行耗时操作
        Thread thread = new Thread() {
            @Override
            public void run() {
                files = FileUtil.getFiles(Environment.getExternalStorageDirectory() + "/AppProjects/", new FileUtil.TimeComparator(true));
                int type = -1;
                for (int i = 0; i < files.length; i++) {
                    DataModel data = new DataModel();
                    data.setTitle(files[i].getName());
                    type = FileUtil.isGradleOrEclipse(files[i]);
                    switch (type) {
                        case 0: {
                            long time = files[i].lastModified();//返回文件最后修改时间，是以个long型毫秒数
                            String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
                            data.setSubtitle("普通目录\n\n" + "创建时间:" + ctime);
                            data.setImageId(R.mipmap.folder);
                            break;
                        }
                        case 1: {
                            long time = files[i].lastModified();//返回文件最后修改时间，是以个long型毫秒数
                            String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
                            data.setSubtitle("AndroidStudio项目\n\n" + "创建时间:" + ctime);
                            data.setImageId(R.mipmap.as);
                            break;
                        }
                        case 2: {
                            long time = files[i].lastModified();//返回文件最后修改时间，是以个long型毫秒数
                            String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
                            data.setSubtitle("Eclipse项目\n\n" + "创建时间:" + ctime);
                            data.setImageId(R.mipmap.eclipse);
                            break;
                        }
                    }
                    mDatas.add(data);
                    Message msg = new Message();
                    msg.what = 0;
                    hand.sendMessage(msg);
//                            recylclerAdapter.addData(data, i);
                }
            }
        };
        thread.start();
    }

    private void init() {
        fileUtil = new FileUtil(context);
        Log.i("TAG", "init()");
        swipelayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        swipelayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipelayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipelayout.setProgressBackgroundColorSchemeResource(android.R.color.white); // 设定下拉圆圈的背景
        swipelayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        //绝对布局，很操蛋！
//        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recylclerAdapter = new MyRecyclerAdapter(context);
        recyclerView.addItemDecoration(new SpaceItemDecoration(8));
        recyclerView.setAdapter(recylclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recylclerAdapter.setOnItemClickListener(new OnItemClickListenerImp());
    }

    private void initId() {
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_recyclerView);
        scrllview = (NestedScrollView) view.findViewById(R.id.srollview);
        progressBar = (ProgressBar) view.findViewById(R.id.main_load_progress);

    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initData();
                // 停止刷新
            }
        }); // 5秒后发送消息，停止刷新
    }

    class OnItemClickListenerImp implements MyRecyclerAdapter.OnItemClickListener {

        @Override
        public void OnClickListener(View view, int pos) {
//            Toast.makeText(context,"Click:"+pos,Toast.LENGTH_SHORT).show();

            //删除cardview_item
            //recylclerAdapter.delete(pos);
            openProject(pos);
        }

        @Override
        public void OnLongClickListener(View view, int pos) {
//            Toast.makeText(context,"Long:"+pos,Toast.LENGTH_SHORT).show();
//            删除cardview_item
//            recylclerAdapter.addData(dataModel4, pos);
//             显示下载提示
//            ((MainActivity)context).showDownLoadDialog();
            createListDialog(view, pos);
        }

    }

    /**
     * 创建一个列表弹窗
     */
    private void createListDialog(View view, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("菜单");
        builder.setItems(R.array.list, new DialogInterface.OnClickListener() {
            /**
             *
             * @param dialog 当前的对话框
             * @param which 当前点击的是列表的第几个 item
             *
            <item>打开</item>
            <item>重命名</item>
            <item>删除</item>
            <item>分享</item>
            <item>隐藏</item>
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openProject(pos);
                        toast(context, "打开成功");
                        break;
                    case 1:
                        renameProject(pos);
                        break;
                    case 2:
                        removeProject(pos);
                        break;
                    case 3:
                        shareProject(pos);
                        break;
                    case 4:
                        hideProject(pos);
                        break;
                }
            }
        });
//        builder.setCancelable(false);//不允许被某些方式取消,比如按对话框之外的区域或者是返回键
        builder.show();
    }

    /**
     * 删除指定项目
     *
     * @param pos
     */
    private void removeProject(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除");
        builder.setMessage("项目删除后将不可恢复，确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File[] temp = new File[files.length - 1];
                for (int i = 0; i < temp.length; i++) {
                    if (i < pos) {
                        temp[i] = files[i];
                    } else if (i >= pos && i != files.length) {
                        temp[i] = files[i + 1];
                    }
                }
                FileUtil.deleteDir(files[pos]);
                recylclerAdapter.delete(pos);
                files = temp;
                toast(context, "删除成功");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 重命名项目
     *
     * @param pos
     */
    private void renameProject(final int pos) {

        final AppCompatEditText tv_name;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("重命名");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_rename_view, null, false);
        tv_name = (AppCompatEditText) view.findViewById(R.id.dialog_name);
        tv_name.setText("" + files[pos].getName());
        builder.setView(view);
        builder.setPositiveButton
                ("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newname = tv_name.getText().toString();
                        FileUtil.renameFile(files[pos].getParent(), files[pos].getName(), newname);
                        files[pos] = new File(files[pos].getParent() + "/" + newname);
                        toast(context, "重命名成功");
                        mDatas.get(pos).setTitle(newname);
                        recylclerAdapter.setData(mDatas);
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 隐藏项目
     *
     * @param pos
     */
    private void hideProject(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("隐藏后项目将在此列表不可见,是否隐藏？");
        builder.setPositiveButton
                ("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileUtil.createFile(files[pos], ".codehide");
                        File[] temp = new File[files.length - 1];
                        for (int i = 0; i < temp.length; i++) {
                            if (i < pos) {
                                temp[i] = files[i];
                            } else if (i >= pos && i != files.length) {
                                temp[i] = files[i + 1];
                            }
                        }
                        recylclerAdapter.delete(pos);
                        toast(context, "隐藏成功");
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 分享项目
     *
     * @param pos
     */
    private void shareProject(final int pos) {
        final AppCompatEditText editTextTitle;
        final AppCompatEditText editTextContent;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认分享");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_share_view, null, false);
        editTextTitle = (AppCompatEditText) view.findViewById(R.id.dialog_title);
        editTextContent = (AppCompatEditText) view.findViewById(R.id.dialog_content);
        editTextTitle.setText("" + files[pos].getName());
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTextTitle.getText().toString();
                if (null == title || title.equals("")) {
                    Snackbar.make(((MainActivity) (context)).fab, "标题不能为空", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                String content = editTextContent.getText().toString();
                if (null == content || content.equals("")) {
                    Snackbar.make(((MainActivity) (context)).fab, "简介不能为空", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                postProject(pos, title, content);

            }
        });
        builder.setNegativeButton
                ("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    /**
     * 发布新内容
     *
     * @param pos
     * @param title
     * @param content
     */
    private void postProject(int pos, String title, String content) {

        PostTask task = new PostTask(title, content, files[pos]);
        task.execute();
    }

    private void openProject(int pos) {
        int type = -1;
        type = FileUtil.isGradleOrEclipse(files[pos]);
        switch (type) {
            case 0: {
                //普通目录

                break;
            }
            case 1: {
                //AS项目
                if (!fileUtil.AideOpenFile(new File(files[pos], "/app/src/main/AndroidManifest.xml"))) {
                    Toast.makeText(context, "项目打开失败，请确保您的AIDE正常安装", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 2: {
                //Eclipse项目
                if (!fileUtil.AideOpenFile(new File(files[pos], "/AndroidManifest.xml"))) {
                    Toast.makeText(context, "项目打开失败，请确保您的AIDE正常安装", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    class PostTask extends AsyncTask<Void, Integer, Long> {
        private ProgressDialog mDialog;
        private String title;
        private String content;
        private File projectFile;

        public PostTask(String title, String content, File projectFile) {
            log("title:" + title);
            log("content:" + content);
            log("projectFile:" + projectFile);
            log("zipfile:" + projectFile.getPath() + ".zip");
            log("dir" + projectFile.getPath());
            this.title = title;
            this.content = content;
            this.projectFile = projectFile;
            mDialog = new ProgressDialog(context);
        }

        @Override
        protected Long doInBackground(Void... params) {
            log("doInBackground-start");
            if (projectFile != null) {
                publishProgress(10);
                ZipCompressorByAnt.zip(projectFile.getPath() + ".zip", projectFile.getPath());
                publishProgress(70);
                BmobServce bmobService = new BmobServiceImp(context);
                bmobService.updateFile(projectFile.getPath() + ".zip", projectFile.getName(), content);
                publishProgress(100);
                return 1L;
            }
            return 0L;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            log("onPreExecute-start");
            mDialog.setTitle("分享项目");
            mDialog.setMessage("加载项目文件...");
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            log("onPostExecute-start");
            mDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            log("onProgressUpdate-start");
            mDialog.setProgress(values[0]);
            if (values[0] == 10) {
                mDialog.setMessage("正在压缩项目文件...");
            } else if (values[0] == 70) {
                mDialog.setMessage("正在上传资源...");
            } else if (values[0] == 100) {
                mDialog.setMessage("内容发布成功..");
            }
        }

        @Override
        protected void onCancelled(Long aLong) {
            super.onCancelled(aLong);
            log("onCancelled(Long aLong)-start");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            log("onCancelled-start");

        }
    }
}
