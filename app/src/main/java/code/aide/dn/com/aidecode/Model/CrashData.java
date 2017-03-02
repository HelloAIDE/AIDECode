package code.aide.dn.com.aidecode.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by 大牛哥 on 2017/1/30.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class CrashData extends BmobObject {
    private String id;
    private String title;
    private String devicesId;
    private String errorMsg;
    private String time;
    private String devicesName;
    private String devicesVersion;
    private String devicesVersionCode;
    private String devicesInfo;
    private String phoneNumber;

    public String getDevicesId() {
        return devicesId;
    }

    public void setDevicesId(String devicesId) {
        this.devicesId = devicesId;
    }

    public String getDevicesInfo() {
        return devicesInfo;
    }

    public void setDevicesInfo(String devicesInfo) {
        this.devicesInfo = devicesInfo;
    }

    public String getDevicesName() {
        return devicesName;
    }

    public void setDevicesName(String devicesName) {
        this.devicesName = devicesName;
    }

    public String getDevicesVersion() {
        return devicesVersion;
    }

    public void setDevicesVersion(String devicesVersion) {
        this.devicesVersion = devicesVersion;
    }

    public String getDevicesVersionCode() {
        return devicesVersionCode;
    }

    public void setDevicesVersionCode(String devicesVersionCode) {
        this.devicesVersionCode = devicesVersionCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
