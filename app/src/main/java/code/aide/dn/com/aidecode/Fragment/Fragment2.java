package code.aide.dn.com.aidecode.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import code.aide.dn.com.aidecode.Activity.PostActivity;
import code.aide.dn.com.aidecode.Adapter.MyRecyclerAdapter;
import code.aide.dn.com.aidecode.Adapter.MyRecyclerAdapter2;
import code.aide.dn.com.aidecode.Decoration.SpaceItemDecoration;
import code.aide.dn.com.aidecode.Model.Project;
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
public class Fragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<Project> mDatas;
    private Context context;

    private SwipeRefreshLayout swipelayout;
    private View view;
    private RecyclerView recyclerView;
    private NestedScrollView scrllview;
    private ProgressBar progressBar;

    private MyRecyclerAdapter2 recylclerAdapter;
    private File[] files;
    int count = 10;
    private Handler hand;
    private FileUtil fileUtil;


    public Fragment2(Context context) {
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
        view = inflater.inflate(R.layout.view2, container, false);
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
        BmobQuery<Project> query = new BmobQuery<Project>();
    //查询playerName叫“比目”的数据
    //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        query.order("-time");
        //执行查询方法
        query.findObjects(new FindListener<Project>() {
            @Override
            public void done(List<Project> object, BmobException e) {
                if (e == null) {
                    mDatas = object;
                    Message msg = new Message();
                    msg.what = 0;
                    hand.sendMessage(msg);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
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
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//
//
//            }
//        };
//        thread.start();
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
        recylclerAdapter = new MyRecyclerAdapter2(context);
        recyclerView.addItemDecoration(new SpaceItemDecoration(8));
        recyclerView.setAdapter(recylclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recylclerAdapter.setOnItemClickListener(new Fragment2.OnItemClickListenerImp());
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
//            toast(context,"Click:" + pos);
            Intent intent = new Intent(context, PostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",mDatas.get(pos));
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void OnLongClickListener(View view, int pos) {
            toast(context,"Long:" + pos);
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
