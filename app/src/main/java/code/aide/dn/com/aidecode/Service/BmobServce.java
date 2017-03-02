package code.aide.dn.com.aidecode.Service;

import java.util.List;

import code.aide.dn.com.aidecode.Model.CrashData;
import code.aide.dn.com.aidecode.Model.DataModel;
import code.aide.dn.com.aidecode.Model.Project;
import code.aide.dn.com.aidecode.Model.User;

/**
 * Created by 大牛哥 on 2017/1/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public interface BmobServce {
    public List<DataModel> getData();
    public boolean addUser(User user);
    public boolean sendCrashMsg(CrashData crashData);
    public void isLogin(String devicesId);
    public void updateFile(String filepath, String filename,String content);
    public List<Project> getPostList();
}
