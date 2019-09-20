package com.dwtedx.income.entity;

import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/14.
 * Company 路之遥网络科技有限公司
 * Description DiIncome
 */
public class DiIncome {
    private int id;
    private int clientid;
    private String username;
    private int userid;
    private int role;
    private double moneysum;
    private String type;
    private int typeid;
    private String account;
    private int accountid;
    private String remark;
    private String location;
    private String voicepath;
    private String imagepath;
    private int recordtype;
    private String recordtime;
    private String createtime;
    private String updatetime;
    private int isupdate;
    private int serverid;
    private int deletefalag;

    private String color;
    private String icon;
    private String recordtimeformat;
    private String moneysumLeft;
    private String moneysumRight;

    private List<DiScan> scanList;

    public DiIncome() {
    }

    public DiIncome(int id, String username, int userid, int role, double moneysum, String type, int typeid, String account, int accountid, String remark, String location, String voicepath, String imagepath, int recordtype, String recordtime, String createtime, String updatetime, int isupdate, int serverid, int deletefalag, String color, String icon) {
        this.id = id;
        this.username = username;
        this.userid = userid;
        this.role = role;
        this.moneysum = moneysum;
        this.type = type;
        this.typeid = typeid;
        this.account = account;
        this.accountid = accountid;
        this.remark = remark;
        this.location = location;
        this.voicepath = voicepath;
        this.imagepath = imagepath;
        this.recordtype = recordtype;
        this.recordtime = recordtime;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.isupdate = isupdate;
        this.serverid = serverid;
        this.deletefalag = deletefalag;
        this.color = color;
        this.icon = icon;
    }

    public DiIncome(int id, String username, int userid, int role, double moneysum, String type, int typeid, String account, int accountid, String remark, String location, String voicepath, String imagepath, int recordtype, String recordtime, String createtime, String updatetime, int isupdate, int serverid, int deletefalag) {
        this.id = id;
        this.username = username;
        this.userid = userid;
        this.role = role;
        this.moneysum = moneysum;
        this.type = type;
        this.typeid = typeid;
        this.account = account;
        this.accountid = accountid;
        this.remark = remark;
        this.location = location;
        this.voicepath = voicepath;
        this.imagepath = imagepath;
        this.recordtype = recordtype;
        this.recordtime = recordtime;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.isupdate = isupdate;
        this.serverid = serverid;
        this.deletefalag = deletefalag;
    }

    public DiIncome(double allmoneysum, int role, String type, int typeid, String color, String recordtime) {
        this.moneysum = allmoneysum;
        this.role = role;
        this.type = type;
        this.typeid = typeid;
        this.color = color;
        this.recordtime = recordtime;
    }

    public DiIncome(double allmoneysum) {
        this.moneysum = allmoneysum;
    }

    public DiIncome(int role, String moneysumLeft, String moneysumRight, double moneysum, String remark) {
        this.role = role;
        this.moneysumLeft = moneysumLeft;
        this.moneysumRight = moneysumRight;
        this.moneysum = moneysum;
        this.remark = remark;
    }

    public DiIncome(int id, int clientid, String username, int userid, int role, double moneysum, String type, int typeid, String account, int accountid, String remark, String location, String voicepath, String imagepath, int recordtype, String recordtime, String createtime, String updatetime, int isupdate, int serverid, int deletefalag, String color, String icon, String recordtimeformat, String moneysumLeft, String moneysumRight) {
        this.id = id;
        this.clientid = clientid;
        this.username = username;
        this.userid = userid;
        this.role = role;
        this.moneysum = moneysum;
        this.type = type;
        this.typeid = typeid;
        this.account = account;
        this.accountid = accountid;
        this.remark = remark;
        this.location = location;
        this.voicepath = voicepath;
        this.imagepath = imagepath;
        this.recordtype = recordtype;
        this.recordtime = recordtime;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.isupdate = isupdate;
        this.serverid = serverid;
        this.deletefalag = deletefalag;
        this.color = color;
        this.icon = icon;
        this.recordtimeformat = recordtimeformat;
        this.moneysumLeft = moneysumLeft;
        this.moneysumRight = moneysumRight;
    }

    public DiIncome(String recordtime, int isupdate) {
        this.recordtime = recordtime;
        this.isupdate = isupdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public double getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(double moneysum) {
        this.moneysum = moneysum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVoicepath() {
        return voicepath;
    }

    public void setVoicepath(String voicepath) {
        this.voicepath = voicepath;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(String recordtime) {
        this.recordtime = recordtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRecordtimeformat() {
        return recordtimeformat;
    }

    public void setRecordtimeformat(String recordtimeformat) {
        this.recordtimeformat = recordtimeformat;
    }

    public String getMoneysumLeft() {
        return moneysumLeft;
    }

    public void setMoneysumLeft(String moneysumLeft) {
        this.moneysumLeft = moneysumLeft;
    }

    public String getMoneysumRight() {
        return moneysumRight;
    }

    public void setMoneysumRight(String moneysumRight) {
        this.moneysumRight = moneysumRight;
    }

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) {
        this.clientid = clientid;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(int recordtype) {
        this.recordtype = recordtype;
    }

    public List<DiScan> getScanList() {
        return scanList;
    }

    public void setScanList(List<DiScan> scanList) {
        this.scanList = scanList;
    }

    public int getDeletefalag() {
        return deletefalag;
    }

    public void setDeletefalag(int deletefalag) {
        this.deletefalag = deletefalag;
    }

}
