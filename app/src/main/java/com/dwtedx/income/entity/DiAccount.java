package com.dwtedx.income.entity;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/14.
 * Company 路之遥网络科技有限公司
 * Description DiIncome
 */
public class DiAccount {
    private int id;
    private double moneysum;
    private String username;
    private int userid;
    private String name;
    private int type;
    private String icon;
    private String color;
    private int sequence;
    private String remark;
    private String createtime;
    private String updatetime;
    private int serverid;
    private int deletefalag;

    public DiAccount() {
    }

    public DiAccount(double moneysum, String username, int userid, String name, int type, String icon, String color, int sequence, String remark, String createtime, String updatetime, int serverid, int deletefalag) {
        this.moneysum = moneysum;
        this.username = username;
        this.userid = userid;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.color = color;
        this.sequence = sequence;
        this.remark = remark;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.serverid = serverid;
        this.deletefalag = deletefalag;
    }

    public DiAccount(int id, double moneysum, String username, int userid, String name, int type, String icon, String color, int sequence, String remark, String createtime, String updatetime, int serverid, int deletefalag) {
        this.id = id;
        this.moneysum = moneysum;
        this.username = username;
        this.userid = userid;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.color = color;
        this.sequence = sequence;
        this.remark = remark;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.serverid = serverid;
        this.deletefalag = deletefalag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(double moneysum) {
        this.moneysum = moneysum;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getDeletefalag() {
        return deletefalag;
    }

    public void setDeletefalag(int deletefalag) {
        this.deletefalag = deletefalag;
    }
}
