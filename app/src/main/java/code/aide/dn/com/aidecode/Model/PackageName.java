package code.aide.dn.com.aidecode.Model;

import java.io.Serializable;

/**
 * Created by 大牛哥 on 2017/2/18.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class PackageName implements Serializable{
    private String Fullpkg;
    private String packagefullname;
    private String appName;

    public PackageName() {
    }

    public PackageName(String appName, String packagefullname) {
        this.appName = appName;
        this.packagefullname = packagefullname;
        Fullpkg = packagefullname.replaceAll("\\.","/");
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFullpkg() {
        return Fullpkg;
    }

    public void setFullpkg(String fullpkg) {
        Fullpkg = fullpkg;
    }

    public String getPackagefullname() {
        return packagefullname;
    }

    public void setPackagefullname(String packagefullname) {
        this.packagefullname = packagefullname;
        Fullpkg = packagefullname.replaceAll("\\.","/");
    }
}
