package code.aide.dn.com.aidecode.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by 大牛哥 on 2017/1/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class User extends BmobObject {
    public String id;
    public String name;
    public String nick;
    public String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
