package code.aide.dn.com.aidecode.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by 健 on 2017/1/14.
 */

public class DataModel extends BmobObject{
    public String url;
    public String id;
    public String title;
    public String subtitle;
    public int imageId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
