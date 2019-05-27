package com.dwtedx.income.entity;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/4/7.
 * Company 路之遥网络科技有限公司
 * Description UMengInfo
 */
public class UMengInfo {

    private String name;
    private String head;
    private String sex;
    private String weixinopenid;
    private String sinaopenid;
    private String qqopenid;
    private int othertype;

    private String username;
    private String password;
    private String smscode;

    public int getOthertype() {
        return othertype;
    }

    public void setOthertype(int othertype) {
        this.othertype = othertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSex() {
        return sex;
    }

    public String getWeixinopenid() {
        return weixinopenid;
    }

    public void setWeixinopenid(String weixinopenid) {
        this.weixinopenid = weixinopenid;
    }

    public String getSinaopenid() {
        return sinaopenid;
    }

    public void setSinaopenid(String sinaopenid) {
        this.sinaopenid = sinaopenid;
    }

    public String getQqopenid() {
        return qqopenid;
    }

    public void setQqopenid(String qqopenid) {
        this.qqopenid = qqopenid;
    }

    public void setSex(String sex) {
        if("1".equals(sex)){
            this.sex = "男";
        }else if("2".equals(sex)){
            this.sex = "女";
        }else if("0".equals(sex)){
            this.sex = "未知";
        }if("m".equals(sex)){
            this.sex = "男";
        }else if("f".equals(sex)){
            this.sex = "女";
        }else {
            this.sex = sex;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }
}
