package code.aide.dn.com.aidecode.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import code.aide.dn.com.aidecode.Activity.MainActivity;
import code.aide.dn.com.aidecode.Core.App;
import code.aide.dn.com.aidecode.Model.BmobModel;
import code.aide.dn.com.aidecode.Model.CrashData;
import code.aide.dn.com.aidecode.Model.DataModel;
import code.aide.dn.com.aidecode.Model.Project;
import code.aide.dn.com.aidecode.Model.User;
import code.aide.dn.com.aidecode.util.Devices;
import code.aide.dn.com.aidecode.util.FileUtil;

import static code.aide.dn.com.aidecode.Core.App.log;
import static code.aide.dn.com.aidecode.util.Tools.getTime;
import static code.aide.dn.com.aidecode.util.Tools.toast;

/**
 * Created by 大牛哥 on 2017/1/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class BmobServiceImp implements BmobServce {
    public static User user = null;
    private Context context;
    private File mFile;
    private String filename;
    private ProgressDialog mDialog;
    private String url;

    public BmobServiceImp(Context context) {
        this.context = context;
        Bmob.initialize(context, BmobModel.APP_ID);
    }

    @Override
    public List<DataModel> getData() {
        return null;
    }

    @Override
    public boolean addUser(User user) {
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (App.DEBUG) {
                        Toast.makeText(context, "用户添加成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (App.DEBUG) {
                        Toast.makeText(context, "用户添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return false;
    }

    @Override
    public boolean sendCrashMsg(CrashData crashData) {
        crashData.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "反馈成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return false;
    }

    boolean login = false;
    @Override
    public void isLogin(String devicesId) {
        BmobQuery<User> query = new BmobQuery<User>();
        //查询id叫“devicesId”的数据
        query.addWhereEqualTo("id", devicesId);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null && object.size() > 0) {
//                    toast(context,"欢迎回来," + object.get(0).getName());
                    user = object.get(0);
                } else {
                    user = new User();
                    String id = Devices.getDevicesId(context);
                    user.setId(id);
                    String name = "新用户" + id.substring(0, 5);
                    user.setName(name);
                    user.setNick("");
                    user.setEmail("");
                    addUser(user);

//                    toast(context,"注册成功," + name);
                }

//                toast(context,MainActivity.getTimeTV().toString());
//                MainActivity.getImageView().setBackgroundColor(0xff000000);
//                MainActivity.getNameTV().setText(user.getName());
//                MainActivity.getTimeTV().setText(usergetEmail());
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("name", user.getName());
                bundle.putString("email", user.getEmail());
                msg.setData(bundle);
                MainActivity.handler.sendMessage(msg);
            }
        });
    }

    protected void onPreExecute() {
    }

    public void updateFile(final String filepath, final String filename, final String content) {
        mFile = new File(filepath);
        onPreExecute();
        Log.i("TAG", "doInBackground-start");
        final BmobFile bmobFile = new BmobFile(mFile);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

                if (e == null) {
                    url = bmobFile.getFileUrl();
                    final File file = new File(filepath.substring(0, filepath.length() - 4));
                    log("file:" + file);
                    final BmobFile icon = new BmobFile(FileUtil.getAppIconFile(file));
                    log("icon" + icon);
                    final String size = FileUtil.getFileSizeFormat(new File(filepath).length());
                    icon.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Project project = new Project(user.getName(), icon, filename, content, url, getTime("yyyy-MM-dd hh:mm:ss"), FileUtil.isGradleOrEclipse(file),size);
                                log("projecct:" + project);
                                saveObservable(project);
                                toast(context,"上传成功");
                            } else {
                                toast(context,"图标上传失败" + e.getMessage());
                                Log.e("TAG-e", e.getMessage());
                            }
                        }
                    });
//                    mNotificationUtil.cancel(100);
                } else {
                    toast(context,"上传出错:" + e.getMessage());
                }
                mDialog.dismiss();
            }
            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
                if(mDialog==null) {
                    mDialog = new ProgressDialog(context);
                    mDialog.setTitle("上传中...");
                    mDialog.setMessage(mFile.getName());
                    mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mDialog.setCancelable(false);
                    mDialog.show();
                }

                Log.i("TAG", "onProgress-" + value);
                mDialog.setProgress(value);
            }
        });

    }

    @Override
    public List<Project> getPostList() {
        return null;
    }

    private void saveObservable(BmobObject obj) {
        Log.i("TAG", "saveObservable-start\n" + obj);
        obj.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    toast(context,"数据存储成功");
                } else {
                    toast(context,"数据存储失败" + e.getMessage());
                    Log.e("TAG-e", e.getMessage());
                }
            }
        });
    }

}
