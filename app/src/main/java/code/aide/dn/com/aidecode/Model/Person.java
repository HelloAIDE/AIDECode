package code.aide.dn.com.aidecode.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by 大牛哥 on 2017/1/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class Person  extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}