package code.aide.dn.com.aidecode.Model;

import java.util.UUID;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 大牛哥 on 2017/2/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class Project  extends BmobObject{
    private String id;
    private String uid;
    private BmobFile icon;
    private String title;
    private String content;
    private String url;
    private String time;
    private String size;
    private int type;

    public Project(String uid, BmobFile icon, String title, String content, String url, String time, int type,String size) {
        this.id = UUID.randomUUID().toString();
        this.uid = uid;
        this.icon = icon;
        this.title = title;
        this.content = content;
        this.url = url;
        this.time = time;
        this.type = type;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Project{" +
                "uid='" + uid + '\'' +
                ", icon=" + icon +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                '}';
    }
}
