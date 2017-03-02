package code.aide.dn.com.aidecode.Model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 大牛哥 on 2017/2/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class File extends BmobObject {
    private String name ;
    private BmobFile bmobFile;
    private User user;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File(String name, BmobFile bmobFile,User user,String content) {
        this.name = name;
        this.bmobFile = bmobFile;
        this.user = user;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
